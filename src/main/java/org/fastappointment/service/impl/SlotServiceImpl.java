package org.fastappointment.service.impl;

import org.fastappointment.api.dto.SlotResponse;
import org.fastappointment.api.dto.SlotSearchRequest;
import org.fastappointment.entity.concretes.Booking;
import org.fastappointment.entity.concretes.Business;
import org.fastappointment.entity.concretes.BusinessHours;
import org.fastappointment.entity.concretes.ServiceOffering;
import org.fastappointment.entity.concretes.Staff;
import org.fastappointment.entity.enums.BookingStatus;
import org.fastappointment.exception.ErrorMessageKeys;
import org.fastappointment.exception.NotFoundException;
import org.fastappointment.repository.BookingRepository;
import org.fastappointment.repository.BusinessHoursRepository;
import org.fastappointment.repository.BusinessRepository;
import org.fastappointment.repository.ServiceOfferingRepository;
import org.fastappointment.service.SlotService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SlotServiceImpl implements SlotService {

    private static final int SLOT_INTERVAL_MINUTES = 10;

    private final BusinessRepository businessRepository;
    private final ServiceOfferingRepository serviceOfferingRepository;
    private final BusinessHoursRepository businessHoursRepository;
    private final BookingRepository bookingRepository;

    public SlotServiceImpl(BusinessRepository businessRepository, ServiceOfferingRepository serviceOfferingRepository,
                           BusinessHoursRepository businessHoursRepository, BookingRepository bookingRepository) {
        this.businessRepository = businessRepository;
        this.serviceOfferingRepository = serviceOfferingRepository;
        this.businessHoursRepository = businessHoursRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<SlotResponse> findAvailableSlots(String slug, Long serviceId, SlotSearchRequest request) {
        Business business = businessRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.BUSINESS_NOT_FOUND, slug));
        ServiceOffering service = serviceOfferingRepository
                .findWithEligibleStaffByIdAndBusinessIdAndActiveTrue(serviceId, business.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.SERVICE_NOT_FOUND, serviceId));
        List<Staff> staff = service.getEligibleStaff().stream()
                .filter(Staff::getActive)
                .sorted(Comparator.comparing(Staff::getId))
                .toList();
        if (staff.isEmpty()) {
            return List.of();
        }

        Map<DayOfWeek, BusinessHours> hoursByDay = businessHoursRepository.findByBusinessId(business.getId()).stream()
                .collect(Collectors.toMap(BusinessHours::getDayOfWeek, Function.identity()));
        List<Booking> bookings = bookingRepository.findActiveForStaffBetween(
                staff.stream().map(Staff::getId).toList(),
                request.getFrom().atStartOfDay(), request.getTo().plusDays(1).atStartOfDay(), BookingStatus.CANCELLED);
        LocalDateTime now = LocalDateTime.now(ZoneId.of(business.getTimezone()));

        List<SlotResponse> slots = new ArrayList<>();
        for (LocalDate date = request.getFrom(); !date.isAfter(request.getTo()); date = date.plusDays(1)) {
            BusinessHours hours = hoursByDay.get(date.getDayOfWeek());
            if (hours == null || !Boolean.TRUE.equals(hours.getEnabled())) {
                continue;
            }
            addSlotsForDay(slots, date, hours, service.getDurationMinutes(), staff, bookings, now);
        }
        return slots;
    }

    private void addSlotsForDay(List<SlotResponse> slots, LocalDate date, BusinessHours hours, int durationMinutes,
                                List<Staff> staff, List<Booking> bookings, LocalDateTime now) {
        LocalDateTime closing = date.atTime(hours.getEndTime());
        for (LocalDateTime start = date.atTime(hours.getStartTime());
             !start.plusMinutes(durationMinutes).isAfter(closing);
             start = start.plusMinutes(SLOT_INTERVAL_MINUTES)) {
            if (!start.isAfter(now)) {
                continue;
            }
            LocalDateTime slotStart = start;
            findFreeStaff(staff, bookings, slotStart, slotStart.plusMinutes(durationMinutes))
                    .ifPresent(member -> slots.add(new SlotResponse(date, slotStart.toLocalTime(), member.getId())));
        }
    }

    private Optional<Staff> findFreeStaff(List<Staff> staff, List<Booking> bookings,
                                          LocalDateTime start, LocalDateTime end) {
        return staff.stream()
                .filter(member -> bookings.stream().noneMatch(booking ->
                        booking.getStaff().getId().equals(member.getId())
                                && booking.getStartsAt().isBefore(end)
                                && booking.getEndsAt().isAfter(start)))
                .findFirst();
    }
}
