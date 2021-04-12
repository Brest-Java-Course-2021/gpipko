package com.epam.gpipko;

import com.epam.gpipko.dao.jdbc.ProjectDaoDtoJdbc;
import com.epam.gpipko.database.SpringJdbcConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import({ProjectServiceImpl.class, ProjectDaoDtoJdbc.class})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@ComponentScan(basePackages = {"com.epam.gpipko.dao.jdbc", "com.epam.gpipko.database"})
@PropertySource({"classpath:daoAuthor.properties","classpath:daoProject.properties"})
@Transactional
public class ProjectServiceIT {

    @Autowired
    ProjectService projectService;

    @Test
    public void shouldFindAllTest() {
        List<Project> projectList = projectService.findAll();
        assertNotNull(projectList);
        assertTrue(projectList.size()>0);
    }

    @Test
    public void shouldFindByIdTest(){
        List<Project> projectList = projectService.findAll();
        Assertions.assertNotNull(projectList);
        assertTrue(projectList.size() > 0);
        Integer projectId = projectList.get(0).getProjectId();
        Project expProject = projectService.findById(projectId).get();
        Assertions.assertEquals(projectId, expProject.getProjectId());
        Assertions.assertEquals(projectList.get(0).getProjectName(), expProject.getProjectName());
        Assertions.assertEquals(projectList.get(0).getCreationDate(), expProject.getCreationDate());
        Assertions.assertEquals(projectList.get(0), expProject);
    }

    @Test
    public void findByIdExceptionalTest() {
        Optional<Project> optionalProject = projectService.findById(381);
        assertTrue(optionalProject.isEmpty());
    }

    @Test
    public void createProjectTest() {
        List<Project> projectList = projectService.findAll();
        Assertions.assertNotNull(projectList);
        assertTrue(projectList.size() > 0);

        projectService.create(new Project("ServiceTest", LocalDate.of(2021,02,02)));

        List<Project> realProjects = projectService.findAll();
        Assertions.assertEquals(projectList.size() + 1, realProjects.size());
    }

    @Test
    public void createProjectWithSameNameTest() {
        List<Project> projectList = projectService.findAll();
        Assertions.assertNotNull(projectList);
        assertTrue(projectList.size() > 0);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    projectService.create(new Project("OilTech", LocalDate.of(2021,03,03)));
                    projectService.create(new Project("OilTech", LocalDate.of(2021,03,03)));
                }
        );
    }

    @Test
    public void updateProjectTest() {
        List<Project> projectList = projectService.findAll();
        Assertions.assertNotNull(projectList);
        assertTrue(projectList.size() > 0);

        Project project = projectList.get(0);
        project.setProjectName("SERVICE PROJECT");
        projectService.update(project);

        Optional<Project> realProject = projectService.findById(project.getProjectId());
        Assertions.assertEquals("SERVICE PROJECT", realProject.get().getProjectName());
    }

    @Test
    public void deleteProjectTest() {
        List<Project> projectList = projectService.findAll();
        Assertions.assertTrue(projectList.size() > 0);

        Project project = projectList.get(0);
        projectService.delete(project.getProjectId());
        List<Project> realProject = projectService.findAll();

        Assertions.assertEquals(realProject.size() + 1, projectList.size());
    }

    @Test
    public void countProjectTest() {
        List<Project> projectList = projectService.findAll();
        Assertions.assertTrue(projectList.size() > 0);

        int count = projectService.count();
        Assertions.assertEquals(projectList.size(), count);
    }
}
