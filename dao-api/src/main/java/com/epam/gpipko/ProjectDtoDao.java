package com.epam.gpipko;

import com.epam.gpipko.dto.ProjectDto;

import java.util.List;

public interface ProjectDtoDao {

    List<ProjectDto> findAllWithAvgGrantSum();

}
