package com.epam.gpipko.web_app;

import com.epam.gpipko.Author;
import com.epam.gpipko.AuthorService;
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
public class AuthorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);

    @Autowired
    AuthorService authorService;

    @Autowired
    ProjectService projectService;

    @Autowired
    public AuthorController(AuthorService authorService, ProjectService projectService) {
        this.authorService = authorService;
        this.projectService = projectService;
    }

    @GetMapping(value = "/authors")
    public final String authors(Model model) {

        LOGGER.debug("authors()");
        model.addAttribute("authors", authorService.findAll());
        return "authors";
    }

    @GetMapping(value = "/author/{id}")
    public final String gotoEditAuthorPage(@PathVariable Integer id, Model model) {

        LOGGER.debug("gotoEditAuthorPage({},{})", id, model);
        Optional<Author> optionalAuthor = authorService.findById(id);
        if (optionalAuthor.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("author", optionalAuthor.get());
            model.addAttribute("projects", projectService.findAll());
            return "author";
        } else {
            return "redirect:authors";
        }
    }

    @GetMapping(value = "/author")
    public final String gotoAddAuthorPage(Model model) {

        LOGGER.debug("gotoAddAuthorPage({})", model);
        model.addAttribute("isNew", true);
        model.addAttribute("author", new Author());
        model.addAttribute("projects", projectService.findAll());
        return "author";
    }

    @PostMapping(value = "/author")
    public String addAuthor(Author author) {

        LOGGER.debug("addAuthor({}, {})", author);
        this.authorService.create(author);
        return "redirect:/authors";
    }

    @PostMapping(value = "/author/{id}")
    public String updateAuthor(Author author) {

        LOGGER.debug("updateAuthor({}, {})", author);
        this.authorService.update(author);
        return "redirect:/authors";
    }

    @GetMapping(value = "/author/{id}/delete")
    public final String deleteAuthorById(@PathVariable Integer id, Model model) {

        LOGGER.debug("delete({},{})", id, model);
        authorService.delete(id);
        return "redirect:/authors";
    }
}
