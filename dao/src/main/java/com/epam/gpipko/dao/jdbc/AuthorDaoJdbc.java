package com.epam.gpipko.dao.jdbc;

import com.epam.gpipko.Author;
import com.epam.gpipko.AuthorDao;
import com.epam.gpipko.Project;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class AuthorDaoJdbc implements AuthorDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorDaoJdbc.class);

    @Value("${author.select}")
    private String selectSql;

    @Value("${author.findById}")
    private String findByIdSql;

    @Value("${author.create}")
    private String createSql;

    @Value("${author.check}")
    private String checkSql;

    @Value("${author.update}")
    private String updateSql;

    @Value("${author.delete}")
    private String deleteSql;

    @Value("${author.selectAllId}")
    private String selectAllIdSql;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    RowMapper rowMapper = BeanPropertyRowMapper.newInstance(Author.class);

    public AuthorDaoJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Author> findAll() {
        LOGGER.debug("Find all authors");
        return namedParameterJdbcTemplate.query(selectSql, rowMapper);
    }

    @Override
    public List<Project> getAllProjectId() {
        LOGGER.debug("Find all project id");
        return namedParameterJdbcTemplate.query(selectAllIdSql, rowMapper);
    }

    @Override
    public Optional<Author> findById(Integer authorId) {

        LOGGER.debug("Find author by id: {}", authorId);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("AUTHOR_ID", authorId);
        List<Author> results = namedParameterJdbcTemplate.query(findByIdSql, sqlParameterSource, rowMapper);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(results));
    }

    private boolean isEmailUnique(Author author) {
        LOGGER.debug("checks is author email unique: {}", author);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("EMAIL", author.getEmail());
        return namedParameterJdbcTemplate.queryForObject(checkSql, sqlParameterSource, Integer.class) == 0;
    }

    @Override
    public Integer create(Author author) {
        LOGGER.debug("Create author: {}", author);
        if (!isEmailUnique(author)) {
            throw new IllegalArgumentException("Author's email is not unique!'");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("FIRSTNAME", author.getFirstName())
                .addValue("LASTNAME", author.getLastName())
                .addValue("EMAIL", author.getEmail())
                .addValue("GRANT_SUM", author.getGrantSum())
                .addValue("PROJECT_ID", author.getProjectId());
        namedParameterJdbcTemplate.update(createSql, sqlParameterSource, keyHolder);
        Integer authorId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        author.setAuthorId(authorId);
        return authorId;

    }


    @Override
    public Integer update(Author author) {
        LOGGER.debug("Update author: {}", author);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("AUTHOR_ID", author.getAuthorId())
                .addValue("FIRSTNAME", author.getFirstName())
                .addValue("LASTNAME", author.getLastName())
                .addValue("EMAIL", author.getEmail())
                .addValue("GRANT_SUM", author.getGrantSum())
                .addValue("PROJECT_ID", author.getProjectId());
        return namedParameterJdbcTemplate.update(updateSql, sqlParameterSource);
    }

    @Override
    public Integer delete(Integer authorId) {
        LOGGER.debug("Delete author by id: {}", authorId);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("AUTHOR_ID", authorId);
        return namedParameterJdbcTemplate.update(deleteSql, sqlParameterSource);
    }
}
