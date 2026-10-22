package com.springboot.taskapi.service;

import com.springboot.taskapi.dto.request.TaskCreateDto;
import com.springboot.taskapi.dto.request.TaskUpdateDto;
import com.springboot.taskapi.dto.response.TaskRespDto;
import com.springboot.taskapi.enums.Priority;
import com.springboot.taskapi.enums.Status;
import com.springboot.taskapi.exception.InvalidCallException;
import com.springboot.taskapi.exception.ResourceNotFoundException;
import com.springboot.taskapi.model.Task;
import com.springboot.taskapi.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;
    @Mock
    private TaskRepository taskRepository;

    @Test
    public void addTest(){
        // mock
        TaskCreateDto taskDto = new TaskCreateDto("testTitle", "testDescription", LocalDate.of(2026, 10, 30), Priority.HIGH);

        // when this save method is call, don't go to DB and save, just assume it saved and give back the same task
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // actual call
        taskService.add(taskDto);

        // verify if repo calls are happening only once
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository, times(1)).save(captor.capture());

        assertEquals("testTitle", captor.getValue().getTitle());
        assertEquals(Status.PENDING, captor.getValue().getStatus());

        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void getAllTest(){
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("test1");
        task1.setStatus(Status.PENDING);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("test2");
        task2.setStatus(Status.IN_PROGRESS);

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<TaskRespDto> list = taskService.getAll();

        assertEquals(2, list.size());
        assertEquals(1L, list.get(0).taskId());
        assertEquals("test1", list.get(0).title());

        verify(taskRepository, times(1)).findAll();

        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void getByIdTest(){
        Task task = new Task();
        task.setId(1L);
        task.setTitle("testTitle");
        task.setStatus(Status.PENDING);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskRespDto dto = taskService.getById(1L);

        assertEquals(1L, dto.taskId());
        assertEquals("testTitle", dto.title());

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void getByIdTest_Invalid(){
        when(taskRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> taskService.getById(5L));

        verify(taskRepository, times(1)).findById(5L);
    }

    @Test
    public void updateTest(){
        Task task = new Task();
        task.setId(1L);
        task.setTitle("testTitle");
        task.setDescription("testDescription");
        task.setStatus(Status.PENDING);

        TaskUpdateDto dto = new TaskUpdateDto("updatedTitle", null, null, null, Status.COMPLETED);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        taskService.update(1L, dto);

        assertEquals("updatedTitle", task.getTitle());
        assertEquals(Status.COMPLETED, task.getStatus());
        assertEquals("testDescription", task.getDescription());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task);

        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void updateTest_Invalid(){
        TaskUpdateDto dto = new TaskUpdateDto("updatedTitle", null, null, null, null);

        when(taskRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> taskService.update(5L, dto));

        verify(taskRepository, times(1)).findById(5L);
    }

    @Test
    public void updateTest_NoFields(){
        Task task = new Task();
        task.setId(1L);
        task.setTitle("testTitle");

        TaskUpdateDto dto = new TaskUpdateDto(null, null, null, null, null);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(InvalidCallException.class,
                () -> taskService.update(1L, dto));

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void deleteByIdTest(){
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteById(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).deleteById(1L);

        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void deleteByIdTest_Invalid(){
        when(taskRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> taskService.deleteById(5L));

        verify(taskRepository, times(1)).findById(5L);
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    public void dynamicFilterTaskTest(){
        LocalDate from = LocalDate.of(2026, 10, 1);
        LocalDate to = LocalDate.of(2026, 10, 31);

        TaskRespDto dto = new TaskRespDto(1L, "testTitle", "testDescription", LocalDate.of(2026, 10, 30),
                Priority.HIGH, Status.PENDING, Instant.now(), Instant.now());

        when(taskRepository.dynamicFilterTask(Priority.HIGH, Status.PENDING, from, to)).thenReturn(List.of(dto));

        List<TaskRespDto> list = taskService.dynamicFilterTask(Priority.HIGH, Status.PENDING, from, to);

        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).taskId());
        assertEquals("testTitle", list.get(0).title());
        assertEquals(Priority.HIGH, list.get(0).priority());
        assertEquals(Status.PENDING, list.get(0).status());

        verify(taskRepository, times(1)).dynamicFilterTask(Priority.HIGH, Status.PENDING, from, to);

        verifyNoMoreInteractions(taskRepository);
    }
}
