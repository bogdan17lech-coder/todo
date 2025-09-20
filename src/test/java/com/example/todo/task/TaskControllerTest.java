package com.example.todo.task;

import com.example.todo.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaskController.class)
@Import(ApiExceptionHandler.class)
class TaskControllerTest {

    @Autowired MockMvc mvc;
    @MockBean TaskService service;

    private Task task(long id, String title, boolean completed) {
        Task t = new Task();
        t.setId(id);
        t.setTitle(title);
        t.setCompleted(completed);
        return t;
    }

    @Test
    void create_returns201_and_body() throws Exception {
        Mockito.when(service.create(any(Task.class)))
                .thenReturn(task(1L, "Buy milk", false));

        mvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Buy milk\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Buy milk"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void list_returns_array() throws Exception {
        Mockito.when(service.list(
                nullable(String.class),
                nullable(Boolean.class),
                nullable(Integer.class),
                nullable(Integer.class)
        )).thenReturn(List.of(task(1L, "A", false), task(2L, "B", true)));
    }

    @Test
    void get_not_found_maps_to_404_json() throws Exception {
        Mockito.when(service.get(42L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        mvc.perform(get("/api/tasks/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Task not found"));
    }

    @Test
    void complete_and_uncomplete() throws Exception {
        Mockito.when(service.setCompleted(1L, true)).thenReturn(task(1L, "X", true));
        Mockito.when(service.setCompleted(1L, false)).thenReturn(task(1L, "X", false));

        mvc.perform(patch("/api/tasks/1/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));

        mvc.perform(patch("/api/tasks/1/uncomplete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void delete_returns204() throws Exception {
        mvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(service).delete(1L);
    }

    @Test
    void post_empty_body_maps_to_invalid_or_missing_body() throws Exception {
        mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("invalid_or_missing_body"));
    }

    @Test
    void post_blank_title_maps_to_validation_failed() throws Exception {
        mvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("validation_failed"))
                .andExpect(jsonPath("$.details.title").value("title must not be blank"));
    }
}
