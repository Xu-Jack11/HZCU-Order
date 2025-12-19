package com.hzcu.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HzcuOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzcuOrderApplication.class, args);
    }

}
