package com.epam.gpipko.rest_app;

import com.epam.gpipko.ProjectDtoService;
import com.epam.gpipko.dto.ProjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

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

    @GetMapping("/project-dtos/search/{startDate}/{endDate}")
    public List<ProjectDto> findByDate(@PathVariable("startDate") String startDateStr,
                                       @PathVariable("endDate") String endDateStr){
        LOGGER.debug("findByDate({},{})", startDateStr, endDateStr);

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        return projectDtoService.findAllWithFilter(startDate, endDate);

    }
}
