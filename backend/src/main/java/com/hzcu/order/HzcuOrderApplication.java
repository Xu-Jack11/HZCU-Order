package com.hzcu.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HzcuOrderApplication {

    public static void main(String[] args) {
        try {
            io.github.cdimascio.dotenv.Dotenv dotenv = io.github.cdimascio.dotenv.Dotenv.configure()
                    .directory("../")
                    .ignoreIfMissing()
                    .load();
            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
            });
            System.out.println("Environment variables loaded from .env file");
        } catch (Exception e) {
            System.out.println("Could not load .env file: " + e.getMessage());
        }

        SpringApplication.run(HzcuOrderApplication.class, args);
    }

}
