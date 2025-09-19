package com.example.todo.task;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@Valid @RequestBody Task body) {
        return service.create(body);
    }


    @GetMapping("/{id}")
    public Task get(@PathVariable Long id) {
        return service.get(id);
    }

    @PatchMapping("/{id}")
    public Task patch(@PathVariable Long id, @RequestBody Task body) {
        return service.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping
    public List<Task> list(@RequestParam(required = false) Boolean completed,
                           @RequestParam(required = false) String q,
                           @RequestParam(required = false) Integer page,
                           @RequestParam(required = false) Integer size) {
        if (page == null && size == null) {
            return service.list(q, completed);
        }
        return service.list(q, completed, page, size);
    }

    @PatchMapping("/{id}/complete")
    public Task complete(@PathVariable Long id) {
        return service.setCompleted(id, true);
    }

    @PatchMapping("/{id}/uncomplete")
    public Task uncomplete(@PathVariable Long id) {
        return service.setCompleted(id, false);
    }

}
