package org.co2.measurement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Carbon Dioxide Measurement Application",
        version = "1.0", description = "Service capable of collecting data from sensors and alert if the CO2 concentrations reach critical levels"))
public class Co2MeasurementApplication {

    public static void main(String[] args) {
        SpringApplication.run(Co2MeasurementApplication.class, args);
    }

}
