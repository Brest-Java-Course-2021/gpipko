package com.epam.gpipko;

import java.util.Objects;

public class Author {

    private Integer authorId;

    private String firstName;

    private String lastName;

    private String email;

    private Integer grantSum;

    private Integer projectId;

    public Author() {
    }

    public Author(String firstName, String lastName, String email, Integer grantSum, Integer projectId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.grantSum = grantSum;
        this.projectId = projectId;
    }

    public Author(Integer authorId, String firstName, String lastName, String email, Integer grantSum, Integer projectId) {
        this.authorId = authorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.grantSum = grantSum;
        this.projectId = projectId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGrantSum() {
        return grantSum;
    }

    public void setGrantSum(Integer grantSum) {
        this.grantSum = grantSum;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId=" + authorId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", grantSum=" + grantSum +
                ", projectId=" + projectId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(authorId, author.authorId) && Objects.equals(firstName, author.firstName)
                && Objects.equals(lastName, author.lastName) && Objects.equals(email, author.email)
                && Objects.equals(grantSum, author.grantSum) && Objects.equals(projectId, author.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId, firstName, lastName, email, grantSum, projectId);
    }
}
