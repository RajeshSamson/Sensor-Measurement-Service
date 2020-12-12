package org.co2.measurement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Sensor Metrics Response JSON holder class.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorMetricResponse {

    private long maxLast30Days;
    private long avgLast30Days;
}
