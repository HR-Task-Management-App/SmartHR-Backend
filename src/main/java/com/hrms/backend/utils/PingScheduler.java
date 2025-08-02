package com.hrms.backend.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class PingScheduler {

    @Autowired
    private RestTemplate restTemplate;

    // Runs every 10 minutes
    @Scheduled(fixedRate = 10 * 60 * 1000)  // in milliseconds
    public void pingServer() {
        try {
            String url = "https://smarthr-backend-jx0v.onrender.com/ping";
            String response = restTemplate.getForObject(url, String.class);
            log.info("Ping response {}",response);
        } catch (Exception e) {
            log.info("Ping failed {}",e.getMessage());
        }
    }
}
