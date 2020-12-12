package org.co2.measurement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * The Sensor Request JSON holder class.
 */
@Data
@AllArgsConstructor
public class SensorRequest implements Serializable {

    @NotNull(message = "CO2 field cannot be empty")
    @Min(value = 0, message = "Please provide co2 field value greater than zero")
    private long co2;

    @NotBlank(message = "Time field cannot be empty")
    private String time;
}
