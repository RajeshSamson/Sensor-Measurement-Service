package org.co2.measurement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The Sensor Status Response JSON holder class.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorStatusResponse implements Serializable {

    private String status;
}
