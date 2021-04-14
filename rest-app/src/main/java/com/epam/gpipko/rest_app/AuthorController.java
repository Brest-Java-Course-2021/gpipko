package com.epam.gpipko.rest_app;

import com.epam.gpipko.Author;
import com.epam.gpipko.AuthorService;
import com.epam.gpipko.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
public class AuthorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);

    private AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(value = "/authors")
    public Collection<Author> authors() {
        return authorService.findAll();
    }

    @GetMapping(value = "/authors/{id}")
    public ResponseEntity<Author> findById(@PathVariable Integer id) {

        LOGGER.debug("findById({})", id);
        Optional<Author> optional = authorService.findById(id);
        return optional.isPresent()
                ? new ResponseEntity<>(optional.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/authors", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> createAuthor(@RequestBody Author author) {
        LOGGER.debug("createAuthor({})", author);
        Integer id = authorService.create(author);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping(value = "/authors", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> updateProject(@RequestBody Author author) {
        LOGGER.debug("updateAuthor({})", author);
        Integer id = authorService.update(author);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @DeleteMapping(value = "/authors/{id}", produces = {"application/json"})
    public ResponseEntity<Integer> deleteAuthor(@PathVariable Integer id) {
        LOGGER.debug("deleteAuthor({})", id);
        Integer result = authorService.delete(id);
        return result > 0
                ? new ResponseEntity<>(result, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/authors/id")
    public Collection<Project> getProjectId() {
        return authorService.getAllProjectId();
    }

    @GetMapping(value = "/authors/count")
    public ResponseEntity<Integer> count() {
        return new ResponseEntity<>(authorService.count(), HttpStatus.OK);
    }
}
