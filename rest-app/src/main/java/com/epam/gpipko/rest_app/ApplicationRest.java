package com.epam.gpipko.rest_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = "com.epam.gpipko")
@PropertySource({"classpath:daoProject.properties","classpath:daoAuthor.properties"})
public class ApplicationRest extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationRest.class, args);
    }
}
