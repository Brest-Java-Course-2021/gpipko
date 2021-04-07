package com.epam.gpipko.dao.jdbc;

import com.epam.gpipko.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.ArgumentMatchers.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProjectDaoJdbcMockTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private ProjectDaoJdbc projectDaoJdbc;

    @Captor
    private ArgumentCaptor<RowMapper<Project>> captor;

    @Test
    public void findAllTest() {
        String sql = "select";
        ReflectionTestUtils.setField(projectDaoJdbc, "selectSql", sql);

        Project project = new Project();
        List<Project> list = new ArrayList<>();
        list.add(project);
        Mockito.when(namedParameterJdbcTemplate.query(any(), ArgumentMatchers.<RowMapper<Project>>any()))
                .thenReturn(list);

        List<Project> result = projectDaoJdbc.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertSame(result.get(0), project);

        Mockito.verify(namedParameterJdbcTemplate, Mockito.times(1))
                .query(eq(sql), captor.capture());

        RowMapper<Project> mapper = captor.getValue();
        Assertions.assertNotNull(mapper);
        Mockito.verifyNoMoreInteractions(namedParameterJdbcTemplate);
    }


}
