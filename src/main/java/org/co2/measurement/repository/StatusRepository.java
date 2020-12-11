package org.co2.measurement.repository;


import org.co2.measurement.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {

    Status findByUuid(long uuid);
}
