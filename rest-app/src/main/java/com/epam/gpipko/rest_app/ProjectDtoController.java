package com.epam.gpipko.rest_app;

import com.epam.gpipko.ProjectDtoService;
import com.epam.gpipko.dto.ProjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class ProjectDtoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDtoController.class);

    private ProjectDtoService projectDtoService;

    public ProjectDtoController(ProjectDtoService projectDtoService){
        this.projectDtoService = projectDtoService;
    }

    @GetMapping(value = "/project-dtos")
    public Collection<ProjectDto> findAllWithAvgSalary() {
        LOGGER.debug("findAllWithAvgGrantSum()");
        return projectDtoService.findAllWithAvgGrantSum();
    }
}
