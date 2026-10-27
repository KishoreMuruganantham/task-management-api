package com.springboot.taskapi.dto.response;

import com.springboot.taskapi.enums.PolicyType;
import com.springboot.taskapi.enums.Tenure;

public record CustomerPolicyRespDto(
        Long customerId,
        String customerName,
        String policyTitle,
        Tenure tenure,
        PolicyType type
) {
}
