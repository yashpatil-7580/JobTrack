package com.yash.jobtracker.repository;

import com.yash.jobtracker.entity.ApplicationStatus;
import com.yash.jobtracker.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobApplicationRepository extends JpaRepository<JobApplication,Long>, JpaSpecificationExecutor<JobApplication> {
     Page<JobApplication> findByStatus(ApplicationStatus status,Pageable pageable);
     Page<JobApplication> findByCompanyId(Long companyId,Pageable pageable);
     Page<JobApplication> findByStatusAndCompanyId(ApplicationStatus status,Long companyId,Pageable pageable);

}
