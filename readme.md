[![Java CI with Maven](https://github.com/Brest-Java-Course-2021/gpipko/actions/workflows/maven.yml/badge.svg)](https://github.com/Brest-Java-Course-2021/gpipko/actions/workflows/maven.yml)

This is simple Competition web application which
allows you register any kind of project and manage their
authors and their data, e.g. email, first name, and so on,
including, of course, grant sum they are going to get
depending on your decision as they're working 
on assigned project. Average grant sum of
all assigned to a project will be calculated, so
you will be able to see a most successful project in this
"competition".

## Requirements

* JDK 11
* Apache Maven

## Build application:
```
mvn clean install
```
## Rest server:
### Start Rest using Apache Tomcat Maven Plugin
To start server:
```
java -jar ./rest-app/target/rest-app-1.0-SNAPSHOT.jar
```
## Available REST endpoints
### version
```
curl --request GET 'http://localhost:8088/version'
```
### project-dtos
```
curl --request GET 'http://localhost:8088/project-dtos'
```
Pretty print json:
```
curl --request GET 'http://localhost:8088/project-dtos' | json_pp
```
### projects
#### findAll
```
curl --request GET 'http://localhost:8088/projects' | json_pp
```
#### findById
```
curl --request GET 'http://localhost:8088/projects/1' | json_pp
```
#### create
```
curl --request POST 'http://localhost:8088/projects' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
    "projectId": 4,
    "projectName": "SpaceTech",
    "creationDate": "1998-05-24"
}'
```
#### update
```
curl --request PUT 'http://localhost:8088/projects' \
--header 'Content-Type: application/json' \
--data-raw '{
    "projectId": 1,
    "projectName": "MilitaryTech",
    "creationDate": "2002-04-25"
}'
```
### delete
```
curl --request DELETE 'http://localhost:8088/projects/4'
```

### authors
#### findAll
```
curl --request GET 'http://localhost:8088/authors' | json_pp
```
#### findById
```
curl --request GET 'http://localhost:8088/authors/1' | json_pp
```
#### create
```
curl --request POST 'http://localhost:8088/authors' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
    "authorId": 6,
    "firstName": "Sam",
    "lastName": "Willson",
    "email": "swillson@gmail.com",
    "grantSum": 42,
    "projectId": 2
}'
```
#### update
```
curl --request PUT 'http://localhost:8088/authors' \
--header 'Content-Type: application/json' \
--data-raw '{
    "authorId": 4,
    "firstName": "Bubba",
    "lastName": "Brown",
    "email": "bbrown@gmail.com",
    "grantSum": 150,
    "projectId": 1
}'
```
#### delete
```
curl --request DELETE 'http://localhost:8088/authors/4'
```
## OR use postman collection
You can import my postman collection via link below:
```
https://www.getpostman.com/collections/0fd4cbab385c51902d24
```
See how to install postman here:
*https://learning.postman.com/docs/getting-started/installation-and-updates/

 See how to import postman collection via a link here:
* https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman

How to run postman collection:
* https://learning.postman.com/docs/running-collections/intro-to-collection-runs/#starting-a-collection-run
