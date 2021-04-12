package com.epam.gpipko.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ProjectDto {

    private Integer projectId;

    private String projectName;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;

    private Integer avgGrantSum;

    public ProjectDto() {
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getAvgGrantSum() {
        return avgGrantSum;
    }

    public void setAvgGrantSum(Integer avgGrantSum) {
        this.avgGrantSum = avgGrantSum;
    }

    @Override
    public String toString() {
        return "ProjectDto{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", creationDate=" + creationDate +
                ", avgGrantSum=" + avgGrantSum +
                '}';
    }
}
