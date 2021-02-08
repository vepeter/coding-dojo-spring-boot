package com.assignment.spring.application.dataloader;

import com.assignment.spring.application.model.Weather;

public interface WeatherDataLoader {
    
    /**
     * @param city
     * @return
     */
    Weather load(String city);

}
