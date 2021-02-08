package com.assignment.spring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.assignment.spring.api.openweathermap.WeatherResponse;
import com.assignment.spring.model.WeatherEntity;
import com.assignment.spring.repository.WeatherRepository;

class ApplicationIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ServletWebServerApplicationContext webServerApplicationContext;
    
    @Autowired
    private WeatherRepository weatherRepository;

    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    private String cityDefinition = "Amsterdam,nl";

    @Test
    void applicationShouldLoadAndSaveExpectedTemperatureForTheCity() {
        Double expectedTemperature = loadExpectedTemperature();
        String applicationUrl = String.format("http://localhost:%s/weather?city=%s",
                webServerApplicationContext.getWebServer().getPort(), cityDefinition);
        
        ResponseEntity<WeatherEntity> response = testRestTemplate.getForEntity(applicationUrl, WeatherEntity.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .extracting("city", "country", "temperature")
                .containsExactly("Amsterdam", "NL", expectedTemperature);
        
        assertThat(weatherRepository.findAll()).hasSize(1);
        assertThat(weatherRepository.findById(1).get())
                .extracting("city", "country", "temperature")
                .containsExactly("Amsterdam", "NL", expectedTemperature);
    }

    private Double loadExpectedTemperature() {
        String apiUrl = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=%s",
                cityDefinition, Constants.APP_ID);
        ResponseEntity<WeatherResponse> apiResponse = testRestTemplate.getForEntity(apiUrl, WeatherResponse.class);
        return apiResponse.getBody().getMain().getTemp();
    }
}