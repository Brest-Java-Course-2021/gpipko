package com.epam.gpipko.dao.jdbc;

import com.epam.gpipko.Project;
import com.epam.gpipko.ProjectDao;
import com.epam.gpipko.ProjectDtoDao;
import com.epam.gpipko.dto.ProjectDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:database.xml", "classpath*:test-dao.xml", "classpath*:dao.xml"})
class ProjectDaoJdbcIT {

    @Autowired
    private ProjectDtoDao projectDtoDao;

    @Autowired
    private ProjectDao projectDao;

    @Test
    public void findAllTest() {
        List<Project> projects = projectDao.findAll();
        Assertions.assertNotNull(projects);
        assertTrue(projects.size() > 0);
    }

    @Test
    public void findByIdTest() {
        List<Project> projects = projectDao.findAll();
        Assertions.assertNotNull(projects);
        assertTrue(projects.size() > 0);
        Integer projectId = projects.get(0).getProjectId();
        Project expProject = projectDao.findById(projectId).get();
        Assertions.assertEquals(projectId, expProject.getProjectId());
        Assertions.assertEquals(projects.get(0).getProjectName(), expProject.getProjectName());
        Assertions.assertEquals(projects.get(0).getCreationDate(), expProject.getCreationDate());
        Assertions.assertEquals(projects.get(0), expProject);
    }

    @Test
    public void findByIdExceptionalTest() {

        Optional<Project> optionalProject = projectDao.findById(999);
        assertTrue(optionalProject.isEmpty());
    }

    @Test
    public void createProjectTest() {
        List<Project> projects = projectDao.findAll();
        Assertions.assertNotNull(projects);
        assertTrue(projects.size() > 0);

        projectDao.create(new Project("Test", new Date("2020/01/01")));

        List<Project> realProjects = projectDao.findAll();
        Assertions.assertEquals(projects.size() + 1, realProjects.size());
    }

    @Test
    public void createProjectWithSameNameTest() {
        List<Project> projects = projectDao.findAll();
        Assertions.assertNotNull(projects);
        assertTrue(projects.size() > 0);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    projectDao.create(new Project("OilTech", new Date("2021/01/01")));
                    projectDao.create(new Project("OilTech", new Date("2021/01/01")));
                }
        );
    }

    @Test
    public void updateProjectTest() {
        List<Project> projects = projectDao.findAll();
        Assertions.assertNotNull(projects);
        assertTrue(projects.size() > 0);

        Project project = projects.get(0);
        project.setProjectName("TEST_PROJECT");
        projectDao.update(project);

        Optional<Project> realProject = projectDao.findById(project.getProjectId());
        Assertions.assertEquals("TEST_PROJECT", realProject.get().getProjectName());
    }

    @Test
    public void deleteProjectTest() {
        List<Project> projectList = projectDao.findAll();
        Assertions.assertTrue(projectList.size() > 0);

        Project project = projectList.get(0);
        projectDao.delete(project.getProjectId());
        List<Project> realProject = projectDao.findAll();

        Assertions.assertEquals(realProject.size() + 1, projectList.size());
    }

    @Test
    public void countProjectTest() {
        List<Project> projectList = projectDao.findAll();
        Assertions.assertTrue(projectList.size() > 0);

        int count = projectDao.count();
        Assertions.assertEquals(projectList.size(), count);
    }

    @Test
    public void findAvgGrantSumTest() {
        List<ProjectDto> avgGrantSumList = projectDtoDao.findAllWithAvgGrantSum();
        Assertions.assertNotNull(avgGrantSumList);
        Assertions.assertTrue(avgGrantSumList.size() > 0);
    }


}