package com.epam.gpipko;

import java.util.List;
import java.util.Optional;

public interface ProjectDao {

    List<Project> findAll();

    Optional<Project> findById(Integer projectId);

    Integer create(Project project);

    Integer update(Project project);

    Integer delete(Integer projectId);

    Integer count();
}
