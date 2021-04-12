package com.epam.gpipko;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService{

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorServiceImpl.class);

    private final AuthorDao authorDao;

    @Autowired
    public AuthorServiceImpl(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public List<Author> findAll() {
        return authorDao.findAll();
    }

    @Override
    public List<Project> getAllProjectId(){
        return authorDao.getAllProjectId();
    }

    @Override
    public Optional<Author> findById(Integer authorId) {
        return authorDao.findById(authorId);
    }

    @Override
    public Integer create(Author author) {
        return authorDao.create(author);
    }

    @Override
    public Integer update(Author author) {
        return authorDao.update(author);
    }

    @Override
    public Integer delete(Integer authorId) {
        return authorDao.delete(authorId);
    }
}
