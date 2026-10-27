package com.springboot.taskapi.controller;

import com.springboot.taskapi.dto.response.CustomerPolicyRespDto;
import com.springboot.taskapi.enums.Tenure;
import com.springboot.taskapi.service.CustomerPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer-policies")
public class CustomerPolicyController {

    private final CustomerPolicyService customerPolicyService;

    @GetMapping("/filter")
    public List<CustomerPolicyRespDto> filterCustomerPolicies(
            @RequestParam(value = "tenure", required = false) Tenure tenure,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return customerPolicyService.getCustomerPolicies(tenure, title, page, size);
    }
}
