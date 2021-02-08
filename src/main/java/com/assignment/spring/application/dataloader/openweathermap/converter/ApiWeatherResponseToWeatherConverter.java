package com.assignment.spring.application.dataloader.openweathermap.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.assignment.spring.application.dataloader.openweathermap.model.WeatherResponse;
import com.assignment.spring.application.model.Weather;

@Mapper(componentModel = "spring")
public interface ApiWeatherResponseToWeatherConverter {

    @Mapping(target = "city", source = "name")
    @Mapping(target = "country", source = "sys.country")
    @Mapping(target = "temperature", source = "main.temp")
    Weather weatherResponseToWeather(WeatherResponse weatherResponse);

}
