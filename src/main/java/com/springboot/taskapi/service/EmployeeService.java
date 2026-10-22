package com.springboot.taskapi.service;

import com.springboot.taskapi.dto.response.EmployeeRespDto;
import com.springboot.taskapi.enums.JobTitle;
import com.springboot.taskapi.exception.InvalidCallException;
import com.springboot.taskapi.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<EmployeeRespDto> getEmployees(JobTitle jobTitle,
                                              String companyName,
                                              int page,
                                              int size) {
        // Step 1: Validate the request params
        if (jobTitle == null) {
            throw new InvalidCallException("jobTitle is required");
        }
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new InvalidCallException("company_name is required");
        }
        if (page < 0) {
            throw new InvalidCallException("page must be greater than or equal to 0");
        }
        if (size < 1 || size > 100) {
            throw new InvalidCallException("size must be between 1 and 100");
        }

        log.info("Fetching employees with jobTitle {} for company {}", jobTitle, companyName);

        // Step 2: Create an Instance of Pageable interface using page and size values thru PageRequest
        Pageable pageable = PageRequest.of(page, size);

        // Step 3: Fetch employees by jobTitle and company name
        return employeeRepository.getEmployeesByJobTitleAndCompanyName(jobTitle, companyName, pageable);
    }
}
