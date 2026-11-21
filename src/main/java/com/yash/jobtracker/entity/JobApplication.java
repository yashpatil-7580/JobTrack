package com.yash.jobtracker.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "job_application")
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobTitle;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    private LocalDate appliedDate;
    private String jobUrl;
    private String notes;
    @ManyToOne
    @JoinColumn(name = "company_id",nullable = false)
    private Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public JobApplication(){}
    public JobApplication(String jobTitle,ApplicationStatus status,LocalDate appliedDate,
                          String jobUrl,String notes,Company company){
        this.jobTitle=jobTitle;
        this.status=status;
        this.appliedDate=appliedDate;
        this.jobUrl=jobUrl;
        this.notes=notes;
        this.company=company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
