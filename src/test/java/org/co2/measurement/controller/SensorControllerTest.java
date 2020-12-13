package org.co2.measurement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.co2.measurement.dto.AlertResponse;
import org.co2.measurement.dto.SensorMetricResponse;
import org.co2.measurement.dto.SensorRequest;
import org.co2.measurement.dto.SensorStatusResponse;
import org.co2.measurement.helper.SensorHelper;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

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
        long uuid = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        mockMvc.perform(post("/api/v1/sensors/{uuid}/measurements/", uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sensorRequest)))
                .andExpect(status().isCreated());

        Sensor sensor = sensorRepository.findByUuid(uuid);
        assertThat(sensor.getUuid()).isEqualTo(uuid);
        assertThat(sensor.getCo2()).isEqualTo(1900);
    }

    @Test
    void testGetSensorStatus() throws Exception {
        long uuid = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        statusRepository.save(new Status(uuid, "ALERT"));
        MvcResult result = mockMvc.perform(get("/api/v1/sensors/{uuid}/", uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        SensorStatusResponse sensorStatusResponse = objectMapper.readValue(content, SensorStatusResponse.class);
        assertThat(sensorStatusResponse.getStatus()).isEqualTo("ALERT");
    }

    @Test
    void testFindSensorMetrics() throws Exception {
        long uuid = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        long[] sensorRecords = {2001, 2003, 2004};
        for (long sensorRecord : sensorRecords) {
            sensorRepository.save(new Sensor(uuid, sensorRecord, SensorHelper.getLocalDateTime()));
            Thread.sleep(1000);
        }

        MvcResult result = mockMvc.perform(get("/api/v1/sensors/{uuid}/metrics", uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        SensorMetricResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), SensorMetricResponse.class);
        assertThat(response.getMaxLast30Days()).isEqualTo(2004);
    }

    @Test
    void testFindSensorAlerts() throws Exception {
        long uuid = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        long[] sensorRecords = {2001, 2003, 2004, 1900, 1901, 1902, 2000, 2030, 2005};
        for (long sensorRecord : sensorRecords) {
            sensorRepository.save(new Sensor(uuid, sensorRecord, SensorHelper.getLocalDateTime()));
            Thread.sleep(1000);
        }

        MvcResult result = mockMvc.perform(get("/api/v1/sensors/{uuid}/alerts", uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        AlertResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), AlertResponse.class);
        assertThat(response.getResponse().size()).isEqualTo(2);
    }
}

