package com.assignment.spring.application.dataloader.openweathermap.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.assignment.spring.application.dataloader.openweathermap.model.Main;
import com.assignment.spring.application.dataloader.openweathermap.model.Sys;
import com.assignment.spring.application.dataloader.openweathermap.model.WeatherResponse;
import com.assignment.spring.application.model.Weather;

class ApiWeatherResponseToWeatherConverterTest {

    ApiWeatherResponseToWeatherConverter unitToTest = Mappers.getMapper(ApiWeatherResponseToWeatherConverter.class);

    @Test
    void testWeatherResponseToWeather() {
        WeatherResponse response = new WeatherResponse();
        response.setName("test city");
        Sys sys = new Sys();
        sys.setCountry("test country");
        response.setSys(sys);
        Main main = new Main();
        main.setTemp(23.4);
        response.setMain(main);

        Weather result = unitToTest.weatherResponseToWeather(response);

        assertThat(result).extracting("id", "city", "country", "temperature")
                .containsExactly(null, "test city", "test country", 23.4);
    }

}
