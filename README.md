ToDo Project

Simple REST API for managing tasks. Supports MySQL and H2 (in-memory) databases via Spring profiles.

Requirements

Java 24

Maven

MySQL (if profile = mysql)

How to run
Profiles

mysql – uses local MySQL

h2 – uses in-memory DB

Set active profile:

-Dspring.profiles.active=mysql


or

-Dspring.profiles.active=h2

Run with Maven
# MySQL
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=mysql"

# H2
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=h2"


App will start on: http://localhost:8080

Endpoints
Method	URL	Description
GET	/api/tasks	Get all tasks
GET	/api/tasks/{id}	Get task by id
POST	/api/tasks	Create task
PUT	/api/tasks/{id}	Update task
PATCH	/api/tasks/{id}	Mark completed
DELETE	/api/tasks/{id}	Delete task
Example JSON
{
  "title": "Study English",
  "description": "30 minutes of practice",
  "completed": false
}

Profiles config

application-mysql.yml

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/todo?createDatabaseIfNotExist=true&serverTimezone=UTC
    username: root
    password: kuwe123


application-h2.yml

spring:
  datasource:
    url: jdbc:h2:mem:todo;DB_CLOSE_DELAY=-1;MODE=MySQL
    driver-class-name: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true
      path: /h2-console
