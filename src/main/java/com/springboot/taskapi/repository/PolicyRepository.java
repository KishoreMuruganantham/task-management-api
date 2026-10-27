package com.springboot.taskapi.repository;

import com.springboot.taskapi.dto.response.CustomerPolicyRespDto;
import com.springboot.taskapi.enums.Tenure;
import com.springboot.taskapi.model.Policy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {

    @Query("""
            select new com.springboot.taskapi.dto.response.CustomerPolicyRespDto(
            p.customer.id,
            p.customer.name,
            p.policyTitle,
            p.tenure,
            p.type
            )
            from Policy p
            where p.tenure = ?1 AND p.policyTitle = ?2
            """)
    List<CustomerPolicyRespDto> getCustomerPolicies(Tenure tenure,
                                                    String policyTitle,
                                                    Pageable pageable);
}
