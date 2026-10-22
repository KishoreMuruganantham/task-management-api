package com.springboot.taskapi.dto.response;

import com.springboot.taskapi.enums.Priority;
import com.springboot.taskapi.enums.Status;

import java.time.Instant;
import java.time.LocalDate;

public record TaskRespDto(
        Long taskId,
        String title,
        String description,
        LocalDate dueDate,
        Priority priority,
        Status status,
        Instant createdAt,
        Instant updatedAt
) {
}
