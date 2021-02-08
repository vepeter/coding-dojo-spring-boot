package com.assignment.spring.application.exception;

/**
 * The exception is thrown when client uses incorrect city name.
 *
 */
public class CityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -9221263334775369876L;
    
    private final String city;

    public CityNotFoundException(String city) {
        super(String.format("The city %s is not found", city));
        this.city = city;
    }
    
    public String getCity() {
        return city;
    }

}
