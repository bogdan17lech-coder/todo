package com.example.todo.task;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repository for Task (Spring Data JPA).
 * I use method names to auto-generate queries.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    // --- Read (no paging) ---
    // All tasks, newest first
    List<Task> findAllByOrderByCreatedAtDesc();

    // By status, newest first
    List<Task> findByCompletedOrderByCreatedAtDesc(boolean completed);

    // Search by title (case-insensitive), newest first
    List<Task> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String q);

    // Search by title + status, newest first
    List<Task> findByTitleContainingIgnoreCaseAndCompletedOrderByCreatedAtDesc(String q, boolean completed);

    // --- Read (with paging) ---
    // By status with Pageable
    Page<Task> findByCompleted(boolean completed, Pageable p);

    // Search by title with Pageable
    Page<Task> findByTitleContainingIgnoreCase(String q, Pageable p);

    // Search by title + status with Pageable
    Page<Task> findByTitleContainingIgnoreCaseAndCompleted(String q, boolean completed, Pageable p);
}
