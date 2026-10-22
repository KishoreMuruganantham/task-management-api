package com.springboot.taskapi.controller;

import com.springboot.taskapi.config.GlobalExceptionHandler;
import com.springboot.taskapi.dto.request.TaskCreateDto;
import com.springboot.taskapi.dto.request.TaskUpdateDto;
import com.springboot.taskapi.dto.response.TaskRespDto;
import com.springboot.taskapi.enums.Priority;
import com.springboot.taskapi.enums.Status;
import com.springboot.taskapi.exception.InvalidCallException;
import com.springboot.taskapi.exception.ResourceNotFoundException;
import com.springboot.taskapi.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {
    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void getAllTest() throws Exception {
        TaskRespDto dto1 = new TaskRespDto(1L, "test1", "desc1", LocalDate.of(2026, 10, 30),
                Priority.HIGH, Status.PENDING, Instant.now(), Instant.now());
        TaskRespDto dto2 = new TaskRespDto(2L, "test2", "desc2", LocalDate.of(2026, 11, 1),
                Priority.LOW, Status.COMPLETED, Instant.now(), Instant.now());

        when(taskService.getAll()).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].taskId").value(1))
                .andExpect(jsonPath("$[0].title").value("test1"));

        verify(taskService, times(1)).getAll();
    }

    @Test
    public void getByIdTest() throws Exception {
        TaskRespDto dto = new TaskRespDto(1L, "testTitle", "testDescription", LocalDate.of(2026, 10, 30),
                Priority.HIGH, Status.PENDING, Instant.now(), Instant.now());

        when(taskService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(1))
                .andExpect(jsonPath("$.title").value("testTitle"));

        verify(taskService, times(1)).getById(1L);
    }

    @Test
    public void getByIdTest_Invalid() throws Exception {
        when(taskService.getById(5L)).thenThrow(new ResourceNotFoundException("Task not found"));

        mockMvc.perform(get("/api/tasks/5"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found"))
                .andExpect(jsonPath("$.comments").value("Id not found in DB"));

        verify(taskService, times(1)).getById(5L);
    }

    @Test
    public void addTest() throws Exception {
        String body = """
                {
                    "title": "testTitle",
                    "description": "testDescription",
                    "dueDate": "2026-10-30",
                    "priority": "HIGH"
                }
                """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        verify(taskService, times(1)).add(any(TaskCreateDto.class));
    }

    @Test
    public void addTest_Invalid() throws Exception {
        String body = """
                {
                    "title": "",
                    "dueDate": "2026-10-30",
                    "priority": "HIGH"
                }
                """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is required"));

        verify(taskService, never()).add(any(TaskCreateDto.class));
    }

    @Test
    public void updateTest() throws Exception {
        String body = """
                {
                    "title": "updatedTitle",
                    "status": "COMPLETED"
                }
                """;

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        verify(taskService, times(1)).update(eq(1L), any(TaskUpdateDto.class));
    }

    @Test
    public void updateTest_NoFields() throws Exception {
        doThrow(new InvalidCallException("Update op terminated"))
                .when(taskService).update(eq(1L), any(TaskUpdateDto.class));

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Update op terminated"))
                .andExpect(jsonPath("$.comments").value("Call is Invalid due to malformed data"));

        verify(taskService, times(1)).update(eq(1L), any(TaskUpdateDto.class));
    }

    @Test
    public void deleteByIdTest() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteById(1L);
    }

    @Test
    public void dynamicFilterTaskTest() throws Exception {
        TaskRespDto dto = new TaskRespDto(1L, "testTitle", "testDescription", LocalDate.of(2026, 10, 30),
                Priority.HIGH, Status.COMPLETED, Instant.now(), Instant.now());

        when(taskService.dynamicFilterTask(Priority.HIGH, Status.COMPLETED, null, null)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/tasks/filter")
                        .param("priority", "HIGH")
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("COMPLETED"));

        verify(taskService, times(1)).dynamicFilterTask(Priority.HIGH, Status.COMPLETED, null, null);
    }
}
