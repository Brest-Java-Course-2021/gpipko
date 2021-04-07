package com.epam.gpipko.dto;

import java.util.Date;

public class ProjectDto {

    private Integer projectId;

    private String projectName;

    private Date creationDate;

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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
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
