package com.springboot.taskapi.model;

import com.springboot.taskapi.enums.JobTitle;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@NoArgsConstructor 
@AllArgsConstructor 
@Getter
@Setter
@ToString(exclude = "company")
@Entity
public class Employee {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private JobTitle jobTitle;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
