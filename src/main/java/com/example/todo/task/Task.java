package com.example.todo.task;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

// Simple JPA entity for "tasks" table
@Entity
@Table(name = "tasks")
public class Task {

    // ID (auto-increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Required short title
    @NotBlank(message = "title must not be blank")
    private String title;

    // Done flag (defaults to false)
    private boolean completed = false;

    // Timestamps
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    // JPA needs no-args ctor
    public Task() {}

    // Update "updatedAt" on each update
    @PreUpdate
    void preUpdate() { this.updatedAt = Instant.now(); }

    // Getters/Setters (no extra logic)
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
