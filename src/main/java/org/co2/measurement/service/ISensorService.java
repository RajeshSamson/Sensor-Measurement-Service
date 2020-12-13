package org.co2.measurement.service;

import org.co2.measurement.dto.SensorRequest;
import org.springframework.http.ResponseEntity;

public interface ISensorService {

    ResponseEntity<?> saveMeasurement(long uuid, SensorRequest request);

    ResponseEntity<?> findSensorMetrics(long uuid);
}
