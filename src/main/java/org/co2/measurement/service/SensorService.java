package org.co2.measurement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.co2.measurement.dto.SensorMetricResponse;
import org.co2.measurement.dto.SensorRequest;
import org.co2.measurement.model.Sensor;
import org.co2.measurement.model.Status;
import org.co2.measurement.repository.SensorRepository;
import org.co2.measurement.repository.StatusRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;
import static org.co2.measurement.helper.SensorHelper.getLocalDateTime;
import static org.co2.measurement.model.Levels.ALERT;
import static org.co2.measurement.model.Levels.OK;
import static org.co2.measurement.model.Levels.WARN;

/**
 * The service class for handling the business logic for the sensor controller.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class SensorService implements ISensorService {

    private final SensorRepository sensorRepository;

    private final StatusRepository statusRepository;

    /**
     * Create a new sensor record
     *
     * @param uuid    - The unique sensor uuid value.
     * @param request - The {@code SensorRequest} request body
     * @return - Returns {@code ResponseEntity} of type {@code Sensor}
     */
    @Override
    public ResponseEntity<?> saveMeasurement(long uuid, SensorRequest request) {
        Sensor newMeasurement = new Sensor(uuid, request.getCo2(), LocalDateTime.parse(request.getTime()));
        this.sensorRepository.save(newMeasurement);
        insertStatus(uuid, request);
        return new ResponseEntity<>(newMeasurement, HttpStatus.CREATED);
    }

    /**
     * Calculate the sensor metrics for last 30 days
     *
     * @param uuid - The sensor uuid value.
     * @return Returns {@code ResponseEntity} of type {@code SensorMetricResponse}
     */
    @Override
    public ResponseEntity<?> findSensorMetrics(long uuid) {
        LocalDateTime localDateTime = getLocalDateTime();
        List<Sensor> sensorsRecords = this.sensorRepository.findAllByUuidAndTimeBetween(uuid, localDateTime.minusDays(30), localDateTime);
        if (sensorsRecords.size() == 0) {
            return new ResponseEntity<>(new SensorMetricResponse(0, 0), HttpStatus.OK);
        } else {
            long average = (long) sensorsRecords.stream().mapToLong(Sensor::getCo2).average().orElse(0L);
            long max = sensorsRecords.stream().mapToLong(Sensor::getCo2).reduce(Long::max).orElse(0L);
            SensorMetricResponse response = new SensorMetricResponse();
            response.setAvgLast30Days(Math.round(average));
            response.setMaxLast30Days(max);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    /**
     * Calculates the sensor past record and set the status value.
     *
     * @param uuid    - The unique sensor uuid value.
     * @param request - The {@code SensorRequest} request body
     */
    private void insertStatus(long uuid, SensorRequest request) {
        Status existingStatus = this.statusRepository.findByUuid(uuid);
        List<Sensor> existingSensorRecords = this.sensorRepository.findAllByUuidOrderByTimeDesc(uuid);
        if (existingStatus == null) {
            Status status = new Status();
            status.setUuid(uuid);
            checkStatusLevel(uuid, existingSensorRecords, request, status);
            statusRepository.save(status);
        } else {
            checkStatusLevel(uuid, existingSensorRecords, request, existingStatus);
            this.statusRepository.save(existingStatus);
        }
    }


    private boolean isAlertStatus(List<Sensor> sensors) {
        if (sensors.size() >= 3) {
            return sensors.stream().limit(3).allMatch(p -> p.getCo2() >= 2000);
        }
        return false;
    }

    private boolean isOkStatus(List<Sensor> sensors) {
        if (sensors.size() >= 3) {
            return sensors.stream().limit(3).allMatch(p -> p.getCo2() < 2000);
        }
        return false;
    }

    /**
     * Check and set the current status of the sensor
     *
     * @param uuid    -  The unique sensor uuid value.
     * @param sensors - {@code List<Sensor>} of existing sensor records
     * @param request - The {@code SensorRequest} request body
     * @param status  - The {@code Levels} request body
     */
    private void checkStatusLevel(long uuid, List<Sensor> sensors, SensorRequest request, Status status) {
        if (isAlertStatus(sensors)) {
            status.setStatus(ALERT.toString());
        } else if (isOkStatus(sensors)) {
            status.setStatus(OK.toString());
        } else {
            if (request.getCo2() >= 2000) {
                status.setStatus(WARN.toString());
            } else {
                Status alertStatusSensor = this.statusRepository.findByUuid(uuid);
                if ((nonNull(alertStatusSensor))
                        && (!Objects.equals(alertStatusSensor.getStatus(), ALERT.toString()) || request.getCo2() < 2000)) {
                    status.setStatus(OK.toString());
                }
            }
        }
    }
}
