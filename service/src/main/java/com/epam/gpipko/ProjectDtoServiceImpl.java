package com.epam.gpipko;

import com.epam.gpipko.dto.ProjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ProjectDtoServiceImpl implements ProjectDtoService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDtoServiceImpl.class);

    private final ProjectDtoDao projectDtoDao;

    @Autowired
    public ProjectDtoServiceImpl(ProjectDtoDao projectDtoDao) {
        this.projectDtoDao = projectDtoDao;
    }

    @Override
    public List<ProjectDto> findAllWithAvgGrantSum() {
        return projectDtoDao.findAllWithAvgGrantSum();
    }

    @Override
    public List<ProjectDto> findAllWithFilter(LocalDate startDate, LocalDate endDate) {
        return projectDtoDao.findAllWithFilter(startDate,endDate);
    }
}
