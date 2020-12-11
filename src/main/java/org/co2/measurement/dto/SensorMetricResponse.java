package org.co2.measurement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorMetricResponse {

    private long maxLast30Days;
    private long avgLast30Days;
}
