package org.co2.measurement.helper;

import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
public class SensorHelper {

    public static LocalDateTime getLocalDateTime() {
        String formattedCurrentDate = getTime();
        return LocalDateTime.parse(formattedCurrentDate);
    }


    public static String getTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }
}
