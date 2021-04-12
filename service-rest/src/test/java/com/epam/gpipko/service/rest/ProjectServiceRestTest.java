package com.epam.gpipko.service.rest;

import com.epam.gpipko.Project;
import com.epam.gpipko.ProjectService;
import com.epam.gpipko.service.rest.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.gpipko.service.rest.config.TestConfig.PROJECTS_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class ProjectServiceRestTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceRestTest.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ProjectService projectService;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldFindAllProjects() throws Exception {

        LOGGER.debug("shouldFindAllProjects()");
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                createProject(0, LocalDate.of(2021,01,01)),
                                createProject(1, LocalDate.of(2021,02,02)))))
                );

        // when
        List<Project> projects = projectService.findAll();

        // then
        mockServer.verify();
        assertNotNull(projects);
        assertTrue(projects.size() > 0);
    }

    @Test
    public void shouldCreateProject() throws Exception {

        LOGGER.debug("shouldCreateProject()");
        // given
        Project project = new Project(RandomStringUtils.randomAlphabetic(255),
                LocalDate.of(2021,03,03));

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );
        // when
        Integer id = projectService.create(project);

        // then
        mockServer.verify();
        assertNotNull(id);
    }

    @Test
    public void shouldFindProjectById() throws Exception {

        // given
        Integer id = 1;
        Project project = new Project(RandomStringUtils.randomAlphabetic(255),
                LocalDate.of(2021,04,04));
        project.setProjectId(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(project))
                );

        // when
        Optional<Project> optionalProject = projectService.findById(id);

        // then
        mockServer.verify();
        assertTrue(optionalProject.isPresent());
        assertEquals(optionalProject.get().getProjectId(), id);
        assertEquals(optionalProject.get().getProjectName(), project.getProjectName());
        assertEquals(optionalProject.get().getCreationDate(), project.getCreationDate());
    }

    @Test
    public void shouldUpdateProject() throws Exception {

        // given
        Integer id = 1;
        Project project = new Project(RandomStringUtils.randomAlphabetic(255),
                LocalDate.of(2021,05,05));
        project.setProjectId(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(project))
                );

        // when
        int result = projectService.update(project);
        Optional<Project> updatedProjectOptional = projectService.findById(id);

        // then
        mockServer.verify();
        assertTrue(1 == result);

        assertTrue(updatedProjectOptional.isPresent());
        assertEquals(updatedProjectOptional.get().getProjectId(), id);
        assertEquals(updatedProjectOptional.get().getProjectName(), project.getProjectName());
        assertEquals(updatedProjectOptional.get().getCreationDate(), project.getCreationDate());
    }

    @Test
    public void shouldDeleteProject() throws Exception {

        // given
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );
        // when
        int result = projectService.delete(id);

        // then
        mockServer.verify();
        assertTrue(1 == result);
    }

    private Project createProject(int id, LocalDate date) {
        Project project = new Project();
        project.setProjectId(id);
        project.setProjectName("p" + id);
        project.setCreationDate(date);
        return project;
    }

}