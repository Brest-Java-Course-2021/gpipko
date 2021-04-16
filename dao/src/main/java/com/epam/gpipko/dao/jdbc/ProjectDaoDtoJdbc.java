package com.epam.gpipko.dao.jdbc;

import com.epam.gpipko.ProjectDtoDao;
import com.epam.gpipko.dto.ProjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ProjectDaoDtoJdbc implements ProjectDtoDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDaoDtoJdbc.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProjectDaoDtoJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Value("${projectDto.findAllWithAvgGrantSum}")
    private String findAllWithAvgGrantSumSql;

    @Value("${projectDto.findAllWithFilter}")
    private String findAllWithFilter;

    @Override
    public List<ProjectDto> findAllWithAvgGrantSum() {
        LOGGER.debug("findAllWithAvgSalary()");

        List<ProjectDto> projects = namedParameterJdbcTemplate.query(findAllWithAvgGrantSumSql,
                BeanPropertyRowMapper.newInstance(ProjectDto.class));
        return projects;
    }

    @Override
    public List<ProjectDto> findAllWithFilter(LocalDate startDate, LocalDate endDate) {

        LOGGER.debug("findAllWithFilter({},{})", startDate, endDate);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        if (startDate == null) {
            mapSqlParameterSource.addValue("date_start", endDate);
            mapSqlParameterSource.addValue("date_end", endDate);
        } else if (endDate == null) {
            mapSqlParameterSource.addValue("date_start", startDate);
            mapSqlParameterSource.addValue("date_end", startDate);
        } else {
            mapSqlParameterSource.addValue("date_start", startDate);
            mapSqlParameterSource.addValue("date_end", endDate);
        }

        return namedParameterJdbcTemplate.query(findAllWithFilter, mapSqlParameterSource,
                BeanPropertyRowMapper.newInstance(ProjectDto.class));
    }
}
