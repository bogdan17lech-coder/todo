# ToDo REST (Spring Boot)

**Stack:** Spring Boot 3, Web, Validation, JPA, H2  
**Port:** 8082

## Run
mvn spring-boot:run

## Endpoints
- POST  /api/tasks
- GET   /api/tasks?completed=&q=&page=&size=
- GET   /api/tasks/{id}
- PATCH /api/tasks/{id}
- PATCH /api/tasks/{id}/complete
- PATCH /api/tasks/{id}/uncomplete
- DELETE /api/tasks/{id}

## Errors
- 400 invalid_or_missing_body
- 400 validation_failed
- 404 Task not found

## Notes
-Pagination: page, size (sorted by createdAt descending).
-H2 console: /h2-console (JDBC URL: jdbc:h2:mem:todo, user sa).
