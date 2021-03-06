package com.assignment.spring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.assignment.spring.application.dataloader.openweathermap.model.WeatherResponse;
import com.assignment.spring.application.model.Weather;
import com.assignment.spring.application.repository.WeatherRepository;

class OpenWeatherMapIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ServletWebServerApplicationContext webServerApplicationContext;
    
    @Autowired
    private WeatherRepository weatherRepository;
    
    @Value("${api.openweathermap.url}")
    private String apiOpenweathermapUrl;
    
    @Value("${api.openweathermap.appid}")
    private String apiOpenweathermapAppId;

    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    private String cityDefinition = "Amsterdam,nl";

    @Test
    void applicationShouldLoadAndSaveExpectedTemperatureForTheCity() {
        Double expectedTemperature = loadExpectedTemperature();
        String applicationUrl = String.format("http://localhost:%s/weather?city=%s",
                webServerApplicationContext.getWebServer().getPort(), cityDefinition);
        
        ResponseEntity<Weather> response = testRestTemplate.getForEntity(applicationUrl, Weather.class);
        
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
        ResponseEntity<WeatherResponse> apiResponse = testRestTemplate.getForEntity(apiOpenweathermapUrl,
                WeatherResponse.class, cityDefinition, apiOpenweathermapAppId);
        return apiResponse.getBody().getMain().getTemp();
    }
}