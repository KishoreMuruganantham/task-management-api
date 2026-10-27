package com.springboot.taskapi.model;

import com.springboot.taskapi.enums.PolicyType;
import com.springboot.taskapi.enums.Tenure;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "customer")
@Entity
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String policyTitle;

    @Enumerated(EnumType.STRING)
    private Tenure tenure;

    @Enumerated(EnumType.STRING)
    private PolicyType type;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
