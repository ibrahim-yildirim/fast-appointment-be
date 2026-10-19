package org.fastappointment.exception;

public final class ErrorMessageKeys {

    public static final String BUSINESS_NOT_FOUND = "error.business.notFound";
    public static final String BUSINESS_SLUG_TAKEN = "error.business.slugTaken";
    public static final String SERVICE_NOT_FOUND = "error.service.notFound";
    public static final String STAFF_NOT_FOUND = "error.staff.notFound";
    public static final String CUSTOMER_NOT_FOUND = "error.customer.notFound";
    public static final String STAFF_NOT_AVAILABLE = "error.booking.staffNotAvailable";
    public static final String OUTSIDE_OPENING_HOURS = "error.booking.outsideOpeningHours";
    public static final String VALIDATION_FAILED = "error.validation.failed";
    public static final String MALFORMED_REQUEST = "error.request.malformed";

    private ErrorMessageKeys() {
    }
}
