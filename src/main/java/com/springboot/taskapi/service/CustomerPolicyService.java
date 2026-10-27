package com.springboot.taskapi.service;

import com.springboot.taskapi.dto.response.CustomerPolicyRespDto;
import com.springboot.taskapi.enums.Tenure;
import com.springboot.taskapi.exception.InvalidCallException;
import com.springboot.taskapi.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerPolicyService {

    private final PolicyRepository policyRepository;

    public List<CustomerPolicyRespDto> getCustomerPolicies(Tenure tenure,
                                                           String title,
                                                           int page,
                                                           int size) {
        // Step 1: Validate the request params
        if (tenure == null) {
            throw new InvalidCallException("tenure is required");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidCallException("title is required");
        }
        if (page < 0) {
            throw new InvalidCallException("page must be greater than or equal to 0");
        }
        if (size < 1 || size > 100) {
            throw new InvalidCallException("size must be between 1 and 100");
        }

        String policyTitle = title.trim();
        log.info("Fetching customers with policy tenure {} and title {}", tenure, policyTitle);

        // Step 2: Create an Instance of Pageable interface using page and size values thru PageRequest
        Pageable pageable = PageRequest.of(page, size);

        // Step 3: Fetch customers along with their policy details
        return policyRepository.getCustomerPolicies(tenure, policyTitle, pageable);
    }
}
