package com.epam.gpipko.web_app.config;

import com.epam.gpipko.AuthorService;
import com.epam.gpipko.ProjectDtoService;
import com.epam.gpipko.ProjectService;
import com.epam.gpipko.service.rest.AuthorServiceRest;
import com.epam.gpipko.service.rest.ProjectDtoServiceRest;
import com.epam.gpipko.service.rest.ProjectServiceRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan
public class ApplicationConfig {

    @Value("${rest.server.protocol}")
    private String protocol;
    @Value("${rest.server.host}")
    private String host;
    @Value("${rest.server.port}")
    private Integer port;

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

    @Bean
    ProjectDtoService projectDtoService() {
        String url = String.format("%s://%s:%d/project-dtos", protocol, host, port);
        return new ProjectDtoServiceRest(url, restTemplate());
    }

    @Bean
    ProjectService projectService() {
        String url = String.format("%s://%s:%d/projects", protocol, host, port);
        return new ProjectServiceRest(url, restTemplate());
    }

    @Bean
    AuthorService authorService() {
        String url = String.format("%s://%s:%d/authors", protocol, host, port);
        return new AuthorServiceRest(url, restTemplate());
    }
}
