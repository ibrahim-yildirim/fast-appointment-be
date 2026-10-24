package org.fastappointment.service.impl;

import org.fastappointment.api.dto.BookingResponse;
import org.fastappointment.api.dto.CancelBookingRequest;
import org.fastappointment.api.dto.CreateBookingRequest;
import org.fastappointment.api.dto.CreateBusinessBookingRequest;
import org.fastappointment.api.dto.CustomerBookingResponse;
import org.fastappointment.api.dto.UpdateBookingRequest;
import org.fastappointment.entity.concretes.Booking;
import org.fastappointment.entity.enums.BookingStatus;
import org.fastappointment.entity.enums.CancelledBy;
import org.fastappointment.entity.concretes.Business;
import org.fastappointment.entity.concretes.BusinessHours;
import org.fastappointment.entity.concretes.Customer;
import org.fastappointment.entity.concretes.ServiceOffering;
import org.fastappointment.entity.concretes.Staff;
import org.fastappointment.exception.ConflictException;
import org.fastappointment.exception.ErrorMessageKeys;
import org.fastappointment.exception.NotFoundException;
import org.fastappointment.mapper.BookingEntityToBookingResponse;
import org.fastappointment.mapper.BookingEntityToCustomerBookingResponse;
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
import java.time.ZoneId;
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
    private final BookingEntityToCustomerBookingResponse bookingEntityToCustomerBookingResponse;

    public BookingServiceImpl(BookingRepository bookingRepository, BusinessRepository businessRepository,
                              ServiceOfferingRepository serviceOfferingRepository, StaffRepository staffRepository,
                              CustomerRepository customerRepository, BusinessHoursRepository businessHoursRepository,
                              BookingEntityToBookingResponse bookingEntityToBookingResponse,
                              BookingEntityToCustomerBookingResponse bookingEntityToCustomerBookingResponse) {
        this.bookingRepository = bookingRepository;
        this.businessRepository = businessRepository;
        this.serviceOfferingRepository = serviceOfferingRepository;
        this.staffRepository = staffRepository;
        this.customerRepository = customerRepository;
        this.businessHoursRepository = businessHoursRepository;
        this.bookingEntityToBookingResponse = bookingEntityToBookingResponse;
        this.bookingEntityToCustomerBookingResponse = bookingEntityToCustomerBookingResponse;
    }

    @Override
    public List<CustomerBookingResponse> findAllForCustomer(Long userId) {
        Customer customer = findCustomer(userId);
        return bookingRepository.findByCustomerIdOrderByStartsAtAsc(customer.getId()).stream()
                .map(bookingEntityToCustomerBookingResponse::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CustomerBookingResponse createForCustomer(Long userId, CreateBookingRequest request) {
        Customer customer = findCustomer(userId);
        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.BUSINESS_NOT_FOUND, request.getBusinessId()));
        ServiceOffering service = findService(business, request.getServiceId());
        Staff staff = findStaff(business, request.getStaffId());
        checkStaffOffersService(staff, service);
        LocalDateTime endsAt = request.getStartsAt().plusMinutes(service.getDurationMinutes());
        checkSlotIsFree(business, staff, request.getStartsAt(), endsAt);
        checkCustomerIsFree(customer, request.getStartsAt(), endsAt);

        Booking booking = Booking.builder()
                .business(business)
                .service(service)
                .staff(staff)
                .customer(customer)
                .startsAt(request.getStartsAt())
                .endsAt(endsAt)
                .status(BookingStatus.CONFIRMED)
                .notes(request.getNotes())
                .build();
        return bookingEntityToCustomerBookingResponse.toResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public CustomerBookingResponse updateForCustomer(Long userId, Long bookingId, UpdateBookingRequest request) {
        Customer customer = findCustomer(userId);
        Booking booking = bookingRepository.findByIdAndCustomerId(bookingId, customer.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.BOOKING_NOT_FOUND, bookingId));
        return bookingEntityToCustomerBookingResponse.toResponse(reschedule(booking, request));
    }

    @Override
    @Transactional
    public void cancelForCustomer(Long userId, Long bookingId, CancelBookingRequest request) {
        Customer customer = findCustomer(userId);
        Booking booking = bookingRepository.findByIdAndCustomerId(bookingId, customer.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.BOOKING_NOT_FOUND, bookingId));
        booking.cancel(CancelledBy.CUSTOMER, cancellationReason(request));
    }

    @Override
    public List<BookingResponse> findAllForOwner(Long ownerId) {
        Business business = findOwnerBusiness(ownerId);
        return bookingRepository.findByBusinessId(business.getId()).stream()
                .map(bookingEntityToBookingResponse::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public BookingResponse createForOwner(Long ownerId, CreateBusinessBookingRequest request) {
        Business business = findOwnerBusiness(ownerId);
        ServiceOffering service = findService(business, request.getServiceId());
        Staff staff = findStaff(business, request.getStaffId());
        checkStaffOffersService(staff, service);
        checkSlotIsFree(business, staff, request.getStartsAt(), request.getEndsAt());
        Customer customer = findOrCreateBusinessCustomer(business, request);
        checkCustomerIsFree(customer, request.getStartsAt(), request.getEndsAt());

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
    public BookingResponse updateForOwner(Long ownerId, Long bookingId, UpdateBookingRequest request) {
        Business business = findOwnerBusiness(ownerId);
        Booking booking = bookingRepository.findByIdAndBusinessId(bookingId, business.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.BOOKING_NOT_FOUND, bookingId));
        return bookingEntityToBookingResponse.toResponse(reschedule(booking, request));
    }

    @Override
    @Transactional
    public void cancelForOwner(Long ownerId, Long bookingId, CancelBookingRequest request) {
        Business business = findOwnerBusiness(ownerId);
        Booking booking = bookingRepository.findByIdAndBusinessId(bookingId, business.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.BOOKING_NOT_FOUND, bookingId));
        booking.cancel(CancelledBy.BUSINESS, cancellationReason(request));
    }

    @Override
    @Transactional
    public BookingResponse markNoShowForOwner(Long ownerId, Long bookingId) {
        Business business = findOwnerBusiness(ownerId);
        Booking booking = bookingRepository.findByIdAndBusinessId(bookingId, business.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.BOOKING_NOT_FOUND, bookingId));
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new ConflictException(ErrorMessageKeys.NO_SHOW_NOT_CONFIRMED);
        }
        LocalDateTime now = LocalDateTime.now(ZoneId.of(business.getTimezone()));
        if (booking.getStartsAt().isAfter(now)) {
            throw new ConflictException(ErrorMessageKeys.NO_SHOW_TOO_EARLY);
        }
        booking.markNoShow();
        return bookingEntityToBookingResponse.toResponse(booking);
    }

    private String cancellationReason(CancelBookingRequest request) {
        if (request == null || request.getReason() == null || request.getReason().isBlank()) {
            return null;
        }
        return request.getReason().trim();
    }

    private Customer findCustomer(Long userId) {
        return customerRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.CUSTOMER_NOT_FOUND, userId));
    }

    private Business findOwnerBusiness(Long ownerId) {
        return businessRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.OWNER_HAS_NO_BUSINESS));
    }

    private Customer findOrCreateBusinessCustomer(Business business, CreateBusinessBookingRequest request) {
        String phone = normalizePhone(request.getCustomerPhone());
        return customerRepository.findByBusinessIdAndPhone(business.getId(), phone)
                .orElseGet(() -> customerRepository.save(Customer.builder()
                        .business(business)
                        .name(request.getCustomerName())
                        .phone(phone)
                        .email(request.getCustomerEmail())
                        .build()));
    }

    private String normalizePhone(String phone) {
        return phone.replaceAll("[^0-9+]", "");
    }

    private ServiceOffering findService(Business business, Long serviceId) {
        return serviceOfferingRepository.findByIdAndBusinessIdAndActiveTrue(serviceId, business.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.SERVICE_NOT_FOUND, serviceId));
    }

    private Staff findStaff(Business business, Long staffId) {
        return staffRepository.findByIdAndBusinessIdAndActiveTrue(staffId, business.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.STAFF_NOT_FOUND, staffId));
    }

    private Booking reschedule(Booking booking, UpdateBookingRequest request) {
        Business business = booking.getBusiness();
        ServiceOffering service = findService(business, request.getServiceId());
        Staff staff = findStaff(business, request.getStaffId());
        checkStaffOffersService(staff, service);
        checkOpeningHours(business, request.getStartsAt(), request.getEndsAt());
        boolean staffBusy = bookingRepository.existsOverlappingBookingExcluding(
                booking.getId(), staff.getId(), request.getStartsAt(), request.getEndsAt(), BookingStatus.CANCELLED);
        if (staffBusy) {
            throw new ConflictException(ErrorMessageKeys.STAFF_NOT_AVAILABLE, staff.getName());
        }
        Customer customer = booking.getCustomer();
        boolean customerBusy = customer != null && bookingRepository.existsOverlappingCustomerBookingExcluding(
                booking.getId(), customer.getId(), request.getStartsAt(), request.getEndsAt(), BookingStatus.CANCELLED);
        if (customerBusy) {
            throw new ConflictException(ErrorMessageKeys.CUSTOMER_NOT_AVAILABLE, customer.getName());
        }
        booking.reschedule(service, staff, request.getStartsAt(), request.getEndsAt(), request.getNotes());
        return booking;
    }

    private void checkStaffOffersService(Staff staff, ServiceOffering service) {
        if (!service.getEligibleStaff().contains(staff)) {
            throw new ConflictException(ErrorMessageKeys.STAFF_NOT_ELIGIBLE, staff.getName(), service.getName());
        }
    }

    private void checkCustomerIsFree(Customer customer, LocalDateTime startsAt, LocalDateTime endsAt) {
        boolean customerBusy = bookingRepository.existsOverlappingCustomerBooking(
                customer.getId(), startsAt, endsAt, BookingStatus.CANCELLED);
        if (customerBusy) {
            throw new ConflictException(ErrorMessageKeys.CUSTOMER_NOT_AVAILABLE, customer.getName());
        }
    }

    private void checkSlotIsFree(Business business, Staff staff, LocalDateTime startsAt, LocalDateTime endsAt) {
        checkOpeningHours(business, startsAt, endsAt);
        boolean staffBusy = bookingRepository.existsOverlappingBooking(
                staff.getId(), startsAt, endsAt, BookingStatus.CANCELLED);
        if (staffBusy) {
            throw new ConflictException(ErrorMessageKeys.STAFF_NOT_AVAILABLE, staff.getName());
        }
    }

    private void checkOpeningHours(Business business, LocalDateTime startsAt, LocalDateTime endsAt) {
        if (!isWithinOpeningHours(business.getId(), startsAt, endsAt)) {
            throw new ConflictException(ErrorMessageKeys.OUTSIDE_OPENING_HOURS);
        }
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
