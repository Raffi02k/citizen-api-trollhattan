package se.trollhattan.citizenapi.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class PersonNumberValidator {

    public boolean isValid(String personNumber) {
        // Step 1: Kontrollera längd och att det bara är siffror
        if (personNumber == null || !personNumber.matches("\\d{12}")) {
            return false;
        }

        // Step 2: Validera datumet (YYYYMMDD)
        if (!hasValidDate(personNumber.substring(0, 8))) {
            return false;
        }

        // Step 3: Beräkna och verifiera Luhn-kontrollsumman
        // Vi skickar med de sista 10 siffrorna (YYMMDDXXXX) enligt svensk standard
        return hasValidLuhnChecksum(personNumber.substring(2));
    }

    private boolean hasValidDate(String yyyyMMdd) {
        try {
            // BASIC_ISO_DATE hanterar formatet yyyyMMdd (t.ex. 19870125)
            LocalDate.parse(yyyyMMdd, DateTimeFormatter.BASIC_ISO_DATE);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    private boolean hasValidLuhnChecksum(String tenDigits) {
        int sum = 0;

        // Vi loopar igenom de första 9 siffrorna (kontrollsiffran är den 10:e)
        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(tenDigits.charAt(i));

            // Multiplicera varannan siffra med 2 (index 0, 2, 4, 6, 8)
            if (i % 2 == 0) {
                digit *= 2;

                // Om resultatet blir tvåsiffrigt (t.ex. 14), addera siffrorna (1+4=5)
                // Inom programmering är "minus 9" ett snabbt sätt att få samma resultat
                if (digit > 9) {
                    digit -= 9;
                }
            }

            // Addera siffran till den totala summan
            sum += digit;
        }

        // Beräkna vad den 10:e siffran (kontrollsiffran) borde vara
        // Vi tar 10 minus sista siffran i summan.
        // Exempel: Om summan är 34, blir det 10 - 4 = 6.
        int expectedChecksum = (10 - (sum % 10)) % 10;

        // Hämta den faktiska kontrollsiffran från personnumret
        int actualChecksum = Character.getNumericValue(tenDigits.charAt(9));

        // Returnera sant om de matchar
        return expectedChecksum == actualChecksum;
    }
}