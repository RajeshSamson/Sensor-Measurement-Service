package org.co2.measurement.repository;


import org.co2.measurement.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {

    /**
     * Retrieves the {@code Status} model with the specific unique identifier.
     *
     * @param uuid - The sensor unique indentifier
     * @return - Returns {@code Status}
     */
    Status findByUuid(long uuid);
}
