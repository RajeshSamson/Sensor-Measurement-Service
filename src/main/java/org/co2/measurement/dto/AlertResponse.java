package org.co2.measurement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.co2.measurement.model.Alerts;

import java.util.List;

@Data
@AllArgsConstructor
public class AlertResponse {

    List<Alerts> response;

}
