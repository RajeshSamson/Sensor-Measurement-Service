package org.co2.measurement.helper;

import lombok.RequiredArgsConstructor;
import org.co2.measurement.dto.SensorRequest;
import org.co2.measurement.service.SensorService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static org.co2.measurement.helper.SensorHelper.getTime;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class ApplicationStartUpHelper implements ApplicationListener<ApplicationReadyEvent> {

    private final SensorService sensorService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        long[] arr = {2001, 2002, 2003, 1900, 1901, 1999, 2001, 2002, 2003};
        for (long l : arr) {
            sensorService.saveMeasurement(1001, new SensorRequest(l, getTime()));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
