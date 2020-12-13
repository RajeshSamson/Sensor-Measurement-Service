package org.co2.measurement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.co2.measurement.model.Alerts;

import java.io.Serializable;
import java.util.List;

/**
 * The Alerts Response JSON holder class.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertResponse implements Serializable {

    List<Alerts> response;

}
