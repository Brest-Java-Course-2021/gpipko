package com.epam.gpipko.service.rest;

import com.epam.gpipko.Author;
import com.epam.gpipko.AuthorService;
import com.epam.gpipko.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceRest implements AuthorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorServiceRest.class);

    private String url;

    private RestTemplate restTemplate;

    public AuthorServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }


    @Override
    public List<Author> findAll() {

        LOGGER.debug("findAll()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<Author>) responseEntity.getBody();
    }

    @Override
    public List<Project> getAllProjectId() {

        LOGGER.debug("getAllProjectId()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<Project>) responseEntity.getBody();
    }

    @Override
    public Optional<Author> findById(Integer authorId) {

        LOGGER.debug("findById({})", authorId);
        ResponseEntity<Author> responseEntity =
                restTemplate.getForEntity(url + "/" + authorId, Author.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Integer create(Author author) {

        LOGGER.debug("create({})", author);
        ResponseEntity responseEntity = restTemplate.postForEntity(url, author, Integer.class);
        return (Integer) responseEntity.getBody();
    }

    @Override
    public Integer update(Author author) {

        LOGGER.debug("update({})", author);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Author> entity = new HttpEntity<>(author, headers);
        ResponseEntity<Integer> result = restTemplate.exchange(url, HttpMethod.PUT, entity, Integer.class);
        return result.getBody();
    }

    @Override
    public Integer delete(Integer authorId) {

        LOGGER.debug("delete({})", authorId);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Author> entity = new HttpEntity<>(headers);
        ResponseEntity<Integer> result =
                restTemplate.exchange(url + "/" + authorId, HttpMethod.DELETE, entity, Integer.class);
        return result.getBody();
    }

    @Override
    public Integer count(){
        ResponseEntity responseEntity = restTemplate.getForEntity(url + "/count", Integer.class);
        return (Integer) responseEntity.getBody();
    }
}
