package org.fastappointment.service.impl;

import org.fastappointment.api.dto.SlotResponse;
import org.fastappointment.api.dto.SlotSearchRequest;
import org.fastappointment.api.dto.SlotStatus;
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
import org.fastappointment.repository.CustomerRepository;
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

    private static final int SLOT_INTERVAL_MINUTES = 30;

    private final BusinessRepository businessRepository;
    private final ServiceOfferingRepository serviceOfferingRepository;
    private final BusinessHoursRepository businessHoursRepository;
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;

    public SlotServiceImpl(BusinessRepository businessRepository, ServiceOfferingRepository serviceOfferingRepository,
                           BusinessHoursRepository businessHoursRepository, BookingRepository bookingRepository,
                           CustomerRepository customerRepository) {
        this.businessRepository = businessRepository;
        this.serviceOfferingRepository = serviceOfferingRepository;
        this.businessHoursRepository = businessHoursRepository;
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<SlotResponse> findSlots(String slug, Long serviceId, SlotSearchRequest request, Long customerUserId) {
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

        LocalDateTime rangeStart = request.getFrom().atStartOfDay();
        LocalDateTime rangeEnd = request.getTo().plusDays(1).atStartOfDay();
        Map<DayOfWeek, BusinessHours> hoursByDay = businessHoursRepository.findByBusinessId(business.getId()).stream()
                .collect(Collectors.toMap(BusinessHours::getDayOfWeek, Function.identity()));
        List<Booking> staffBookings = bookingRepository.findActiveForStaffBetween(
                staff.stream().map(Staff::getId).toList(), rangeStart, rangeEnd, BookingStatus.CANCELLED);
        List<Booking> customerBookings = findCustomerBookings(customerUserId, rangeStart, rangeEnd);
        LocalDateTime now = LocalDateTime.now(ZoneId.of(business.getTimezone()));

        List<SlotResponse> slots = new ArrayList<>();
        for (LocalDate date = request.getFrom(); !date.isAfter(request.getTo()); date = date.plusDays(1)) {
            BusinessHours hours = hoursByDay.get(date.getDayOfWeek());
            if (hours == null || !Boolean.TRUE.equals(hours.getEnabled())) {
                continue;
            }
            addSlotsForDay(slots, date, hours, service.getDurationMinutes(), staff, staffBookings, customerBookings, now);
        }
        return slots;
    }

    // Only a logged-in customer's own bookings, at any business: they can't be in two places at once.
    private List<Booking> findCustomerBookings(Long customerUserId, LocalDateTime from, LocalDateTime to) {
        if (customerUserId == null) {
            return List.of();
        }
        return customerRepository.findByUserId(customerUserId)
                .map(customer -> bookingRepository.findActiveForCustomerBetween(
                        customer.getId(), from, to, BookingStatus.CANCELLED))
                .orElse(List.of());
    }

    private void addSlotsForDay(List<SlotResponse> slots, LocalDate date, BusinessHours hours, int durationMinutes,
                                List<Staff> staff, List<Booking> staffBookings, List<Booking> customerBookings,
                                LocalDateTime now) {
        LocalDateTime closing = date.atTime(hours.getEndTime());
        for (LocalDateTime start = date.atTime(hours.getStartTime());
             !start.plusMinutes(durationMinutes).isAfter(closing);
             start = start.plusMinutes(SLOT_INTERVAL_MINUTES)) {
            if (!start.isAfter(now)) {
                continue;
            }
            LocalDateTime end = start.plusMinutes(durationMinutes);
            slots.add(toSlot(date, start, end, staff, staffBookings, customerBookings));
        }
    }

    private SlotResponse toSlot(LocalDate date, LocalDateTime start, LocalDateTime end, List<Staff> staff,
                                List<Booking> staffBookings, List<Booking> customerBookings) {
        if (customerBookings.stream().anyMatch(booking -> overlaps(booking, start, end))) {
            return new SlotResponse(date, start.toLocalTime(), null, SlotStatus.CUSTOMER_BUSY);
        }
        return findFreeStaff(staff, staffBookings, start, end)
                .map(member -> new SlotResponse(date, start.toLocalTime(), member.getId(), SlotStatus.AVAILABLE))
                .orElseGet(() -> new SlotResponse(date, start.toLocalTime(), null, SlotStatus.BOOKED));
    }

    private Optional<Staff> findFreeStaff(List<Staff> staff, List<Booking> bookings,
                                          LocalDateTime start, LocalDateTime end) {
        return staff.stream()
                .filter(member -> bookings.stream().noneMatch(booking ->
                        booking.getStaff().getId().equals(member.getId()) && overlaps(booking, start, end)))
                .findFirst();
    }

    private boolean overlaps(Booking booking, LocalDateTime start, LocalDateTime end) {
        return booking.getStartsAt().isBefore(end) && booking.getEndsAt().isAfter(start);
    }
}
