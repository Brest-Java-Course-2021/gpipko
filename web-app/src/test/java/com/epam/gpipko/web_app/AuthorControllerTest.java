package com.epam.gpipko.web_app;

import com.epam.gpipko.Author;
import com.epam.gpipko.AuthorService;
import com.epam.gpipko.ProjectService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private ProjectService projectService;

    @Captor
    private ArgumentCaptor<Author> captor;

    @Test
    public void shouldReturnAuthorsPage() throws Exception {
        Author a1 = createAuthor(1, "Walter", "Gilman", "wgilman@mail.com",
                50, 1);
        Author a2 = createAuthor(2, "Henry", "Wotton", "hwotton@mail.com",
                100, 1);
        Author a3 = createAuthor(3, "Runako", "Adisa", "radisa@mail.com",
                30, 2);
        Author a4 = createAuthor(4, "Ronnie", "Venereal", "rvenreal@mail.com",
                45, 2);
        when(authorService.findAll()).thenReturn(Arrays.asList(a1, a2, a3, a4));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("authors"))
                .andExpect(model().attribute("authors", hasItem(
                        allOf(
                                hasProperty("authorId", is(a1.getAuthorId())),
                                hasProperty("firstName", is(a1.getFirstName())),
                                hasProperty("lastName", is(a1.getLastName())),
                                hasProperty("email", is(a1.getEmail())),
                                hasProperty("grantSum", is(a1.getGrantSum())),
                                hasProperty("projectId", is(a1.getProjectId()))
                        )
                )))
                .andExpect(model().attribute("authors", hasItem(
                        allOf(
                                hasProperty("authorId", is(a2.getAuthorId())),
                                hasProperty("firstName", is(a2.getFirstName())),
                                hasProperty("lastName", is(a2.getLastName())),
                                hasProperty("email", is(a2.getEmail())),
                                hasProperty("grantSum", is(a2.getGrantSum())),
                                hasProperty("projectId", is(a2.getProjectId()))
                        )
                )))
                .andExpect(model().attribute("authors", hasItem(
                        allOf(
                                hasProperty("authorId", is(a3.getAuthorId())),
                                hasProperty("firstName", is(a3.getFirstName())),
                                hasProperty("lastName", is(a3.getLastName())),
                                hasProperty("email", is(a3.getEmail())),
                                hasProperty("grantSum", is(a3.getGrantSum())),
                                hasProperty("projectId", is(a3.getProjectId()))
                        )
                )))
                .andExpect(model().attribute("authors", hasItem(
                        allOf(
                                hasProperty("authorId", is(a4.getAuthorId())),
                                hasProperty("firstName", is(a4.getFirstName())),
                                hasProperty("lastName", is(a4.getLastName())),
                                hasProperty("email", is(a4.getEmail())),
                                hasProperty("grantSum", is(a4.getGrantSum())),
                                hasProperty("projectId", is(a4.getProjectId()))
                        )
                )))
        ;
    }

    @Test
    public void shouldOpenNewAuthorPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/author")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("author"))
                .andExpect(model().attribute("isNew", is(true)))
                .andExpect(model().attribute("author", is(Author.class)));
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void shouldAddNewAuthor() throws Exception {

        String testValue = RandomStringUtils.randomAlphabetic(255);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/author")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("authorId", String.valueOf(23))
                        .param("firstName", testValue)
                        .param("lastName",testValue)
                        .param("email","testemail@gmail.com")
                        .param("grantSum",String.valueOf(440))
                        .param("projectId", String.valueOf(1))
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/authors"))
                .andExpect(redirectedUrl("/authors"));

        verify(authorService).create(captor.capture());

        Author author = captor.getValue();
        assertEquals(Integer.valueOf(23), author.getAuthorId());
        assertEquals(testValue, author.getFirstName());
        assertEquals(testValue, author.getLastName());
        assertEquals("testemail@gmail.com", author.getEmail());
        assertEquals(Integer.valueOf(440), author.getGrantSum());
        assertEquals(Integer.valueOf(1), author.getProjectId());
    }

    @Test
    public void shouldOpenEditAuthorPageById() throws Exception {
        Author author = createAuthor(1, "Walter", "Gilman", "wgilman@mail.com",
                50, 1);
        when(authorService.findById(any())).thenReturn(Optional.of(author));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/author/" + author.getProjectId())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("author"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("author", hasProperty("authorId",
                        is(author.getAuthorId()))))
                .andExpect(model().attribute("author", hasProperty("firstName",
                        is(author.getFirstName()))))
                .andExpect(model().attribute("author", hasProperty("lastName",
                        is(author.getLastName()))))
                .andExpect(model().attribute("author", hasProperty("email",
                        is(author.getEmail()))))
                .andExpect(model().attribute("author", hasProperty("grantSum",
                        is(author.getGrantSum()))))
                .andExpect(model().attribute("author", hasProperty("projectId",
                        is(author.getProjectId()))));
    }

    @Test
    public void shouldReturnToAuthorsPageIfAuthorNotFoundById() throws Exception {
        int id = 424242;
        mockMvc.perform(
                MockMvcRequestBuilders.get("/author/" + id)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("authors"));
        verify(authorService).findById(id);
    }

    @Test
    public void shouldUpdateAuthorAfterEdit() throws Exception {

        String testValue = RandomStringUtils.randomAlphabetic(255);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/author/22")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("authorId", String.valueOf(22))
                        .param("firstName", testValue)
                        .param("lastName",testValue)
                        .param("email","testemailfn@gmail.com")
                        .param("grantSum",String.valueOf(442))
                        .param("projectId", String.valueOf(2))
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/authors"))
                .andExpect(redirectedUrl("/authors"));

        verify(authorService).update(captor.capture());

        Author author = captor.getValue();
        assertEquals(Integer.valueOf(22), author.getAuthorId());
        assertEquals(testValue, author.getFirstName());
        assertEquals(testValue, author.getLastName());
        assertEquals("testemailfn@gmail.com", author.getEmail());
        assertEquals(Integer.valueOf(442), author.getGrantSum());
        assertEquals(Integer.valueOf(2), author.getProjectId());
    }

    @Test
    public void shouldDeleteAuthor() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/author/3/delete")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/authors"))
                .andExpect(redirectedUrl("/authors"));

        verify(authorService).delete(3);
    }

    private Author createAuthor(Integer authorId, String firstName, String lastName, String email,
                                Integer grantSum, Integer projectId) {
        Author author = new Author();
        author.setAuthorId(authorId);
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setEmail(email);
        author.setGrantSum(grantSum);
        author.setProjectId(projectId);
        return author;
    }
}
