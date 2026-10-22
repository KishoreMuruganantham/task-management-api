package com.springboot.taskapi.service;

import com.springboot.taskapi.dto.response.EmployeeRespDto;
import com.springboot.taskapi.enums.CompanyStatus;
import com.springboot.taskapi.enums.JobTitle;
import com.springboot.taskapi.exception.InvalidCallException;
import com.springboot.taskapi.repository.EmployeeRepository;
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
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    public void getEmployeesTest() {
        EmployeeRespDto dto1 = new EmployeeRespDto("Alice", JobTitle.DEV, "Hexaware", CompanyStatus.ACTIVE);
        EmployeeRespDto dto2 = new EmployeeRespDto("Frank", JobTitle.DEV, "Hexaware", CompanyStatus.ACTIVE);

        when(employeeRepository.getEmployeesByJobTitleAndCompanyName(
                JobTitle.DEV, "Hexaware", PageRequest.of(0, 5)))
                .thenReturn(List.of(dto1, dto2));

        List<EmployeeRespDto> result = employeeService.getEmployees(JobTitle.DEV, "Hexaware", 0, 5);

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).employeeName());
        assertEquals(JobTitle.DEV, result.get(0).jobTitle());
        assertEquals("Hexaware", result.get(0).companyName());
        assertEquals(CompanyStatus.ACTIVE, result.get(0).status());

        verify(employeeRepository, times(1))
                .getEmployeesByJobTitleAndCompanyName(JobTitle.DEV, "Hexaware", PageRequest.of(0, 5));
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    public void getEmployeesSecondPageTest() {
        EmployeeRespDto dto = new EmployeeRespDto("Grace", JobTitle.DEV, "Hexaware", CompanyStatus.ACTIVE);

        when(employeeRepository.getEmployeesByJobTitleAndCompanyName(
                JobTitle.DEV, "Hexaware", PageRequest.of(1, 2)))
                .thenReturn(List.of(dto));

        List<EmployeeRespDto> result = employeeService.getEmployees(JobTitle.DEV, "Hexaware", 1, 2);

        assertEquals(1, result.size());
        assertEquals("Grace", result.get(0).employeeName());

        verify(employeeRepository, times(1))
                .getEmployeesByJobTitleAndCompanyName(JobTitle.DEV, "Hexaware", PageRequest.of(1, 2));
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    public void getEmployeesMissingJobTitleTest() {
        assertThrows(InvalidCallException.class,
                () -> employeeService.getEmployees(null, "Hexaware", 0, 5));

        verifyNoInteractions(employeeRepository);
    }

    @Test
    public void getEmployeesMissingCompanyNameTest() {
        assertThrows(InvalidCallException.class,
                () -> employeeService.getEmployees(JobTitle.DEV, "   ", 0, 5));

        verifyNoInteractions(employeeRepository);
    }

    @Test
    public void getEmployeesInvalidPageTest() {
        assertThrows(InvalidCallException.class,
                () -> employeeService.getEmployees(JobTitle.DEV, "Hexaware", -1, 5));

        verifyNoInteractions(employeeRepository);
    }

    @Test
    public void getEmployeesInvalidSizeTest() {
        assertThrows(InvalidCallException.class,
                () -> employeeService.getEmployees(JobTitle.DEV, "Hexaware", 0, 0));

        assertThrows(InvalidCallException.class,
                () -> employeeService.getEmployees(JobTitle.DEV, "Hexaware", 0, 101));

        verifyNoInteractions(employeeRepository);
    }
}
