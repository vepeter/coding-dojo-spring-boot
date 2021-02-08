package com.assignment.spring.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.spring.application.exception.CityNotFoundException;
import com.assignment.spring.application.exception.SystemException;
import com.assignment.spring.application.model.Weather;
import com.assignment.spring.application.service.WeatherService;
import com.assignment.spring.web.converter.WeatherToWeatherResponseConverter;
import com.assignment.spring.web.response.ErrorResponse;
import com.assignment.spring.web.response.WeatherResponse;

@RestController
public class WeatherController {

    private final WeatherService weatherService;
    
    private final WeatherToWeatherResponseConverter converter;

    @Autowired
    public WeatherController(WeatherService weatherService, WeatherToWeatherResponseConverter converter) {
        super();
        this.weatherService = weatherService;
        this.converter = converter;
    }

    @GetMapping("/weather")
    public WeatherResponse weather(@RequestParam("city") String city) {
        Weather result = weatherService.registerWeather(city);
        return converter.weatherToWeatherResponse(result);
    }

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCityNotFoundException(CityNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ErrorResponse> handleSystemException(SystemException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("There is a problem connecting to the service. Please try later."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
