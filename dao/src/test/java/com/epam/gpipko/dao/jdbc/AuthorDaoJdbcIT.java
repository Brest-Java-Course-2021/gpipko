package com.epam.gpipko.dao.jdbc;

import com.epam.gpipko.Author;
import com.epam.gpipko.AuthorDao;
import com.epam.gpipko.Project;
import com.epam.gpipko.database.SpringJdbcConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJdbcTest
@Import({AuthorDaoJdbc.class})
@PropertySource({"classpath:daoAuthor.properties"})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorDaoJdbcIT {

    @Autowired
    private AuthorDao authorDao;

    @Test
    public void findAllTest() {
        List<Author> authorList = authorDao.findAll();
        Assertions.assertNotNull(authorList);
        assertTrue(authorList.size() > 0);
    }

    @Test
    public void findByIdTest() {
        List<Author> authorList = authorDao.findAll();
        Assertions.assertNotNull(authorList);
        Assertions.assertTrue(authorList.size() > 0);

        Integer id = authorList.get(0).getAuthorId();
        Author expAuthor = authorDao.findById(id).get();

        Assertions.assertNotNull(expAuthor);
        Assertions.assertEquals(authorList.get(0).getAuthorId(), expAuthor.getAuthorId());
        Assertions.assertEquals(authorList.get(0).getFirstName(), expAuthor.getFirstName());
        Assertions.assertEquals(authorList.get(0).getLastName(), expAuthor.getLastName());
        Assertions.assertEquals(authorList.get(0).getEmail(), expAuthor.getEmail());
        Assertions.assertEquals(authorList.get(0).getGrantSum(), expAuthor.getGrantSum());
        Assertions.assertEquals(authorList.get(0).getProjectId(), expAuthor.getProjectId());
        Assertions.assertEquals(authorList.get(0), expAuthor);
    }

    @Test
    public void getAllProjectIdTest() {
        List<Project> projectList = authorDao.getAllProjectId();
        Assertions.assertNotNull(projectList);
        Assertions.assertTrue(projectList.size() > 0);
    }

    @Test
    public void findByIdExceptionalTest() {

        Optional<Author> optionalAuthor = authorDao.findById(999);
        assertTrue(optionalAuthor.isEmpty());
    }

    @Test
    public void createAuthorTest() {
        List<Author> authorList = authorDao.findAll();
        Assertions.assertNotNull(authorList);
        assertTrue(authorList.size() > 0);

        authorDao.create(new Author("FirstName","LastName","email",50,1));

        List<Author> realAuthors = authorDao.findAll();
        Assertions.assertEquals(authorList.size() + 1, realAuthors.size());
    }

    @Test
    public void createAuthorWithSameEmailTest() {
        List<Author> authorList = authorDao.findAll();
        Assertions.assertNotNull(authorList);
        assertTrue(authorList.size() > 0);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    authorDao.create(new Author("FirstName","LastName","email",50,1));
                    authorDao.create(new Author("FirstName","LastName","email",50,1));
                }
        );
    }

    @Test
    public void updateAuthorTest() {
        List<Author> authorList = authorDao.findAll();
        Assertions.assertNotNull(authorList);
        assertTrue(authorList.size() > 0);

        Author author = authorList.get(0);
        author.setFirstName("NEW FIRST_NAME");
        authorDao.update(author);

        Optional<Author> realAuthor = authorDao.findById(author.getAuthorId());
        Assertions.assertEquals("NEW FIRST_NAME", realAuthor.get().getFirstName());
    }

    @Test
    public void deleteAuthorTest() {
        List<Author> authorList = authorDao.findAll();
        Assertions.assertTrue(authorList.size() > 0);

        Author author = authorList.get(0);
        authorDao.delete(author.getAuthorId());
        List<Author> realAuthor = authorDao.findAll();

        Assertions.assertEquals(realAuthor.size() + 1, authorList.size());
    }

}
