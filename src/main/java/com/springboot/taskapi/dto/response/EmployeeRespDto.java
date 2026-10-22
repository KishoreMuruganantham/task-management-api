package com.springboot.taskapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.taskapi.enums.CompanyStatus;
import com.springboot.taskapi.enums.JobTitle;

public record EmployeeRespDto(
        @JsonProperty("employee_name")
        String employeeName,
        JobTitle jobTitle,
        @JsonProperty("company_name")
        String companyName,
        CompanyStatus status
) {
}
