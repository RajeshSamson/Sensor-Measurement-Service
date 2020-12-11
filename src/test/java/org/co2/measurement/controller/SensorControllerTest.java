package org.co2.measurement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.co2.measurement.dto.SensorRequest;
import org.co2.measurement.model.Sensor;
import org.co2.measurement.model.Status;
import org.co2.measurement.repository.SensorRepository;
import org.co2.measurement.repository.StatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SensorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private StatusRepository statusRepository;

    private SensorRequest sensorRequest;

    @BeforeEach
    public void init() {
        sensorRequest = new SensorRequest(1900, "2020-12-11T12:40:00");
    }

    @Test
    void testSaveSensorMeasurements() throws Exception {
        mockMvc.perform(post("/api/v1/sensors/{uuid}/measurements/", 1001)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sensorRequest)))
                .andExpect(status().isCreated());

        Sensor sensor = sensorRepository.findByUuid(1001);
        assertThat(sensor.getUuid()).isEqualTo(1001);
        assertThat(sensor.getCo2()).isEqualTo(1900);
    }

    @Test
    void testGetSensorStatus() throws Exception {
        statusRepository.save(new Status(1001,"OK"));
        mockMvc.perform(get("/api/v1/sensors/{uuid}/", 1001)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

