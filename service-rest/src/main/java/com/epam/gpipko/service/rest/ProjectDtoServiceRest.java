package com.epam.gpipko.service.rest;

import com.epam.gpipko.ProjectDtoService;
import com.epam.gpipko.dto.ProjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Service
public class ProjectDtoServiceRest implements ProjectDtoService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDtoServiceRest.class);

    private final String url;

    private final RestTemplate restTemplate;

    public ProjectDtoServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ProjectDto> findAllWithAvgGrantSum() {

        LOGGER.debug("findAllWithAvgGrantSum()");
        return restTemplate.exchange(url, GET, null,
                new ParameterizedTypeReference<List<ProjectDto>>() {})
                .getBody();
    }

    @Override
    public List<ProjectDto> findAllWithFilter(LocalDate startDate, LocalDate endDate) {

        LOGGER.debug("findAllByDate({}, {})", startDate, endDate);
        ResponseEntity responseEntity = restTemplate.getForEntity(url + "/search/" + startDate + "/" + endDate, List.class);
        return (List<ProjectDto>) responseEntity.getBody();
    }
}
