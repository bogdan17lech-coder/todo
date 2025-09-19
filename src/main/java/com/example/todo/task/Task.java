package com.example.todo.task;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

@Entity
@Table(name = "tasks")
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "title must not be blank")
    private String title;

    private boolean completed = false;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public Task() {} // JPA нужен пустой конструктор

    @PreUpdate
    void preUpdate() { this.updatedAt = Instant.now(); }

    // ... остальной код Task

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    // (по желанию добавь и для id/createdAt/updatedAt)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.time.Instant updatedAt) { this.updatedAt = updatedAt; }


    // Сгенерируй getters/setters в IDE (Alt+Insert)
}
