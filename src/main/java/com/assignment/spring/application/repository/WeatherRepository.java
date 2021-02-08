package com.assignment.spring.application.repository;

import org.springframework.data.repository.CrudRepository;

import com.assignment.spring.application.model.Weather;

public interface WeatherRepository extends CrudRepository<Weather, Integer> {
}
