# ToDo REST (Spring Boot)

Stack: Spring Boot 3, Web, Validation, JPA, H2  
Port: 8082

## Run
mvn spring-boot:run

## Endpoints
POST  /api/tasks
GET   /api/tasks?completed=&q=&page=&size=
GET   /api/tasks/{id}
PATCH /api/tasks/{id}
PATCH /api/tasks/{id}/complete
PATCH /api/tasks/{id}/uncomplete
DELETE /api/tasks/{id}

## Errors
400 invalid_or_missing_body / validation_failed  
404 Task not found
