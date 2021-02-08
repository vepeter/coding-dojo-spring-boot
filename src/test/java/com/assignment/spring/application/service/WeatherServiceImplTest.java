package com.assignment.spring.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.assignment.spring.application.dataloader.WeatherDataLoader;
import com.assignment.spring.application.exception.CityNotFoundException;
import com.assignment.spring.application.exception.SystemException;
import com.assignment.spring.application.model.Weather;
import com.assignment.spring.application.repository.WeatherRepository;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {

    @InjectMocks
    private WeatherServiceImpl unitToTest;

    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private WeatherDataLoader weatherDataLoader;

    @Mock
    private Weather loadedWeather;

    @Mock
    private Weather storedWeather;

    private String city = "testCity";

    @Test
    void testRegisterWeather() {
        given(weatherDataLoader.load(city)).willReturn(loadedWeather);
        given(weatherRepository.save(loadedWeather)).willReturn(storedWeather);

        Weather result = unitToTest.registerWeather(city);

        assertThat(result).isSameAs(storedWeather);
        then(weatherDataLoader).should().load(city);
        then(weatherRepository).should().save(loadedWeather);
    }
    
    @Test
    void testRegisterWeather_withCityNotFoundException() {
        given(weatherDataLoader.load(city)).willThrow(new CityNotFoundException(city));
        
        assertThatThrownBy(() -> unitToTest.registerWeather(city))
                .isInstanceOf(CityNotFoundException.class)
                .hasMessageContaining("The city " + city + " is not found")
                .extracting("city").isEqualTo(city);
        then(weatherRepository).shouldHaveNoInteractions();
    }
    
    @Test
    void testRegisterWeather_withSystemException() {
        given(weatherDataLoader.load(city))
                .willThrow(new SystemException("cannot connect to the service"));
        
        assertThatThrownBy(() -> unitToTest.registerWeather(city))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining("cannot connect to the service");
        then(weatherRepository).shouldHaveNoInteractions();
    }

}
