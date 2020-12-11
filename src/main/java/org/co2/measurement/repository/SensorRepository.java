package org.co2.measurement.repository;

import org.co2.measurement.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

    /**
     * Find all the sensor records in between the given time line
     *
     * @param uuid    - The sensor unique id value.
     * @param start   - The start date value.
     * @param endTime - The end date value.
     * @return - Returns the {@code List<Sensor>} for give time frame period.
     */
    List<Sensor> findAllByUuidAndTimeBetween(long uuid, LocalDateTime start, LocalDateTime endTime);

    /**
     * Find all the sensor order by timestamp in descending order
     *
     * @param uuid - The sensor unique id value.
     * @return - Returns the {@code List<Sensor>}
     */
    List<Sensor> findAllByUuidOrderByTimeDesc(long uuid);

    /**
     * Find sensor by it unique identifier
     *
     * @param uuid - The sensor unique id value.
     * @return - Returns the {@code Sensor}
     */
    Sensor findByUuid(long uuid);
}
