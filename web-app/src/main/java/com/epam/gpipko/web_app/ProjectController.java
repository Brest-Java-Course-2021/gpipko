package com.epam.gpipko.web_app;

import com.epam.gpipko.Project;
import com.epam.gpipko.ProjectDtoService;
import com.epam.gpipko.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class ProjectController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectDtoService projectDtoService;

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectDtoService projectDtoService, ProjectService projectService){
        this.projectDtoService = projectDtoService;
        this.projectService = projectService;
    }

    @GetMapping(value = "/projects")
    public final String projects(Model model){

        LOGGER.debug("projects()");
        model.addAttribute("projects",projectDtoService.findAllWithAvgGrantSum());
        return "projects";
    }

    @GetMapping(value = "/project/{id}")
    public final String gotoEditProjectPage(@PathVariable Integer id, Model model) {

        LOGGER.debug("gotoEditProjectPage({},{})", id, model);
        Optional<Project> optionalProject = projectService.findById(id);
        if (optionalProject.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("project", optionalProject.get());
            return "project";
        } else {
            return "redirect:projects";
        }
    }

    @GetMapping(value = "/project")
    public final String gotoAddProjectPage(Model model) {

        LOGGER.debug("gotoAddProjectPage({})", model);
        model.addAttribute("isNew", true);
        model.addAttribute("project", new Project());
        return "project";
    }

    @PostMapping(value = "/project")
    public String addProject(Project project) {

        LOGGER.debug("addDProject({}, {})", project);
        this.projectService.create(project);
        return "redirect:/projects";
    }

    @PostMapping(value = "/project/{id}")
    public String updateProject(Project project) {

        LOGGER.debug("updateProject({}, {})", project);
        this.projectService.update(project);
        return "redirect:/projects";
    }

    @GetMapping(value = "/project/{id}/delete")
    public final String deleteProjectById(@PathVariable Integer id, Model model) {

        LOGGER.debug("delete({},{})", id, model);
        projectService.delete(id);
        return "redirect:/projects";
    }
}
