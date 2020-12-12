package org.co2.measurement.service;

import lombok.RequiredArgsConstructor;
import org.co2.measurement.dto.SensorStatusResponse;
import org.co2.measurement.model.Status;
import org.co2.measurement.repository.StatusRepository;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

/**
 * The service class for handling the business logic for the status model.
 */
@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepository statusRepository;

    private final ReloadableResourceBundleMessageSource messageSource;

    /**
     * Retrieve the status for the given sensor unique identifier
     *
     * @param uuid - The sensor unique identifier
     * @return - Returns {@code ResponseEntity<SensorStatusResponse>}
     */
    public ResponseEntity<?> getSensorStatus(long uuid) {
        Status status = statusRepository.findByUuid(uuid);
        if (status == null) {
            return new ResponseEntity<>(messageSource.getMessage("error.sensor.status",
                    new Object[]{String.valueOf(uuid)}, getLocale()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new SensorStatusResponse(status.getStatus()), HttpStatus.OK);
    }
}
