package com.springboot.taskapi.dto.request;

import com.springboot.taskapi.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskCreateDto(
        @NotBlank(message = "Title is required")
        @NotNull(message = "Title is required")
        String title,
        String description,
        @NotNull(message = "Due date is required")
        LocalDate dueDate,
        @NotNull(message = "Priority is required")
        Priority priority
) {
}
