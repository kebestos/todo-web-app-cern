This is a working Spring Boot application, with basic authorization/authentication implemented by using spring security.

Java 21
Spring boot 3.4.1

This application has basic CRUD REST API for task and task category, based on the following model :

![DB model](DBModel.png)

An API to search Tasks by user, name, description, deadline, deadline criteria and category in any combination is available.

There is a user restriction for the tasks API, User X cannot access Tasks of user Y as long as he doesn't have admin role.