package com.assignment.spring.web.converter;

import org.mapstruct.Mapper;

import com.assignment.spring.application.model.Weather;
import com.assignment.spring.web.response.WeatherResponse;

@Mapper(componentModel = "spring")
public interface WeatherToWeatherResponseConverter {

    WeatherResponse weatherToWeatherResponse(Weather source);

}
