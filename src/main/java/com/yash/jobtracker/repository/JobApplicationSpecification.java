package com.yash.jobtracker.repository;

import com.yash.jobtracker.entity.ApplicationStatus;
import com.yash.jobtracker.entity.JobApplication;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
@Component
public class JobApplicationSpecification {

    public Specification<JobApplication> statusEquals(ApplicationStatus status){
        return (root, query, criteriaBuilder) ->{
           return criteriaBuilder.equal(root.get("status"),status);
        };
    }

    public Specification<JobApplication> companyEquals(Long companyId){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("company").get("id"), companyId);
        };
    }
    public Specification<JobApplication> appliedBetween(LocalDate startDate,LocalDate endDate){
        return (root, query, criteriaBuilder) -> {
           Predicate p= criteriaBuilder.greaterThanOrEqualTo(root.get("appliedDate"),startDate);
           Predicate p2=criteriaBuilder.lessThanOrEqualTo(root.get("appliedDate"),endDate);
           return criteriaBuilder.and(p,p2);
        };
    }

}
