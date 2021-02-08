package com.assignment.spring.web.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.assignment.spring.application.exception.CityNotFoundException;
import com.assignment.spring.application.exception.SystemException;
import com.assignment.spring.application.model.Weather;
import com.assignment.spring.application.service.WeatherService;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTest {

    @MockBean
    private WeatherService weatherService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /weather success")
    void testWeather_success() throws Exception {
        Weather response = new Weather();
        response.setId(1);
        response.setCity("Amsterdam");
        response.setCountry("NL");
        response.setTemperature(123.4);
        given(weatherService.registerWeather("Amsterdam,nl")).willReturn(response);

        mockMvc.perform(get("/weather?city=Amsterdam,nl"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is("Amsterdam")))
                .andExpect(jsonPath("$.country", is("NL")))
                .andExpect(jsonPath("$.temperature", is(123.4)));
    }
    
    @Test
    @DisplayName("GET /weather success")
    void testWeather_withCityNotFoundException() throws Exception {
        given(weatherService.registerWeather("test"))
                .willThrow(new CityNotFoundException("test"));

        mockMvc.perform(get("/weather?city=test"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("The city test is not found")));
    }
    
    @Test
    @DisplayName("GET /weather success")
    void testWeather_withSystemException() throws Exception {
        given(weatherService.registerWeather("Amsterdam,nl"))
                .willThrow(new SystemException("cannot connect to the service"));
        
        mockMvc.perform(get("/weather?city=Amsterdam,nl"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", 
                        is("There is a problem connecting to the service. Please try later.")));
    }

}
