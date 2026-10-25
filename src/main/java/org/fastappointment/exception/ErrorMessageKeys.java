package org.fastappointment.exception;

public final class ErrorMessageKeys {

    public static final String BUSINESS_NOT_FOUND = "error.business.notFound";
    public static final String BUSINESS_SLUG_TAKEN = "error.business.slugTaken";
    public static final String OWNER_HAS_BUSINESS = "error.business.ownerHasBusiness";
    public static final String OWNER_HAS_NO_BUSINESS = "error.business.ownerHasNoBusiness";
    public static final String SERVICE_NOT_FOUND = "error.service.notFound";
    public static final String STAFF_NOT_FOUND = "error.staff.notFound";
    public static final String CUSTOMER_NOT_FOUND = "error.customer.notFound";
    public static final String CUSTOMER_PHONE_TAKEN = "error.customer.phoneTaken";
    public static final String BOOKING_NOT_FOUND = "error.booking.notFound";
    public static final String STAFF_NOT_ELIGIBLE = "error.booking.staffNotEligible";
    public static final String CUSTOMER_NOT_AVAILABLE = "error.booking.customerNotAvailable";
    public static final String STAFF_NOT_AVAILABLE = "error.booking.staffNotAvailable";
    public static final String OUTSIDE_OPENING_HOURS = "error.booking.outsideOpeningHours";
    public static final String NO_SHOW_NOT_CONFIRMED = "error.booking.noShowNotConfirmed";
    public static final String NO_SHOW_TOO_EARLY = "error.booking.noShowTooEarly";
    public static final String EMAIL_TAKEN = "error.auth.emailTaken";
    public static final String INVALID_CREDENTIALS = "error.auth.invalidCredentials";
    public static final String INVALID_REFRESH_TOKEN = "error.auth.invalidRefreshToken";
    public static final String VALIDATION_FAILED = "error.validation.failed";
    public static final String MALFORMED_REQUEST = "error.request.malformed";

    private ErrorMessageKeys() {
    }
}
