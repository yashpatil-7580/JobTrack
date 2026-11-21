package com.yash.jobtracker.dto;

import com.yash.jobtracker.entity.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class JobApplicationPatchRequest {
    private String jobTitle;
    private ApplicationStatus status;
    private LocalDate appliedDate;
    private String jobUrl;
    private String notes;
    private Long companyId;
    public JobApplicationPatchRequest(){}
    public JobApplicationPatchRequest(String jobTitle, ApplicationStatus status, LocalDate appliedDate,
                                 String jobUrl, String notes, Long companyId){
        this.jobTitle=jobTitle;
        this.status=status;
        this.appliedDate=appliedDate;
        this.jobUrl=jobUrl;
        this.notes=notes;
        this.companyId=companyId;
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
