package com.epam.gpipko;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    List<Author> findAll();

    List<Project> getAllProjectId();

    Optional<Author> findById(Integer authorId);

    Integer create(Author author);

    Integer update(Author author);

    Integer delete(Integer authorId);
    Integer count();
}
