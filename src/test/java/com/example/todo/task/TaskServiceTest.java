package com.example.todo.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TaskServiceTest {

    @Autowired TaskService service;
    @Autowired TaskRepository repo;

    @BeforeEach
    void clean() {
        repo.deleteAll();
    }

    private Task t(String title) {
        Task x = new Task();
        x.setTitle(title);
        return x;
    }

    @Test
    void create_saves_defaults() {
        Task saved = service.create(t("Buy milk"));
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.isCompleted()).isFalse();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void complete_and_uncomplete_toggle_flag() {
        Task a = service.create(t("Write tests"));
        Task done = service.setCompleted(a.getId(), true);
        assertThat(done.isCompleted()).isTrue();

        Task undone = service.setCompleted(a.getId(), false);
        assertThat(undone.isCompleted()).isFalse();
    }

    @Test
    void list_filters_and_search() {
        Task t1 = service.create(t("Buy milk"));
        Task t2 = service.create(t("Write tests"));
        Task t3 = service.create(t("Buy bread"));

        List<Task> all = service.list(null, null); // непагинированный метод сервиса
        assertThat(all).hasSize(3);

        List<Task> onlyBuy = service.list("buy", null);
        assertThat(onlyBuy).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Buy milk", "Buy bread");

        service.setCompleted(t1.getId(), true);
        List<Task> completed = service.list(null, true);
        assertThat(completed).hasSize(1);
    }

    @Test
    void pagination_splits_results() {
        for (int i = 1; i <= 11; i++) service.create(t("Task " + i));

        List<Task> page0 = service.list(null, null, 0, 10);
        List<Task> page1 = service.list(null, null, 1, 10);

        assertThat(page0).hasSize(10);
        assertThat(page1).hasSize(1);
    }

    @Test
    void get_missing_throws_404() {
        assertThatThrownBy(() -> service.get(999L))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode().value())
                .isEqualTo(404);
    }
}
