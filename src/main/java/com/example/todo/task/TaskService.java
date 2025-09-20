package com.example.todo.task;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Service layer for Task: simple business logic + calls to repository.
 */
@Service
public class TaskService {

    private final TaskRepository repo; // injected repository

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    // Create a new task
    public Task create(Task t) {
        return repo.save(t);
    }

    // List tasks (no paging): supports text search and completed filter
    public List<Task> list(String q, Boolean completed) {
        boolean hasQ = q != null && !q.isBlank();
        if (hasQ) {
            if (completed == null) return repo.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(q);
            return repo.findByTitleContainingIgnoreCaseAndCompletedOrderByCreatedAtDesc(q, completed);
        } else {
            if (completed == null) return repo.findAllByOrderByCreatedAtDesc();
            return repo.findByCompletedOrderByCreatedAtDesc(completed);
        }
    }

    // Get one task or 404
    public Task get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    // Partial update: change title/completed if provided
    public Task update(Long id, Task patch) {
        Task t = get(id);
        if (patch.getTitle() != null && !patch.getTitle().isBlank()) {
            t.setTitle(patch.getTitle());
        }
        // Note: primitive boolean → if "completed" is missing in JSON, it becomes false.
        // For a strict PATCH you could switch to Boolean and check null.
        t.setCompleted(patch.isCompleted());
        return repo.save(t);
    }

    // Delete by id (404 if missing)
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        repo.deleteById(id);
    }

    // List with paging support (page,size) + optional filters
    public List<Task> list(String q, Boolean completed, Integer page, Integer size) {
        int p = (page == null || page < 0) ? 0 : page;
        int s = (size == null || size <= 0) ? 10 : size;
        Pageable pageable = PageRequest.of(p, s, Sort.by("createdAt").descending());

        boolean hasQ = q != null && !q.isBlank();
        if (hasQ) {
            if (completed == null) return repo.findByTitleContainingIgnoreCase(q, pageable).getContent();
            return repo.findByTitleContainingIgnoreCaseAndCompleted(q, completed, pageable).getContent();
        } else {
            if (completed == null) return repo.findAll(pageable).getContent();
            return repo.findByCompleted(completed, pageable).getContent();
        }
    }

    // Toggle completion flag
    public Task setCompleted(Long id, boolean value) {
        Task t = get(id);      // throws 404 if not found
        t.setCompleted(value); // updatedAt is handled by @PreUpdate
        return repo.save(t);
    }
}
