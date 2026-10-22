package com.springboot.taskapi.model;

import com.springboot.taskapi.enums.Priority;
import com.springboot.taskapi.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Task {  //t

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;  //findById

    private String title; // findByTitle(title)
    @Column(length = 1000)
    private String description;
    private LocalDate dueDate; // findByDueDate(dueDate)
    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
    @Enumerated(EnumType.STRING)
    private Priority priority;  // findByPriority(priority) : List<Task>
    @Enumerated(EnumType.STRING)
    private Status status; // findByStatus(status) : List<Task>
}
