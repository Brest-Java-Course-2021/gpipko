package com.epam.gpipko;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    List<Author> findAll();

    Optional<Author> findById(Integer authorId);

    void create(Author author);

    Integer update(Author author);

    Integer delete(Integer authorId);

}
