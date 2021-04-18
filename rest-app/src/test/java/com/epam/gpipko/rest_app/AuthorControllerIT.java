package com.epam.gpipko.rest_app;

import com.epam.gpipko.Author;
import com.epam.gpipko.rest_app.exception.RestResponseEntityExceptionHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
public class AuthorControllerIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorControllerIT.class);

    public static final String AUTHORS_ENDPOINT = "/authors";

    @Autowired
    private AuthorController authorController;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private RestResponseEntityExceptionHandler controllerAdvisor;

    protected MockMvc mockMvc;

    protected MockAuthorService authorService = new MockAuthorService();

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(authorController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldFindAllAuthors() throws Exception {

        LOGGER.debug("shouldFindAllAuthors()");
        List<Author> authors = authorService.findAll();
        assertNotNull(authors);
        assertTrue(authors.size() > 0);
    }

    @Test
    public void shouldFindById() throws Exception {

        LOGGER.debug("shouldFindById()");
        List<Author> authors = authorService.findAll();
        Assertions.assertNotNull(authors);
        assertTrue(authors.size() > 0);

        Integer authorId = authors.get(0).getAuthorId();
        Author expAuthor = authorService.findById(authorId).get();
        Assertions.assertEquals(authorId, expAuthor.getAuthorId());
        Assertions.assertEquals(authors.get(0).getFirstName(), expAuthor.getFirstName());
        Assertions.assertEquals(authors.get(0).getLastName(), expAuthor.getLastName());
        Assertions.assertEquals(authors.get(0).getEmail(), expAuthor.getEmail());
        Assertions.assertEquals(authors.get(0).getGrantSum(), expAuthor.getGrantSum());
        Assertions.assertEquals(authors.get(0).getProjectId(), expAuthor.getProjectId());
        Assertions.assertEquals(authors.get(0), expAuthor);
    }

    @Test
    public void shouldCreateAuthor() throws Exception {

        LOGGER.debug("shouldCreateAuthor()");
        Integer countBefore = authorService.count();

        authorService.create(new Author("Test FirstName","Test LastName",
                "Testemail@gmail.com", 50,1));

        Integer countAfter = authorService.count();
        Assertions.assertEquals(countBefore + 1, countAfter);
    }

    @Test
    public void shouldUpdateAuthor() throws Exception {

        LOGGER.debug("shouldUpdateAuthor()");
        List<Author> authors = authorService.findAll();
        Assertions.assertNotNull(authors);
        assertTrue(authors.size() > 0);

        Author author = authors.get(0);
        author.setFirstName("FNAME");
        author.setLastName("LNAME");
        author.setEmail("EMAIL@gmail.com");
        author.setGrantSum(10);
        author.setProjectId(1);
        authorService.update(author);

        Optional<Author> realAuthor = authorService.findById(author.getAuthorId());
        Assertions.assertEquals("FNAME", realAuthor.get().getFirstName());
        Assertions.assertEquals("LNAME", realAuthor.get().getLastName());
        Assertions.assertEquals("EMAIL@gmail.com", realAuthor.get().getEmail());
        Assertions.assertEquals(10, realAuthor.get().getGrantSum());
        Assertions.assertEquals(1, realAuthor.get().getProjectId());
    }

    @Test
    public void shouldDeleteAuthor() throws Exception {

        LOGGER.debug("shouldDeleteAuthor()");
        Integer id = authorService.create(new Author("Test FirstName","Test LastName",
                "TestEmail@gmail.com", 51,1));
        Integer countBefore = authorService.count();

        authorService.delete(id);

        Integer countAfter = authorService.count();
        Assertions.assertEquals(countBefore - 1, countAfter);
    }

    private class MockAuthorService {

        public List<Author> findAll() throws Exception {
            LOGGER.debug("findAll()");
            MockHttpServletResponse response = mockMvc.perform(get(AUTHORS_ENDPOINT)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);

            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Author>>() {
            });
        }

        public Optional<Author> findById(Integer authorId) throws Exception {
            LOGGER.debug("findById({})", authorId);
            MockHttpServletResponse response = mockMvc.perform(get(AUTHORS_ENDPOINT + "/" + authorId)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return Optional.of(objectMapper.readValue(response.getContentAsString(), Author.class));
        }

        public Integer create(Author author) throws Exception {
            LOGGER.debug("create({})", author);
            String json = objectMapper.writeValueAsString(author);
            MockHttpServletResponse response =
                    mockMvc.perform(post(AUTHORS_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isCreated())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        public Integer update(Author author) throws Exception {
            LOGGER.debug("update({})", author);
            MockHttpServletResponse response =
                    mockMvc.perform(put(AUTHORS_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(author))
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        public Integer delete(Integer authorId) throws Exception {
            LOGGER.debug("delete(id:{})", authorId);
            MockHttpServletResponse response = mockMvc.perform(
                    MockMvcRequestBuilders.delete(new StringBuilder(AUTHORS_ENDPOINT).append("/")
                            .append(authorId).toString())
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();

            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        public Integer count() throws Exception {
            LOGGER.debug("count()");
            MockHttpServletResponse response = mockMvc.perform(get(AUTHORS_ENDPOINT + "/count")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }
    }
}
