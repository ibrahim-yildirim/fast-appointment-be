package org.fastappointment.util;

public final class PhoneNumbers {

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
        if (digits.startsWith("5") && digits.length() == 10) {
            return "0" + digits;
        }
        return digits;
    }
}
