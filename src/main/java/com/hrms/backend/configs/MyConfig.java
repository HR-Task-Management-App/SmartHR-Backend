package com.hrms.backend.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

    @Bean
    public ModelMapper mapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Skip only nulls, but allow blank strings (like "")
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        return modelMapper;
    }

}
