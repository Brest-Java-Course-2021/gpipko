package com.epam.gpipko;

import com.epam.gpipko.database.SpringJdbcConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import({AuthorServiceImpl.class})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@ComponentScan(basePackages = {"com.epam.gpipko.dao.jdbc", "com.epam.gpipko.database"})
@PropertySource({"classpath:daoAuthor.properties","classpath:daoProject.properties"})
@Transactional
public class AuthorServiceIT {

    @Autowired
    AuthorService authorService;

    @Test
    public void findAllTest() {
        List<Author> authorList = authorService.findAll();
        Assertions.assertNotNull(authorList);
        assertTrue(authorList.size() > 0);
    }

    @Test
    public void findByIdTest() {
        List<Author> authorList = authorService.findAll();
        Assertions.assertNotNull(authorList);
        Assertions.assertTrue(authorList.size() > 0);

        Integer id = authorList.get(0).getAuthorId();
        Author expAuthor = authorService.findById(id).get();

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
        List<Project> projectList = authorService.getAllProjectId();
        Assertions.assertNotNull(projectList);
        Assertions.assertTrue(projectList.size() > 0);
    }

    @Test
    public void findByIdExceptionalTest() {

        Optional<Author> optionalAuthor = authorService.findById(999);
        assertTrue(optionalAuthor.isEmpty());
    }

    @Test
    public void createAuthorTest() {
        List<Author> authorList = authorService.findAll();
        Assertions.assertNotNull(authorList);
        assertTrue(authorList.size() > 0);

        authorService.create(new Author("FirstName","LastName","email",50,1));

        List<Author> realAuthors = authorService.findAll();
        Assertions.assertEquals(authorList.size() + 1, realAuthors.size());
    }

    @Test
    public void createAuthorWithSameEmailTest() {
        List<Author> authorList = authorService.findAll();
        Assertions.assertNotNull(authorList);
        assertTrue(authorList.size() > 0);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    authorService.create(new Author("FirstName","LastName","email",50,1));
                    authorService.create(new Author("FirstName","LastName","email",50,1));
                }
        );
    }

    @Test
    public void updateAuthorTest() {
        List<Author> authorList = authorService.findAll();
        Assertions.assertNotNull(authorList);
        assertTrue(authorList.size() > 0);

        Author author = authorList.get(0);
        author.setFirstName("NEW FIRST_NAME");
        authorService.update(author);

        Optional<Author> realAuthor = authorService.findById(author.getAuthorId());
        Assertions.assertEquals("NEW FIRST_NAME", realAuthor.get().getFirstName());
    }

    @Test
    public void deleteAuthorTest() {
        List<Author> authorList = authorService.findAll();
        Assertions.assertTrue(authorList.size() > 0);

        Author author = authorList.get(0);
        authorService.delete(author.getAuthorId());
        List<Author> realAuthor = authorService.findAll();

        Assertions.assertEquals(realAuthor.size() + 1, authorList.size());
    }

}
