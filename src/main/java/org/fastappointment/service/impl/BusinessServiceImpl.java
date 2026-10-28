package org.fastappointment.service.impl;

import org.fastappointment.api.dto.BusinessHoursRequest;
import org.fastappointment.api.dto.BusinessResponse;
import org.fastappointment.api.dto.CreateBusinessRequest;
import org.fastappointment.api.dto.UpdateBusinessRequest;
import org.fastappointment.config.CacheConfig;
import org.fastappointment.entity.concretes.Business;
import org.fastappointment.entity.concretes.BusinessHours;
import org.fastappointment.exception.ConflictException;
import org.fastappointment.exception.ErrorMessageKeys;
import org.fastappointment.exception.NotFoundException;
import org.fastappointment.mapper.BusinessEntityToBusinessResponse;
import org.fastappointment.repository.BusinessHoursRepository;
import org.fastappointment.repository.BusinessRepository;
import org.fastappointment.repository.UserRepository;
import org.fastappointment.service.BusinessService;
import org.fastappointment.util.PhoneNumbers;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessHoursRepository businessHoursRepository;
    private final UserRepository userRepository;
    private final BusinessEntityToBusinessResponse businessEntityToBusinessResponse;

    public BusinessServiceImpl(BusinessRepository businessRepository, BusinessHoursRepository businessHoursRepository,
                               UserRepository userRepository,
                               BusinessEntityToBusinessResponse businessEntityToBusinessResponse) {
        this.businessRepository = businessRepository;
        this.businessHoursRepository = businessHoursRepository;
        this.userRepository = userRepository;
        this.businessEntityToBusinessResponse = businessEntityToBusinessResponse;
    }

    @Override
    @Cacheable(CacheConfig.BUSINESSES_CACHE)
    public List<BusinessResponse> findAll() {
        List<Business> businesses = businessRepository.findAll();
        List<Long> businessIds = businesses.stream()
                .map(Business::getId)
                .toList();
        Map<Long, List<BusinessHours>> hoursByBusinessId = businessHoursRepository.findByBusinessIdIn(businessIds).stream()
                .collect(Collectors.groupingBy(hours -> hours.getBusiness().getId()));

        return businesses.stream()
                .map(business -> businessEntityToBusinessResponse.toResponse(
                        business, hoursByBusinessId.getOrDefault(business.getId(), List.of())))
                .toList();
    }

    @Override
    public BusinessResponse findBySlug(String slug) {
        Business business = businessRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.BUSINESS_NOT_FOUND, slug));
        List<BusinessHours> hours = businessHoursRepository.findByBusinessId(business.getId());
        return businessEntityToBusinessResponse.toResponse(business, hours);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConfig.BUSINESSES_CACHE, allEntries = true)
    public BusinessResponse create(Long ownerId, CreateBusinessRequest request) {
        if (businessRepository.existsByOwnerId(ownerId)) {
            throw new ConflictException(ErrorMessageKeys.OWNER_HAS_BUSINESS);
        }
        if (businessRepository.existsBySlug(request.getSlug())) {
            throw new ConflictException(ErrorMessageKeys.BUSINESS_SLUG_TAKEN, request.getSlug());
        }
        Business business = Business.builder()
                .owner(userRepository.getReferenceById(ownerId))
                .slug(request.getSlug())
                .name(request.getName())
                .description(request.getDescription())
                .timezone(request.getTimezone())
                .address(request.getAddress())
                .city(request.getCity())
                .phone(PhoneNumbers.normalize(request.getPhone()))
                .email(request.getEmail())
                .build();
        Business savedBusiness = businessRepository.save(business);

        List<BusinessHours> hours = request.getHours().stream()
                .map(hoursRequest -> toBusinessHours(savedBusiness, hoursRequest))
                .toList();
        List<BusinessHours> savedHours = businessHoursRepository.saveAll(hours);

        return businessEntityToBusinessResponse.toResponse(savedBusiness, savedHours);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConfig.BUSINESSES_CACHE, allEntries = true)
    public BusinessResponse update(Long ownerId, UpdateBusinessRequest request) {
        Business business = businessRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.OWNER_HAS_NO_BUSINESS));
        business.update(request.getName(), request.getDescription(), request.getTimezone(),
                request.getAddress(), request.getCity(), PhoneNumbers.normalize(request.getPhone()), request.getEmail());

        Map<DayOfWeek, BusinessHours> hoursByDay = businessHoursRepository.findByBusinessId(business.getId()).stream()
                .collect(Collectors.toMap(BusinessHours::getDayOfWeek, hours -> hours));
        List<BusinessHours> hours = request.getHours().stream()
                .map(hoursRequest -> updateOrCreateHours(business, hoursByDay.get(hoursRequest.getDay()), hoursRequest))
                .toList();

        return businessEntityToBusinessResponse.toResponse(business, hours);
    }

    private BusinessHours updateOrCreateHours(Business business, BusinessHours existing, BusinessHoursRequest request) {
        if (existing == null) {
            return businessHoursRepository.save(toBusinessHours(business, request));
        }
        existing.update(request.getEnabled(), request.getStart(), request.getEnd());
        return existing;
    }

    private BusinessHours toBusinessHours(Business business, BusinessHoursRequest request) {
        return BusinessHours.builder()
                .business(business)
                .dayOfWeek(request.getDay())
                .enabled(request.getEnabled())
                .startTime(request.getStart())
                .endTime(request.getEnd())
                .build();
    }
}
