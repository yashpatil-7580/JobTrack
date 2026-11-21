package com.yash.jobtracker.repository;

import com.yash.jobtracker.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {

}
