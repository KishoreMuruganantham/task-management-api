package com.springboot.taskapi.mapper;

import com.springboot.taskapi.dto.request.TaskCreateDto;
import com.springboot.taskapi.dto.response.TaskRespDto;
import com.springboot.taskapi.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public static Task convertDtoToEntity(TaskCreateDto dto){
        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setDueDate(dto.dueDate());
        task.setPriority(dto.priority());
        return task;
    }

    public static TaskRespDto convertEntityToDto(Task task){
        return
                new TaskRespDto (
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getDueDate(),
                        task.getPriority(),
                        task.getStatus(),
                        task.getCreatedAt(),
                        task.getUpdatedAt()
                );
    }
}
