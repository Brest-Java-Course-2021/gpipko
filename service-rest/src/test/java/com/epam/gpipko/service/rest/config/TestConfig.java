package com.epam.gpipko.service.rest.config;

import com.epam.gpipko.AuthorService;
import com.epam.gpipko.ProjectDtoService;
import com.epam.gpipko.ProjectService;
import com.epam.gpipko.service.rest.AuthorServiceRest;
import com.epam.gpipko.service.rest.ProjectDtoServiceRest;
import com.epam.gpipko.service.rest.ProjectServiceRest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfig {

    public static final String PROJECT_DTOS_URL = "http://localhost:8088/project-dtos";
    public static final String PROJECTS_URL = "http://localhost:8088/projects";
    public static final String AUTHORS_URL = "http://localhost:8088/authors";

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

    @Bean
    ProjectDtoService projectDtoService() {
        return new ProjectDtoServiceRest(PROJECT_DTOS_URL, restTemplate());
    }

    @Bean
    ProjectService projectService() {
        return new ProjectServiceRest(PROJECTS_URL, restTemplate());
    }

    @Bean
    AuthorService authorService() {
        return new AuthorServiceRest(AUTHORS_URL, restTemplate());
    }
}
