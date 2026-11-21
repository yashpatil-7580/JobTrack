package com.yash.jobtracker.dto;

import com.yash.jobtracker.entity.ApplicationStatus;
import java.time.LocalDate;

public class JobApplicationResponse {
    private Long id;
    private String jobTitle;
    private ApplicationStatus status;
    private LocalDate appliedDate;
    private String jobUrl;
    private String notes;
    private Long companyId;
    private String companyName;
    public JobApplicationResponse(){}
    public JobApplicationResponse(Long id,String jobTitle, ApplicationStatus status, LocalDate appliedDate,
                                 String jobUrl, String notes, Long companyId,String companyName){
        this.id=id;
        this.jobTitle=jobTitle;
        this.status=status;
        this.appliedDate=appliedDate;
        this.jobUrl=jobUrl;
        this.notes=notes;
        this.companyId=companyId;
        this.companyName=companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
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
}
