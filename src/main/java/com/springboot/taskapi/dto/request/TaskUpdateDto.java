package com.springboot.taskapi.dto.request;

import com.springboot.taskapi.enums.Priority;
import com.springboot.taskapi.enums.Status;

import java.time.LocalDate;

public record TaskUpdateDto(
        String title,
        String description,
        LocalDate dueDate,
        Priority priority,
        Status status
) {
}
