package com.epam.gpipko;

import com.epam.gpipko.dao.jdbc.ProjectDaoDtoJdbc;
import com.epam.gpipko.database.SpringJdbcConfig;
import com.epam.gpipko.dto.ProjectDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import({ProjectDtoServiceImpl.class, ProjectDaoDtoJdbc.class})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@ComponentScan(basePackages = {"com.epam.gpipko.dao.jdbc", "com.epam.gpipko.database"})
@PropertySource({"classpath:daoAuthor.properties","classpath:daoProject.properties"})
@Transactional
class ProjectDtoServiceIT {

    @Autowired
    ProjectDtoService projectDtoService;

    @Test
    void findAllWithAvgGrantSum() {
        List<ProjectDto> projectList = projectDtoService.findAllWithAvgGrantSum();
        assertNotNull(projectList);
        assertTrue(projectList.size() > 0);
        assertTrue(projectList.get(0).getAvgGrantSum().intValue() > 0);
    }
}