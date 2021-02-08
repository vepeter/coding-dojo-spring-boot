package com.assignment.spring.application.service;

import org.springframework.stereotype.Service;

import com.assignment.spring.application.dataloader.WeatherDataLoader;
import com.assignment.spring.application.model.Weather;
import com.assignment.spring.application.repository.WeatherRepository;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;

    private final WeatherDataLoader weatherDataLoader;

    public WeatherServiceImpl(WeatherRepository weatherRepository, WeatherDataLoader weatherDataLoader) {
        super();
        this.weatherRepository = weatherRepository;
        this.weatherDataLoader = weatherDataLoader;
    }

    @Override
    public Weather registerWeather(String city) {
        Weather weather = weatherDataLoader.load(city);
        return weatherRepository.save(weather);
    }

}
