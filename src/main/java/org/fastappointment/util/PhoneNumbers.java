package org.fastappointment.util;

public final class PhoneNumbers {

    public static final String MOBILE_PATTERN = "5\\d{9}";
    public static final String BUSINESS_PATTERN = "[2-5]\\d{9}";

    private PhoneNumbers() {
    }

    public static String normalize(String phone) {
        String digits = phone.replaceAll("[^0-9]", "");
        if (digits.startsWith("00")) {
            digits = digits.substring(2);
        }
        if (digits.startsWith("90") && digits.length() == 12) {
            return "0" + digits.substring(2);
        }
        if (digits.length() == 10) {
            return "0" + digits;
        }
        return digits;
    }

    public static String format(String phone) {
        String normalized = normalize(phone);
        if (normalized.length() != 11 || !normalized.startsWith("0")) {
            return phone;
        }
        return "+90 " + normalized.substring(1, 4) + " " + normalized.substring(4, 7) + " "
                + normalized.substring(7, 9) + " " + normalized.substring(9);
    }
}
