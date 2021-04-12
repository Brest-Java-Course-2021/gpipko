package com.epam.gpipko.rest_app;

import com.epam.gpipko.Project;
import com.epam.gpipko.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
public class ProjectController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping(value = "/projects")
    public Collection<Project> projects() {
        return projectService.findAll();
    }

    @GetMapping(value = "/projects/{id}")
    public ResponseEntity<Project> findById(@PathVariable Integer id) {

        LOGGER.debug("findById({})", id);
        Optional<Project> optional = projectService.findById(id);
        return optional.isPresent()
                ? new ResponseEntity<>(optional.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/projects", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> createProject(@RequestBody Project project) {
        LOGGER.debug("createProject({})", project);
        Integer id = projectService.create(project);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping(value = "/projects", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> updateProject(@RequestBody Project project) {
        LOGGER.debug("updateProject({})", project);
        Integer id = projectService.update(project);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @DeleteMapping(value = "/projects/{id}", produces = {"application/json"})
    public ResponseEntity<Integer> deleteProject(@PathVariable Integer id) {
        LOGGER.debug("deleteProject({})", id);
        Integer result = projectService.delete(id);
        return result > 0
                ? new ResponseEntity<>(result, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/projects/count")
    public ResponseEntity<Integer> count() {
        return new ResponseEntity<>(projectService.count(), HttpStatus.OK);
    }
}
