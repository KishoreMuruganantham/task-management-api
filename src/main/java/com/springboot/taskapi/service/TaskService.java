package com.springboot.taskapi.service;

import com.springboot.taskapi.dto.request.TaskCreateDto;
import com.springboot.taskapi.dto.request.TaskUpdateDto;
import com.springboot.taskapi.dto.response.TaskRespDto;
import com.springboot.taskapi.enums.Priority;
import com.springboot.taskapi.enums.Status;
import com.springboot.taskapi.exception.InvalidCallException;
import com.springboot.taskapi.exception.ResourceNotFoundException;
import com.springboot.taskapi.mapper.TaskMapper;
import com.springboot.taskapi.model.Task;
import com.springboot.taskapi.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;

    public void add(TaskCreateDto dto) {
        // Step 1: Map TaskCreateDto to Task entity
        Task task = TaskMapper.convertDtoToEntity(dto);
        log.info("Task entity prepared from dto");

        // Step 2: Assign Status as PENDING to Task
        task.setStatus(Status.PENDING);
        log.info("Task status set to PENDING");

        // Step 3: Insert Task in DB
        taskRepository.save(task);
        log.info("Task saved with id {}", task.getId());
    }

    public TaskRespDto getById(Long id) {
        Optional<Task> optional = taskRepository.findById(id);
        if(optional.isEmpty())
            throw new ResourceNotFoundException("Task not found");

        return TaskMapper.convertEntityToDto(optional.get());
    }

    public List<TaskRespDto> getAll() {
        List<Task> list = taskRepository.findAll();
        // Map List<Task> to List<TaskRespDto> using Mapper
        return list
                .stream()
                .map(TaskMapper :: convertEntityToDto)
                .toList();
    }

    public void deleteById(Long id) {
        // Check if id is valid
        getById(id);
        taskRepository.deleteById(id);
    }

    public void update(Long id, TaskUpdateDto dto) {
        Optional<Task> optional = taskRepository.findById(id);
        if(optional.isEmpty())
            throw new ResourceNotFoundException("Task not found");

        Task task = optional.get();
        boolean flag = false;

        if(!(dto.title() == null) && !(dto.title().trim().isEmpty())) {    // If dto.title() is NOT(!) empty
            task.setTitle(dto.title());
            flag = true;
        }

        if(!(dto.description() == null) && !(dto.description().trim().isEmpty())) {
            task.setDescription(dto.description());
            flag = true;
        }

        if(!(dto.dueDate() == null)) {
            task.setDueDate(dto.dueDate());
            flag = true;
        }

        if(!(dto.priority() == null)) {
            task.setPriority(dto.priority());
            flag = true;
        }

        if(!(dto.status() == null)) {
            task.setStatus(dto.status());
            flag = true;
        }

        if(!flag)
            throw new InvalidCallException("Update op terminated");

        taskRepository.save(task);

    }

    public List<TaskRespDto> dynamicFilterTask(Priority priority,
                                               Status status,
                                               LocalDate fromDate,
                                               LocalDate toDate) {

        return taskRepository.dynamicFilterTask(priority,
                                                status,
                                                fromDate,
                                                toDate);
    }
}
