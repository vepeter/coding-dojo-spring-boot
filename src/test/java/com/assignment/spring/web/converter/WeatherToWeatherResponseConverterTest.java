package com.assignment.spring.web.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.assignment.spring.application.model.Weather;
import com.assignment.spring.web.response.WeatherResponse;

class WeatherToWeatherResponseConverterTest {

    WeatherToWeatherResponseConverter unitToTest = Mappers.getMapper(WeatherToWeatherResponseConverter.class);

    @Test
    void testWeatherResponseToWeather() {
        Weather source = new Weather();
        source.setId(123);
        source.setCity("test city");
        source.setCountry("test country");
        source.setTemperature(123.4);

        WeatherResponse result = unitToTest.weatherToWeatherResponse(source);

        assertThat(result).extracting("id", "city", "country", "temperature")
                .containsExactly(123, "test city", "test country", 123.4);
    }

}
