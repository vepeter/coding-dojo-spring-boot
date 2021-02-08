package com.assignment.spring.controller;

import com.assignment.spring.api.openweathermap.WeatherResponse;
import com.assignment.spring.model.WeatherEntity;
import com.assignment.spring.repository.WeatherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WeatherController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeatherRepository weatherRepository;
    
    @Value("${app.api.openweathermap.url}")
    private String apiOpenweathermapUrl;
    
    @Value("${app.api.openweathermap.appid}")
    private String apiOpenweathermapAppId;

    @RequestMapping("/weather")
    public WeatherEntity weather(HttpServletRequest request) {
        String city = request.getParameter("city");
        ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(apiOpenweathermapUrl, WeatherResponse.class, city, apiOpenweathermapAppId);
        return mapper(response.getBody());
    }

    private WeatherEntity mapper(WeatherResponse response) {
        WeatherEntity entity = new WeatherEntity();
        entity.setCity(response.getName());
        entity.setCountry(response.getSys().getCountry());
        entity.setTemperature(response.getMain().getTemp());

        return weatherRepository.save(entity);
    }
}
