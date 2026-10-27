package com.springboot.taskapi.service;

import com.springboot.taskapi.dto.response.CustomerPolicyRespDto;
import com.springboot.taskapi.enums.PolicyType;
import com.springboot.taskapi.enums.Tenure;
import com.springboot.taskapi.exception.InvalidCallException;
import com.springboot.taskapi.repository.PolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerPolicyServiceTest {

    @InjectMocks
    private CustomerPolicyService customerPolicyService;

    @Mock
    private PolicyRepository policyRepository;

    @Test
    public void getCustomerPoliciesTest() {
        CustomerPolicyRespDto dto1 = new CustomerPolicyRespDto(
                1L, "Alice", "Health Shield", Tenure.ONE_YEAR, PolicyType.COMPREHENSIVE);
        CustomerPolicyRespDto dto2 = new CustomerPolicyRespDto(
                2L, "Bob", "Health Shield", Tenure.ONE_YEAR, PolicyType.THIRD_PARTY);

        when(policyRepository.getCustomerPolicies(
                Tenure.ONE_YEAR, "Health Shield", PageRequest.of(0, 5)))
                .thenReturn(List.of(dto1, dto2));

        List<CustomerPolicyRespDto> result = customerPolicyService.getCustomerPolicies(
                Tenure.ONE_YEAR, "Health Shield", 0, 5);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).customerId());
        assertEquals("Alice", result.get(0).customerName());
        assertEquals("Health Shield", result.get(0).policyTitle());
        assertEquals(Tenure.ONE_YEAR, result.get(0).tenure());
        assertEquals(PolicyType.COMPREHENSIVE, result.get(0).type());

        verify(policyRepository, times(1))
                .getCustomerPolicies(Tenure.ONE_YEAR, "Health Shield", PageRequest.of(0, 5));
        verifyNoMoreInteractions(policyRepository);
    }

    @Test
    public void getCustomerPoliciesSecondPageTest() {
        CustomerPolicyRespDto dto = new CustomerPolicyRespDto(
                2L, "Bob", "Health Shield", Tenure.ONE_YEAR, PolicyType.THIRD_PARTY);

        when(policyRepository.getCustomerPolicies(
                Tenure.ONE_YEAR, "Health Shield", PageRequest.of(1, 1)))
                .thenReturn(List.of(dto));

        List<CustomerPolicyRespDto> result = customerPolicyService.getCustomerPolicies(
                Tenure.ONE_YEAR, "Health Shield", 1, 1);

        assertEquals(1, result.size());
        assertEquals("Bob", result.get(0).customerName());

        verify(policyRepository, times(1))
                .getCustomerPolicies(Tenure.ONE_YEAR, "Health Shield", PageRequest.of(1, 1));
        verifyNoMoreInteractions(policyRepository);
    }

    @Test
    public void getCustomerPoliciesMissingTenureTest() {
        assertThrows(InvalidCallException.class,
                () -> customerPolicyService.getCustomerPolicies(null, "Health Shield", 0, 5));

        verifyNoInteractions(policyRepository);
    }

    @Test
    public void getCustomerPoliciesMissingTitleTest() {
        assertThrows(InvalidCallException.class,
                () -> customerPolicyService.getCustomerPolicies(Tenure.ONE_YEAR, "   ", 0, 5));

        verifyNoInteractions(policyRepository);
    }

    @Test
    public void getCustomerPoliciesInvalidPageTest() {
        assertThrows(InvalidCallException.class,
                () -> customerPolicyService.getCustomerPolicies(Tenure.ONE_YEAR, "Health Shield", -1, 5));

        verifyNoInteractions(policyRepository);
    }

    @Test
    public void getCustomerPoliciesInvalidSizeTest() {
        assertThrows(InvalidCallException.class,
                () -> customerPolicyService.getCustomerPolicies(Tenure.ONE_YEAR, "Health Shield", 0, 0));

        assertThrows(InvalidCallException.class,
                () -> customerPolicyService.getCustomerPolicies(Tenure.ONE_YEAR, "Health Shield", 0, 101));

        verifyNoInteractions(policyRepository);
    }
}
