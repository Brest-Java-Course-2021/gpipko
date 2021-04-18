package com.epam.gpipko.service.rest;

import com.epam.gpipko.Author;
import com.epam.gpipko.AuthorService;
import com.epam.gpipko.service.rest.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.gpipko.service.rest.config.TestConfig.AUTHORS_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class AuthorServiceRestTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorServiceRestTest.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AuthorService authorService;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldFindAllAuthors() throws Exception {

        LOGGER.debug("shouldFindAllAuthors()");
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(AUTHORS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                createAuthor(0, 1, 75),
                                createAuthor(1, 1, 95)
                                )
                                )
                        )
                );

        // when
        List<Author> authorList = authorService.findAll();

        // then
        mockServer.verify();
        assertNotNull(authorList);
        assertTrue(authorList.size() > 0);
    }

    @Test
    public void shouldCreateAuthor() throws Exception {

        LOGGER.debug("shouldCreateAuthor()");
        // given
        Author author = new Author(1, RandomStringUtils.randomAlphabetic(42),
                RandomStringUtils.randomAlphabetic(42),
                RandomStringUtils.randomAlphabetic(42),24, 3 );

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(AUTHORS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );
        // when
        Integer id = authorService.create(author);

        // then
        mockServer.verify();
        assertNotNull(id);
    }

    @Test
    public void shouldFindAuthorById() throws Exception {

        // given
        Integer id = 1;
        Author author = new Author(RandomStringUtils.randomAlphabetic(42),
                RandomStringUtils.randomAlphabetic(42),
                RandomStringUtils.randomAlphabetic(42),25, 2 );
        author.setAuthorId(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(AUTHORS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(author))
                );

        // when
        Optional<Author> optionalAuthor = authorService.findById(id);

        // then
        mockServer.verify();
        assertTrue(optionalAuthor.isPresent());
        assertEquals(optionalAuthor.get().getAuthorId(), id);
        assertEquals(optionalAuthor.get().getFirstName(), author.getFirstName());
        assertEquals(optionalAuthor.get().getLastName(), author.getLastName());
        assertEquals(optionalAuthor.get().getEmail(), author.getEmail());
        assertEquals(optionalAuthor.get().getGrantSum(), author.getGrantSum());
        assertEquals(optionalAuthor.get().getProjectId(), author.getProjectId());
    }

    @Test
    public void shouldUpdateAuthor() throws Exception {

        // given
        Integer id = 1;
        Author author = new Author(RandomStringUtils.randomAlphabetic(42),
                RandomStringUtils.randomAlphabetic(42),
                RandomStringUtils.randomAlphabetic(42),26, 1 );
        author.setAuthorId(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(AUTHORS_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(AUTHORS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(author))
                );

        // when
        int result = authorService.update(author);
        Optional<Author> updatedAuthorOptional = authorService.findById(id);

        // then
        mockServer.verify();
        assertTrue(1 == result);

        assertTrue(updatedAuthorOptional.isPresent());
        assertEquals(updatedAuthorOptional.get().getAuthorId(), id);
        assertEquals(updatedAuthorOptional.get().getFirstName(), author.getFirstName());
        assertEquals(updatedAuthorOptional.get().getLastName(), author.getLastName());
        assertEquals(updatedAuthorOptional.get().getEmail(), author.getEmail());
        assertEquals(updatedAuthorOptional.get().getGrantSum(), author.getGrantSum());
        assertEquals(updatedAuthorOptional.get().getProjectId(), author.getProjectId());
    }

    @Test
    public void shouldDeleteAuthor() throws Exception {

        // given
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(AUTHORS_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );
        // when
        int result = authorService.delete(id);

        // then
        mockServer.verify();
        assertTrue(1 == result);
    }

    private Author createAuthor(Integer authorId, Integer projectId, Integer grantSum) {
        Author author = new Author();
        author.setAuthorId(authorId);
        author.setFirstName("f" + authorId);
        author.setLastName("l" + authorId);
        author.setEmail("e@" + authorId);
        author.setGrantSum(grantSum);
        author.setProjectId(projectId);
        return author;
    }
}
