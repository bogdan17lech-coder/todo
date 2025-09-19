package com.example.todo.task;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class TaskService {
    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public Task create(Task t) {
        return repo.save(t);
    }

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


    public Task get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    public Task update(Long id, Task patch) {
        Task t = get(id);
        if (patch.getTitle() != null && !patch.getTitle().isBlank()) {
            t.setTitle(patch.getTitle());
        }
        // Примитив boolean: если поле в JSON не передали — станет false.
        t.setCompleted(patch.isCompleted());
        return repo.save(t);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        repo.deleteById(id);
    }


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

    public Task setCompleted(Long id, boolean value) {
        Task t = get(id);      // кинет 404, если нет
        t.setCompleted(value); // @PreUpdate обновит updatedAt
        return repo.save(t);
    }

}
