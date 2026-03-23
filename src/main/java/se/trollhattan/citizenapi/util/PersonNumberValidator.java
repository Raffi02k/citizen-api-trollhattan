package se.trollhattan.citizenapi.util;

import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class PersonNumberValidator {

    public boolean isValid(String personNumber) {
        if (personNumber == null)
            return false;

        // Step 1: Normalize and Check Length/Digits
        String cleaned = personNumber.replaceAll("\\D", "");
        if (cleaned.length() != 10 && cleaned.length() != 12) {
            return false;
        }

        // Step 2: Validate the Date
        if (!isValidDate(personNumber, cleaned)) {
            return false;
        }

        // Step 3: Calculate and verify Luhn checksum (To be implemented)
        return true;
    }

    private boolean isValidDate(String original, String cleaned) {
        String datePart;

        if (cleaned.length() == 12) {
            // Format: YYYYMMDD
            datePart = cleaned.substring(0, 8);
        } else {
            // Format: YYMMDD (Need to determine century)
            String yy = cleaned.substring(0, 2);
            String mmdd = cleaned.substring(2, 6);

            // If the separator is '+', the person is 100+ years old
            int century = original.contains("+") ? 1800 : 1900;

            // Note: This is a simplified century logic. In a production
            // citizen API, you'd compare against the current year.
            datePart = (century + Integer.parseInt(yy)) + mmdd;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate parsedDate = LocalDate.parse(datePart, formatter);

            // Check if the date is in the future (optional but recommended)
            return !parsedDate.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}