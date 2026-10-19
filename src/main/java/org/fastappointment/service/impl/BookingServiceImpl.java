package org.fastappointment.service.impl;

import org.fastappointment.api.dto.BookingResponse;
import org.fastappointment.api.dto.CreateBookingRequest;
import org.fastappointment.entity.concretes.Booking;
import org.fastappointment.entity.enums.BookingStatus;
import org.fastappointment.entity.concretes.Business;
import org.fastappointment.entity.concretes.BusinessHours;
import org.fastappointment.entity.concretes.Customer;
import org.fastappointment.entity.concretes.ServiceOffering;
import org.fastappointment.entity.concretes.Staff;
import org.fastappointment.exception.ConflictException;
import org.fastappointment.exception.ErrorMessageKeys;
import org.fastappointment.exception.NotFoundException;
import org.fastappointment.mapper.BookingEntityToBookingResponse;
import org.fastappointment.repository.BookingRepository;
import org.fastappointment.repository.BusinessHoursRepository;
import org.fastappointment.repository.BusinessRepository;
import org.fastappointment.repository.CustomerRepository;
import org.fastappointment.repository.ServiceOfferingRepository;
import org.fastappointment.repository.StaffRepository;
import org.fastappointment.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BusinessRepository businessRepository;
    private final ServiceOfferingRepository serviceOfferingRepository;
    private final StaffRepository staffRepository;
    private final CustomerRepository customerRepository;
    private final BusinessHoursRepository businessHoursRepository;
    private final BookingEntityToBookingResponse bookingEntityToBookingResponse;

    public BookingServiceImpl(BookingRepository bookingRepository, BusinessRepository businessRepository,
                              ServiceOfferingRepository serviceOfferingRepository, StaffRepository staffRepository,
                              CustomerRepository customerRepository, BusinessHoursRepository businessHoursRepository,
                              BookingEntityToBookingResponse bookingEntityToBookingResponse) {
        this.bookingRepository = bookingRepository;
        this.businessRepository = businessRepository;
        this.serviceOfferingRepository = serviceOfferingRepository;
        this.staffRepository = staffRepository;
        this.customerRepository = customerRepository;
        this.businessHoursRepository = businessHoursRepository;
        this.bookingEntityToBookingResponse = bookingEntityToBookingResponse;
    }

    @Override
    public List<BookingResponse> findAll() {
        return bookingRepository.findAll().stream()
                .map(bookingEntityToBookingResponse::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public BookingResponse create(CreateBookingRequest request) {
        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.BUSINESS_NOT_FOUND, request.getBusinessId()));
        ServiceOffering service = serviceOfferingRepository.findById(request.getServiceId())
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.SERVICE_NOT_FOUND, request.getServiceId()));
        Staff staff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.STAFF_NOT_FOUND, request.getStaffId()));

        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.CUSTOMER_NOT_FOUND, request.getCustomerId()));
        }

        if (!isWithinOpeningHours(business.getId(), request.getStartsAt(), request.getEndsAt())) {
            throw new ConflictException(ErrorMessageKeys.OUTSIDE_OPENING_HOURS);
        }

        boolean staffBusy = bookingRepository.existsOverlappingBooking(
                staff.getId(), request.getStartsAt(), request.getEndsAt(), BookingStatus.CANCELLED);
        if (staffBusy) {
            throw new ConflictException(ErrorMessageKeys.STAFF_NOT_AVAILABLE, staff.getName());
        }

        Booking booking = Booking.builder()
                .business(business)
                .service(service)
                .staff(staff)
                .customer(customer)
                .startsAt(request.getStartsAt())
                .endsAt(request.getEndsAt())
                .status(BookingStatus.CONFIRMED)
                .notes(request.getNotes())
                .build();
        return bookingEntityToBookingResponse.toResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }

    private boolean isWithinOpeningHours(Long businessId, LocalDateTime startsAt, LocalDateTime endsAt) {
        if (!startsAt.toLocalDate().equals(endsAt.toLocalDate())) {
            return false;
        }
        Optional<BusinessHours> hours = businessHoursRepository.findByBusinessIdAndDayOfWeek(
                businessId, startsAt.getDayOfWeek());
        if (hours.isEmpty() || !Boolean.TRUE.equals(hours.get().getEnabled())) {
            return false;
        }
        LocalTime opening = hours.get().getStartTime();
        LocalTime closing = hours.get().getEndTime();
        return !startsAt.toLocalTime().isBefore(opening) && !endsAt.toLocalTime().isAfter(closing);
    }
}
