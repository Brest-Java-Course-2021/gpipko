package com.epam.gpipko.service.rest;


import com.epam.gpipko.Project;
import com.epam.gpipko.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceRest implements ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceRest.class);

    private String url;

    private RestTemplate restTemplate;

    public ProjectServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Project> findAll() {

        LOGGER.debug("findAll()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<Project>) responseEntity.getBody();
    }

    @Override
    public Optional<Project> findById(Integer projectId) {

        LOGGER.debug("findById({})", projectId);
        ResponseEntity<Project> responseEntity =
                restTemplate.getForEntity(url + "/" + projectId, Project.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Integer create(Project project) {

        LOGGER.debug("create({})", project);
        ResponseEntity responseEntity = restTemplate.postForEntity(url, project, Integer.class);
        return (Integer) responseEntity.getBody();
    }

    @Override
    public Integer update(Project project) {

        LOGGER.debug("update({})", project);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Project> entity = new HttpEntity<>(project, headers);
        ResponseEntity<Integer> result = restTemplate.exchange(url, HttpMethod.PUT, entity, Integer.class);
        return result.getBody();
    }

    @Override
    public Integer delete(Integer projectId) {

        LOGGER.debug("delete({})", projectId);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Project> entity = new HttpEntity<>(headers);
        ResponseEntity<Integer> result =
                restTemplate.exchange(url + "/" + projectId, HttpMethod.DELETE, entity, Integer.class);
        return result.getBody();
    }

    @Override
    public Integer count() {
        ResponseEntity responseEntity = restTemplate.getForEntity(url + "/count", Integer.class);
        return (Integer) responseEntity.getBody();
    }
}
