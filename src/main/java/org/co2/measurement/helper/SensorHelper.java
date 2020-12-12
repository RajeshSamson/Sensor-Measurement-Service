package org.co2.measurement.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The helper class for the sensor measurement application.
 */
public class SensorHelper {

    /**
     * Method to convert the given date {@code String} to {@code LocalDateTime}
     * @return
     */
    public static LocalDateTime getLocalDateTime() {
        String formattedCurrentDate = getTime();
        return LocalDateTime.parse(formattedCurrentDate);
    }


    public static String getTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }
}
