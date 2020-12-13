package org.co2.measurement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The Sensor Metrics Response JSON holder class.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorMetricResponse implements Serializable {

    private long maxLast30Days;
    private long avgLast30Days;
}
