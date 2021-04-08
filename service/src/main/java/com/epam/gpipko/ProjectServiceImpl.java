package com.epam.gpipko;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectDao projectDao;

    @Autowired
    public ProjectServiceImpl(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    @Override
    public List<Project> findAll() {
        return projectDao.findAll();
    }

    @Override
    public Optional<Project> findById(Integer projectId) {
        return projectDao.findById(projectId);
    }

    @Override
    public Integer create(Project project) {
        return projectDao.create(project);
    }

    @Override
    public Integer update(Project project) {
        return projectDao.update(project);
    }

    @Override
    public Integer delete(Integer projectId) {
        return projectDao.delete(projectId);
    }

    @Override
    public Integer count() {
        return projectDao.count();
    }
}
