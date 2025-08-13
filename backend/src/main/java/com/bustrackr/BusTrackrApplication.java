package com.bustrackr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BusTrackrApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusTrackrApplication.class, args);
    }

}