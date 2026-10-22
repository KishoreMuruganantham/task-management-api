package com.springboot.taskapi.controller;

import com.springboot.taskapi.dto.response.EmployeeRespDto;
import com.springboot.taskapi.enums.JobTitle;
import com.springboot.taskapi.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/api/employees")
    public List<EmployeeRespDto> getEmployees(
            @RequestParam(value = "jobTitle", required = false) JobTitle jobTitle,
            @RequestParam(value = "company_name", required = false) String companyName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return employeeService.getEmployees(jobTitle, companyName, page, size);
    }
}
