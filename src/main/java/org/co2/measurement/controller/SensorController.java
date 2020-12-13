package org.co2.measurement.controller;

import lombok.RequiredArgsConstructor;
import org.co2.measurement.dto.SensorRequest;
import org.co2.measurement.helper.SensorHelper;
import org.co2.measurement.service.AlertService;
import org.co2.measurement.service.SensorService;
import org.co2.measurement.service.StatusService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * This is the controller class which exposes the Application Rest endpoint.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    private final StatusService statusService;

    private final AlertService alertService;

    private final ReloadableResourceBundleMessageSource messageSource;

    /**
     * This method is used to capture the sensor measurements
     *
     * @param uuid    - The sensor unique id.
     * @param request - The Request body
     * @return - Returns {@code ResponseEntity<Sensor>}
     */
    @PostMapping("/sensors/{uuid}/measurements")
    public ResponseEntity<?> saveSensorMeasurements(@PathVariable("uuid") long uuid,
                                                    @Valid @RequestBody SensorRequest request) {
        boolean isValid = SensorHelper.validateDateFormat(request.getTime());
        if (!isValid) {
            throw new IllegalArgumentException(messageSource.getMessage("error.invalid.date", null, LocaleContextHolder.getLocale()));
        }
        return this.sensorService.saveMeasurement(uuid, request);
    }

    /**
     * This method is used to find the current status of the provided sensor unique identifier
     *
     * @param uuid - The sensor unique id.
     * @return - Returns {@code ResponseEntity<SensorStatusResponse>}
     */
    @GetMapping("/sensors/{uuid}")
    public ResponseEntity<?> getSensorStatus(@PathVariable("uuid") long uuid) {
        return this.statusService.getSensorStatus(uuid);
    }

    /**
     * This method is used to find the metrics of the given sensor unique identifier
     *
     * @param uuid - The sensor unique id.
     * @return - Returns {@code ResponseEntity<SensorMetricResponse>}
     */
    @GetMapping("/sensors/{uuid}/metrics")
    public ResponseEntity<?> findSensorMetrics(@PathVariable("uuid") long uuid) {
        return this.sensorService.findSensorMetrics(uuid);
    }

    /**
     * This method is retrieves all the alerts associated with sensor unique identifier
     *
     * @param uuid - The sensor unique id.
     * @return - Returns {@code ResponseEntity<AlertResponse>}
     */
    @GetMapping("/sensors/{uuid}/alerts")
    public ResponseEntity<?> findSensorAlerts(@PathVariable("uuid") long uuid) {
        return this.alertService.getAlertsStatus(uuid);
    }
}
