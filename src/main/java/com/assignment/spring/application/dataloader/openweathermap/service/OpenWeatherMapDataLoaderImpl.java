package com.assignment.spring.application.dataloader.openweathermap.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.assignment.spring.application.dataloader.WeatherDataLoader;
import com.assignment.spring.application.dataloader.openweathermap.converter.ApiWeatherResponseToWeatherConverter;
import com.assignment.spring.application.dataloader.openweathermap.model.WeatherResponse;
import com.assignment.spring.application.exception.CityNotFoundException;
import com.assignment.spring.application.exception.SystemException;
import com.assignment.spring.application.model.Weather;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class OpenWeatherMapDataLoaderImpl implements WeatherDataLoader {

    private final RestTemplate restTemplate;

    private final ApiWeatherResponseToWeatherConverter converter;

    private final String apiOpenweathermapUrl;

    private final String apiOpenweathermapAppId;

    public OpenWeatherMapDataLoaderImpl(RestTemplate restTemplate, ApiWeatherResponseToWeatherConverter converter,
            @Value("${api.openweathermap.url}") String apiOpenweathermapUrl,
            @Value("${api.openweathermap.appid}") String apiOpenweathermapAppId) {
        super();
        this.restTemplate = restTemplate;
        this.converter = converter;
        this.apiOpenweathermapUrl = apiOpenweathermapUrl;
        this.apiOpenweathermapAppId = apiOpenweathermapAppId;
    }

    @HystrixCommand(fallbackMethod = "fallback", ignoreExceptions = CityNotFoundException.class)
    @Override
    public Weather load(String city) {
        try {
            WeatherResponse response = restTemplate.getForObject(apiOpenweathermapUrl, WeatherResponse.class, city,
                    apiOpenweathermapAppId);
            return converter.weatherResponseToWeather(response);
        } catch (HttpClientErrorException e) {
            throw new CityNotFoundException(city);
        }
    }

    public Weather fallback(String city) {
        throw new SystemException("cannot connect to the OpenWeatherMap service");
    }

}
