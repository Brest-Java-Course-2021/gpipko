package com.epam.gpipko.rest_app;

import com.epam.gpipko.Project;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
public class ProjectControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectControllerIT.class);

    public static final String PROJECTS_ENDPOINT = "/projects";

    @Autowired
    private ProjectController projectController;

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    protected MockProjectService projectService = new MockProjectService();

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(projectController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldFindAllProjects() throws Exception {

        LOGGER.debug("shouldFindAllProjects()");
        List<Project> projects = projectService.findAll();
        assertNotNull(projects);
        assertTrue(projects.size() > 0);
    }

    @Test
    public void shouldFindById() throws Exception {

        LOGGER.debug("shouldFindById()");
        List<Project> projects = projectService.findAll();
        Assertions.assertNotNull(projects);
        assertTrue(projects.size() > 0);

        Integer projectId = projects.get(0).getProjectId();
        Project expProject = projectService.findById(projectId).get();
        Assertions.assertEquals(projectId, expProject.getProjectId());
        Assertions.assertEquals(projects.get(0).getProjectName(), expProject.getProjectName());
        Assertions.assertEquals(projects.get(0).getCreationDate(), expProject.getCreationDate());
        Assertions.assertEquals(projects.get(0), expProject);
    }

    @Test
    public void shouldCreateProject() throws Exception {

        LOGGER.debug("shouldCreateProject()");
        Integer countBefore = projectService.count();

        projectService.create(new Project("TestTech",
                LocalDate.of(2021, 01, 01)));

        Integer countAfter = projectService.count();
        Assertions.assertEquals(countBefore + 1, countAfter);
    }

    @Test
    public void shouldUpdateProject() throws Exception {

        LOGGER.debug("shouldUpdateProject()");
        List<Project> projects = projectService.findAll();
        Assertions.assertNotNull(projects);
        assertTrue(projects.size() > 0);

        Project project = projects.get(0);
        project.setProjectName("PROJECT_NAME");
        project.setCreationDate(LocalDate.of(2020, 10, 10));
        projectService.update(project);

        Optional<Project> realProject = projectService.findById(project.getProjectId());
        Assertions.assertEquals("PROJECT_NAME", realProject.get().getProjectName());
        Assertions.assertEquals(LocalDate.of(2020, 10, 10), realProject.get().getCreationDate());
    }

    @Test
    public void shouldDeleteProject() throws Exception {

        LOGGER.debug("shouldDeleteProject()");
        Integer id = projectService.create(new Project("TEST_PROJECT",
                LocalDate.of(2021, 04, 21)));
        Integer countBefore = projectService.count();

        projectService.delete(id);

        Integer countAfter = projectService.count();
        Assertions.assertEquals(countBefore - 1, countAfter);
    }

    private class MockProjectService {

        public List<Project> findAll() throws Exception {
            LOGGER.debug("findAll()");
            MockHttpServletResponse response = mockMvc.perform(get(PROJECTS_ENDPOINT)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);

            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Project>>() {
            });
        }

        public Optional<Project> findById(Integer projectId) throws Exception {
            LOGGER.debug("findById({})", projectId);
            MockHttpServletResponse response = mockMvc.perform(get(PROJECTS_ENDPOINT + "/" + projectId)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return Optional.of(objectMapper.readValue(response.getContentAsString(), Project.class));
        }

        public Integer create(Project project) throws Exception {
            LOGGER.debug("create({})", project);
            String json = objectMapper.writeValueAsString(project);
            MockHttpServletResponse response =
                    mockMvc.perform(post(PROJECTS_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isCreated())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        public Integer update(Project project) throws Exception {
            LOGGER.debug("update({})", project);
            MockHttpServletResponse response =
                    mockMvc.perform(put(PROJECTS_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(project))
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        public Integer delete(Integer projectId) throws Exception {
            LOGGER.debug("delete(id:{})", projectId);
            MockHttpServletResponse response = mockMvc.perform(
                    MockMvcRequestBuilders.delete(new StringBuilder(PROJECTS_ENDPOINT).append("/")
                            .append(projectId).toString())
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();

            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        public Integer count() throws Exception {
            LOGGER.debug("count()");
            MockHttpServletResponse response = mockMvc.perform(get(PROJECTS_ENDPOINT + "/count")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }
    }
}
