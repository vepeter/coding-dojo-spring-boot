package com.assignment.spring.application.dataloader.openweathermap.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.assignment.spring.application.exception.CityNotFoundException;
import com.assignment.spring.application.exception.SystemException;
import com.assignment.spring.application.model.Weather;

@SpringBootTest({
    "api.openweathermap.url=http://server/weather?q={city}&appid={appid}",
    "api.openweathermap.appid=testappid"})
@EnableCircuitBreaker
@EnableAspectJAutoProxy
class OpenWeatherMapDataLoaderImplTest {

    private MockRestServiceServer mockServer;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OpenWeatherMapDataLoaderImpl unitToTest;

    @BeforeEach
    public void setUp() {
        this.mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testLoad_respondsWithNormalResult() {
        mockServer.expect(ExpectedCount.once(), requestTo("http://server/weather?q=testcity&appid=testappid"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        MockRestResponseCreators.withSuccess(
                                new ClassRelativeResourceLoader(OpenWeatherMapDataLoaderImplTest.class)
                                        .getResource("OpenWeatherMapDataLoaderImplTest-api-response.json"),
                                MediaType.APPLICATION_JSON));

        Weather result = unitToTest.load("testcity");

        assertThat(result).extracting("city", "country", "temperature")
                .containsExactly("Amsterdam", "NL", 268.3);
    }

    @Test
    void testLoad_respondsWithSystemException() {
        mockServer.expect(ExpectedCount.once(), 
                          requestTo("http://server/weather?q=testcity&appid=testappid"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.BAD_GATEWAY));
        
        assertThatThrownBy(() -> unitToTest.load("testcity"))
                .isInstanceOf(SystemException.class);
    }
    
    @Test
    void testLoad_respondsWithCityNotFoundException() {
        mockServer.expect(ExpectedCount.once(), 
                        requestTo("http://server/weather?q=testcity&appid=testappid"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));
        
        assertThatThrownBy(() -> unitToTest.load("testcity"))
                .isInstanceOf(CityNotFoundException.class)
                .hasMessageContaining("The city testcity is not found")
                .extracting("city").isEqualTo("testcity");
    }

}
