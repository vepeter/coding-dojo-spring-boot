package com.assignment.spring.web.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeatherResponse {

    private Integer id;

    private String city;

    private String country;

    private Double temperature;
}
