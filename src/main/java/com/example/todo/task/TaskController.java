package com.example.todo.task;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for tasks (JSON API under /api/tasks).
 * Keep endpoints small and straightforward.
 */
@RestController
@RequestMapping("/api/tasks") // base path
public class TaskController {

    private final TaskService service; // DI

    public TaskController(TaskService service) {
        this.service = service;
    }

    // Create a task (validates body)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@Valid @RequestBody Task body) {
        return service.create(body);
    }

    // Get single task by id
    @GetMapping("/{id}")
    public Task get(@PathVariable Long id) {
        return service.get(id);
    }

    // Partial update (title/completed from body)
    @PatchMapping("/{id}")
    public Task patch(@PathVariable Long id, @RequestBody Task body) {
        return service.update(id, body);
    }

    // Delete task by id
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // List tasks; supports filter (q, completed) and optional paging (page,size)
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

    // Mark as completed
    @PatchMapping("/{id}/complete")
    public Task complete(@PathVariable Long id) {
        return service.setCompleted(id, true);
    }

    // Mark as not completed
    @PatchMapping("/{id}/uncomplete")
    public Task uncomplete(@PathVariable Long id) {
        return service.setCompleted(id, false);
    }
}
