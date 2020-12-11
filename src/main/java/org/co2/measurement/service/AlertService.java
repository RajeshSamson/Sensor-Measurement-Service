package org.co2.measurement.service;

import lombok.RequiredArgsConstructor;
import org.co2.measurement.dto.AlertResponse;
import org.co2.measurement.model.Alerts;
import org.co2.measurement.model.Sensor;
import org.co2.measurement.repository.AlertsRepository;
import org.co2.measurement.repository.SensorRepository;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertsRepository alertsRepository;

    private final SensorRepository sensorRepository;

    private final ReloadableResourceBundleMessageSource messageSource;

    public ResponseEntity<?> getAlertsStatus(long uuid) {
        generateAlertsReport(uuid);
        List<Alerts> alerts = alertsRepository.findAllByUuid(uuid);
        if (alerts == null) {
            return new ResponseEntity<>(messageSource.getMessage("error.alert.status",
                    new Object[]{String.valueOf(uuid)}, getLocale()), HttpStatus.BAD_REQUEST);
        } else {
            AlertResponse response = new AlertResponse(alerts);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    private void generateAlertsReport(long uuid) {
        List<Sensor> sensors = sensorRepository.findAllByUuidOrderByTimeDesc(uuid);
        if (sensors.size() > 0) {
            final int rangeSize = 3;
            final int group = (sensors.size() + rangeSize - 1) / rangeSize;
            List<List<Sensor>> result = IntStream.range(0, group)
                    .mapToObj(i -> sensors.subList(3 * i, Math.min(3 * i + 3, sensors.size())))
                    .collect(Collectors.toList());

            for (List<Sensor> items : result) {
                boolean isAlert = items.stream().allMatch(p -> p.getCo2() >= 2000);
                if (isAlert) {
                    Alerts alerts = new Alerts();
                    alerts.setUuid(uuid);
                    alerts.setStartTime(items.get(items.size() - 1).getTime());
                    alerts.setEndTime(items.get(0).getTime());
                    alertsRepository.save(alerts);
                }
            }
        }
    }
}
