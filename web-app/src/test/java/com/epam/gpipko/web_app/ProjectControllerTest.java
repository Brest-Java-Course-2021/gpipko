package com.epam.gpipko.web_app;

import com.epam.gpipko.Project;
import com.epam.gpipko.ProjectDtoService;
import com.epam.gpipko.ProjectService;
import com.epam.gpipko.dto.ProjectDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private ProjectDtoService projectDtoService;

    @Captor
    private ArgumentCaptor<Project> captor;

    @Test
    public void shouldReturnProjectsPage() throws Exception {
        ProjectDto p1 = createProjectDto(1, "MilitaryTech", Integer.valueOf(150), LocalDate.of(2021,01,20));
        ProjectDto p2 = createProjectDto(2, "CivilTech", Integer.valueOf(400), LocalDate.of(2021,02,15));
        ProjectDto p3 = createProjectDto(3, "EcoTech", null, LocalDate.of(2021,03,24));
        when(projectDtoService.findAllWithAvgGrantSum()).thenReturn(Arrays.asList(p1, p2, p3));
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
                .andExpect(model().attribute("project", is(Project.class)));
        verifyNoMoreInteractions(projectDtoService, projectService);
    }

    @Test
    public void shouldAddNewProject() throws Exception {
        String testName = RandomStringUtils.randomAlphabetic(255);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/project")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("projectName", testName)
                        .param("creationDate", String.valueOf(LocalDate.of(2021,01,01)))
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/projects"))
                .andExpect(redirectedUrl("/projects"));

        verify(projectService).create(captor.capture());

        Project p = captor.getValue();
        assertEquals(testName, p.getProjectName());
    }

    @Test
    public void shouldOpenEditProjectPageById() throws Exception {
        Project p = createProject(1, "MilitaryTech", LocalDate.of(2021,01,20));
        when(projectService.findById(any())).thenReturn(Optional.of(p));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/project/" + p.getProjectId())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("project"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("project", hasProperty("projectId", is(p.getProjectId()))))
                .andExpect(model().attribute("project", hasProperty("projectName", is(p.getProjectName()))));
    }

    @Test
    public void shouldReturnToProjectsPageIfProjectNotFoundById() throws Exception {
        int id = 99999;
        mockMvc.perform(
                MockMvcRequestBuilders.get("/project/" + id)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("projects"));
        verify(projectService).findById(id);
    }

    @Test
    public void shouldUpdateProjectAfterEdit() throws Exception {

        String testName = RandomStringUtils.randomAlphabetic(255);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/project/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("projectId", "1")
                        .param("projectName", testName)
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/projects"))
                .andExpect(redirectedUrl("/projects"));

        verify(projectService).update(captor.capture());

        Project p = captor.getValue();
        assertEquals(testName, p.getProjectName());
    }

    @Test
    public void shouldDeleteProject() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/project/3/delete")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/projects"))
                .andExpect(redirectedUrl("/projects"));

        verify(projectService).delete(3);
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
