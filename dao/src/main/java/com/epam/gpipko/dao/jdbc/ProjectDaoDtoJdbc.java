package com.epam.gpipko.dao.jdbc;

import com.epam.gpipko.ProjectDtoDao;
import com.epam.gpipko.dto.ProjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectDaoDtoJdbc implements ProjectDtoDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDaoDtoJdbc.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProjectDaoDtoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Value("${projectDto.findAllWithAvgGrantSum}")
    private String findAllWithAvgGrantSumSql;

    @Override
    public List<ProjectDto> findAllWithAvgGrantSum() {
        LOGGER.debug("findAllWithAvgSalary()");

        List<ProjectDto> projects = namedParameterJdbcTemplate.query(findAllWithAvgGrantSumSql,
                BeanPropertyRowMapper.newInstance(ProjectDto.class));
        return projects;
    }
}
