package com.springboot.taskapi.controller;

import com.springboot.taskapi.dto.request.TaskCreateDto;
import com.springboot.taskapi.dto.request.TaskUpdateDto;
import com.springboot.taskapi.dto.response.TaskRespDto;
import com.springboot.taskapi.enums.Priority;
import com.springboot.taskapi.enums.Status;
import com.springboot.taskapi.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/api/tasks")
    public void postTask(@Valid @RequestBody TaskCreateDto taskDto){
        taskService.add(taskDto);
    }

    @GetMapping("/api/tasks/{id}")
    public TaskRespDto getById(@PathVariable Long id){
        return taskService.getById(id);
    }

    @GetMapping("/api/tasks")
    public List<TaskRespDto> getAll(){
        return taskService.getAll();
    }

    @DeleteMapping("/api/tasks/{id}")
    public void deleteById(@PathVariable Long id){
        taskService.deleteById(id);
    }

    @PutMapping("/api/tasks/{id}")
    public void update(@PathVariable Long id,
                       @RequestBody TaskUpdateDto dto){
        taskService.update(id, dto);
    }

    @GetMapping("/api/tasks/filter")
    public List<TaskRespDto> dynamicFilterTask(@RequestParam(value = "priority", required = false) Priority priority,
                                               @RequestParam(value = "status", required = false) Status status,
                                               @RequestParam(value = "fromDate", required = false) LocalDate fromDate,
                                               @RequestParam(value = "toDate", required = false) LocalDate toDate){
        return taskService.dynamicFilterTask(priority, status, fromDate, toDate);
    }
}
