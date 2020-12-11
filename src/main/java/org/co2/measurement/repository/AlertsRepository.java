package org.co2.measurement.repository;

import org.co2.measurement.model.Alerts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertsRepository extends JpaRepository<Alerts, Long> {

    /**
     * Find all the Alerts of a specific unique sensor.
     *
     * @param uuid - The sensor unique identifier
     * @return - Returns {@code List<Alerts>}
     */
    List<Alerts> findAllByUuid(long uuid);
}
