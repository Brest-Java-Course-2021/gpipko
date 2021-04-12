package com.epam.gpipko.dao.jdbc;

import com.epam.gpipko.Project;
import com.epam.gpipko.ProjectDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ProjectDaoJdbc implements ProjectDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDaoJdbc.class);

    @Value("${project.select}")
    private String selectSql;

    @Value("${project.findById}")
    private String findByIdSql;

    @Value("${project.create}")
    private String createSql;

    @Value("${project.check}")
    private String checkSql;

    @Value("${project.update}")
    private String updateSql;

    @Value("${project.count}")
    private String countSql;

    @Value("${project.delete}")
    private String deleteSql;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    RowMapper rowMapper = BeanPropertyRowMapper.newInstance(Project.class);

    public ProjectDaoJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    @Override
    public List<Project> findAll() {
        LOGGER.debug("Find all projects");
        return namedParameterJdbcTemplate.query(selectSql, rowMapper);
    }

    @Override
    public Optional<Project> findById(Integer projectId) {
        LOGGER.debug("Find project by id: {}", projectId);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("PROJECT_ID", projectId);
        List<Project> results = namedParameterJdbcTemplate.query(findByIdSql, sqlParameterSource, rowMapper);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(results));
    }

    private boolean isProjectNameUnique(Project project) {
        return namedParameterJdbcTemplate.queryForObject(checkSql,
                new MapSqlParameterSource("PROJECT_NAME", project.getProjectName()), Integer.class) == 0;
    }

    @Override
    public Integer create(Project project) {
        LOGGER.debug("Create project: {}", project);
        if (!isProjectNameUnique(project)) {
            LOGGER.warn("Project with the same name already exists in DB: {}", project);
            throw new IllegalArgumentException("Project with the same name already exists in DB.");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("PROJECT_NAME", project.getProjectName())
                .addValue("CREATION_DATE", project.getCreationDate());
        namedParameterJdbcTemplate.update(createSql, sqlParameterSource, keyHolder);
        Integer projectId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        project.setProjectId(projectId);
        return projectId;
    }

    @Override
    public Integer update(Project project) {
        LOGGER.debug("Update project: {}", project);
        SqlParameterSource sqlParameterSource =
                new MapSqlParameterSource("PROJECT_NAME", project.getProjectName())
                        .addValue("PROJECT_ID", project.getProjectId())
                        .addValue("CREATION_DATE", project.getCreationDate());;
        return namedParameterJdbcTemplate.update(updateSql, sqlParameterSource);
    }

    @Override
    public Integer delete(Integer projectId) {
        LOGGER.debug("Delete project by id: {}", projectId);
        return namedParameterJdbcTemplate.update(deleteSql, new MapSqlParameterSource()
                .addValue("PROJECT_ID", projectId));
    }

    @Override
    public Integer count() {
        LOGGER.debug("count()");
        return namedParameterJdbcTemplate.queryForObject(countSql, new HashMap<>(), Integer.class);
    }
}
