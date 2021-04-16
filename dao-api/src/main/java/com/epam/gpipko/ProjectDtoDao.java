package com.epam.gpipko;

import com.epam.gpipko.dto.ProjectDto;

import java.time.LocalDate;
import java.util.List;

public interface ProjectDtoDao {

    List<ProjectDto> findAllWithAvgGrantSum();

    List<ProjectDto> findAllWithFilter(LocalDate startDate, LocalDate endDate);
}
