package com.epam.gpipko.web_app;

import com.epam.gpipko.Project;
import com.epam.gpipko.dto.ProjectDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ProjectControllerIT {

    private static final String PROJECT_DTOS_URL = "http://localhost:8088/project-dtos";

    private static final String PROJECTS_URL = "http://localhost:8088/projects";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldReturnProjectsPage() throws Exception {
        ProjectDto p1 = createProjectDto(1, "MilitaryTech", Integer.valueOf(75), LocalDate.of(2021,01,20));
        ProjectDto p2 = createProjectDto(2, "CivilTech", Integer.valueOf(37), LocalDate.of(2021,02,15));
        ProjectDto p3 = createProjectDto(3, "EcoTech", null, LocalDate.of(2021,03,24));
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECT_DTOS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(p1, p2, p3)))
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/projects")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("projects"))
                .andExpect(model().attribute("projects", hasItem(
                        allOf(
                                hasProperty("projectId", is(p1.getProjectId())),
                                hasProperty("projectName", is(p1.getProjectName())),
                                hasProperty("avgGrantSum", is(p1.getAvgGrantSum())),
                                hasProperty("creationDate", is(p1.getCreationDate()))
                        )
                )))
                .andExpect(model().attribute("projects", hasItem(
                        allOf(
                                hasProperty("projectId", is(p2.getProjectId())),
                                hasProperty("projectName", is(p2.getProjectName())),
                                hasProperty("avgGrantSum", is(p2.getAvgGrantSum())),
                                hasProperty("creationDate", is(p2.getCreationDate()))
                        )
                )))
                .andExpect(model().attribute("projects", hasItem(
                        allOf(
                                hasProperty("projectId", is(p3.getProjectId())),
                                hasProperty("projectName", is(p3.getProjectName())),
                                hasProperty("avgGrantSum", isEmptyOrNullString()),
                                hasProperty("creationDate", is(p3.getCreationDate()))
                        )
                )))
        ;
        mockServer.verify();
    }

    @Test
    public void shouldOpenNewProjectPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/project")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("project"))
                .andExpect(model().attribute("isNew", is(true)))
                .andExpect(model().attribute("project", isA(Project.class)));
        mockServer.verify();
    }

    @Test
    public void shouldAddNewProject() throws Exception {

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/project")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("projectName", "test")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/projects"))
                .andExpect(redirectedUrl("/projects"));

        mockServer.verify();
    }

    @Test
    public void shouldOpenEditProjectPageById() throws Exception {
        Project p = createProject(1, "MilitaryTech", LocalDate.of(2021,01,20));
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL + "/" + p.getProjectId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(p))
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/project/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("project"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("project", hasProperty("projectId", is(p.getProjectId()))))
                .andExpect(model().attribute("project", hasProperty("projectName", is(p.getProjectName()))))
                .andExpect(model().attribute("project", hasProperty("creationDate", is(p.getCreationDate()))));
        mockServer.verify();
    }

    @Test
    public void shouldReturnToProjectsPageIfProjectNotFoundById() throws Exception {
        int id = 99999;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/project/" + id)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("projects"));
        mockServer.verify();
    }

    @Test
    public void shouldUpdateProjectAfterEdit() throws Exception {
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );
        String testName = RandomStringUtils.randomAlphabetic(255);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/project/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("projectId", "1")
                        .param("projectName", testName)
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/projects"))
                .andExpect(redirectedUrl("/projects"));

        mockServer.verify();
    }

    @Test
    public void shouldDeleteProject() throws Exception {
        int id = 3;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/project/3/delete")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/projects"))
                .andExpect(redirectedUrl("/projects"));

        mockServer.verify();
    }

    private ProjectDto createProjectDto(int id, String name, Integer avgGrantSum, LocalDate date) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setProjectId(id);
        projectDto.setProjectName(name);
        projectDto.setCreationDate(date);
        projectDto.setAvgGrantSum(avgGrantSum);
        return projectDto;
    }

    private Project createProject(int id, String name, LocalDate date) {
        Project project = new Project();
        project.setProjectId(id);
        project.setProjectName(name);
        project.setCreationDate(date);
        return project;
    }
}