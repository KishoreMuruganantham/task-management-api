package com.springboot.taskapi.repository;

import com.springboot.taskapi.dto.response.EmployeeRespDto;
import com.springboot.taskapi.enums.JobTitle;
import com.springboot.taskapi.model.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
            select new com.springboot.taskapi.dto.response.EmployeeRespDto(
            e.name,
            e.jobTitle,
            c.name,
            c.status
            )
            from Employee e
            JOIN e.company c
            where e.jobTitle = ?1 AND c.name = ?2
            """)
    List<EmployeeRespDto> getEmployeesByJobTitleAndCompanyName(JobTitle jobTitle,
                                                               String companyName,
                                                               Pageable pageable);
}
