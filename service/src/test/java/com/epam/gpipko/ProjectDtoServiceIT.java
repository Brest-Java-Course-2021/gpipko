package com.epam.gpipko;

import com.epam.gpipko.dto.ProjectDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:database.xml", "classpath*:service-test.xml", "classpath*:dao.xml"})
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