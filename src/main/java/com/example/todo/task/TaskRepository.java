package com.example.todo.task;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByOrderByCreatedAtDesc();
    List<Task> findByCompletedOrderByCreatedAtDesc(boolean completed);
    List<Task> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String q);
    List<Task> findByTitleContainingIgnoreCaseAndCompletedOrderByCreatedAtDesc(String q, boolean completed);
    Page<Task> findByCompleted(boolean completed, Pageable p);
    Page<Task> findByTitleContainingIgnoreCase(String q, Pageable p);
    Page<Task> findByTitleContainingIgnoreCaseAndCompleted(String q, boolean completed, Pageable p);
}
