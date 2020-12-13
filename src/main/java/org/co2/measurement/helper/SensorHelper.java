package org.co2.measurement.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * The helper class for the sensor measurement application.
 */
public class SensorHelper {

    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Method to convert the given date {@code String} to {@code LocalDateTime}
     *
     * @return - Returns {@code LocalDateTime}
     */
    public static LocalDateTime getLocalDateTime() {
        String formattedCurrentDate = getTime();
        return LocalDateTime.parse(formattedCurrentDate);
    }


    public static String getTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        return currentDateTime.format(DateTimeFormatter.ofPattern(PATTERN));
    }

    public static boolean validateDateFormat(String time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN);
        try {
            LocalDate.parse(time, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
