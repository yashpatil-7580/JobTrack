package com.yash.jobtracker.service;

import com.yash.jobtracker.dto.JobApplicationPatchRequest;
import com.yash.jobtracker.dto.JobApplicationRequest;
import com.yash.jobtracker.dto.JobApplicationResponse;
import com.yash.jobtracker.dto.PaginatedResponse;
import com.yash.jobtracker.entity.ApplicationStatus;
import com.yash.jobtracker.entity.Company;
import com.yash.jobtracker.entity.JobApplication;
import com.yash.jobtracker.exception.CompanyNotFoundException;
import com.yash.jobtracker.exception.JobApplicationNotFound;
import com.yash.jobtracker.repository.CompanyRepository;
import com.yash.jobtracker.repository.JobApplicationRepository;
import com.yash.jobtracker.repository.JobApplicationSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.Set;


@Service
public class JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;
    private final CompanyRepository companyRepository;
    private final JobApplicationSpecification applicationSpecification;
    public JobApplicationService(JobApplicationRepository jobApplicationRepository,
                                 CompanyRepository companyRepository,JobApplicationSpecification applicationSpecification){
        this.jobApplicationRepository=jobApplicationRepository;
        this.companyRepository=companyRepository;
        this.applicationSpecification=applicationSpecification;
    }

    public JobApplicationResponse createApplication(JobApplicationRequest request){
        Company company=companyRepository.findById(request.getCompanyId())
                .orElseThrow(()->new CompanyNotFoundException("Company with Id " + request.getCompanyId() + " Not Found"));
         JobApplication application=mapToJobApplication(request,company);
         JobApplication savedApplication=jobApplicationRepository.save(application);
         JobApplicationResponse response=mapToResponse(savedApplication);
         return response;
    }

    public JobApplicationResponse getApplicationById(Long id){
        JobApplication application=jobApplicationRepository.findById(id)
                .orElseThrow(()->new JobApplicationNotFound("Job Application not found with Id "+id));
        JobApplicationResponse response=mapToResponse(application);
        return response;
    }

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id",
            "jobTitle",
            "status",
            "appliedDate",
            "jobUrl",
            "notes"
    );
    public PaginatedResponse getAllApplications(int page, int size, String sortBy, String direction, ApplicationStatus status,Long companyId,Integer days) {
        if(page<0){
            throw new IllegalArgumentException("Page must be 0 or greater");
        }
        if(size<1 || size >100){
            throw new IllegalArgumentException("Size must be between 1 and 100");
        }
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }
        if(!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")){
            throw new IllegalArgumentException("Direction must be either ascending or descending");
        }
       if( companyId!=null &&  companyId<=0 ){
           throw new IllegalArgumentException("Company Id must be greater than 0");
       }
       if( days!=null && days<=0 ){
           throw new IllegalArgumentException("Days must be greater than 0");
       }
        Sort.Direction sortDirection=Sort.Direction.fromString(direction);
        Sort sort=Sort.by(sortDirection,sortBy);

        Pageable pageable= PageRequest.of(page,size,sort);
        Specification<JobApplication> spec=Specification.unrestricted();
        Page<JobApplication> applications;

        if(status!=null){
            spec=spec.and(applicationSpecification.statusEquals(status));
        }
        if(companyId!=null){
            spec=spec.and(applicationSpecification.companyEquals(companyId));
        }
        if(days!=null){
            LocalDate endDate=LocalDate.now();
            LocalDate startDate=endDate.minusDays(days);
            spec=spec.and(applicationSpecification.appliedBetween(startDate,endDate));
        }
          applications=jobApplicationRepository.findAll(spec,pageable);
        Page<JobApplicationResponse> responses=applications.map(application->mapToResponse(application));
        PaginatedResponse paginatedResponse=new PaginatedResponse(responses.getContent(),responses.getNumber(),
                responses.getSize(),responses.getTotalElements(),responses.getTotalPages(),responses.isFirst(),responses.isLast());
        return paginatedResponse;
    }

    public JobApplicationResponse updateApplication(Long id,JobApplicationRequest request){
        JobApplication application=jobApplicationRepository.findById(id)
                .orElseThrow(()->new JobApplicationNotFound("Job Application not found with Id "+id));

        Company company=companyRepository.findById(request.getCompanyId())
                .orElseThrow(()->new CompanyNotFoundException("Company not found with Id "+request.getCompanyId()));

        application.setJobUrl(request.getJobUrl());
        application.setCompany(company);
        application.setAppliedDate(request.getAppliedDate());
        application.setNotes(request.getNotes());
        application.setStatus(request.getStatus());
        application.setJobTitle(request.getJobTitle());
        JobApplication savedApplication=jobApplicationRepository.save(application);
        JobApplicationResponse response=mapToResponse(savedApplication);
        return response;
    }

    public void deleteApplication(Long id){
        JobApplication application=jobApplicationRepository.findById(id)
                .orElseThrow(()->new JobApplicationNotFound("Job Application not found with Id "+id));
           jobApplicationRepository.delete(application);

    }

    public JobApplicationResponse patchApplication(Long id, JobApplicationPatchRequest request){
        JobApplication application=jobApplicationRepository.findById(id)
                .orElseThrow(()->new JobApplicationNotFound("Job Application not found with Id "+id));


        if(request.getJobTitle()!=null){
            application.setJobTitle(request.getJobTitle());
        }
        if(request.getStatus()!=null){
            application.setStatus(request.getStatus());
        }
        if(request.getAppliedDate()!=null){
            application.setAppliedDate(request.getAppliedDate());
        }
        if(request.getJobUrl()!=null){
            application.setJobUrl(request.getJobUrl());
        }
        if(request.getNotes()!=null){
            application.setNotes(request.getNotes());
        }
        if(request.getCompanyId()!=null){
            Company company=companyRepository.findById(request.getCompanyId())
                    .orElseThrow(()->new CompanyNotFoundException("Company not found with Id "+request.getCompanyId()));

            application.setCompany(company);
        }
        JobApplication savedApplication=jobApplicationRepository.save(application);
        JobApplicationResponse response=mapToResponse(savedApplication);
        return response;
    }

    private JobApplication mapToJobApplication(JobApplicationRequest request, Company company){
           JobApplication jobApplication=new JobApplication();
           jobApplication.setJobTitle(request.getJobTitle());
           jobApplication.setStatus(request.getStatus());
           jobApplication.setNotes(request.getNotes());
           jobApplication.setAppliedDate(request.getAppliedDate());
           jobApplication.setCompany(company);
           jobApplication.setJobUrl(request.getJobUrl());

           return jobApplication;
    }

    private JobApplicationResponse mapToResponse(JobApplication application){
        JobApplicationResponse response=new JobApplicationResponse();
        response.setId(application.getId());
        response.setAppliedDate(application.getAppliedDate());
        response.setCompanyId(application.getCompany().getId());
        response.setCompanyName(application.getCompany().getName());
        response.setJobTitle(application.getJobTitle());
        response.setNotes(application.getNotes());
        response.setJobUrl(application.getJobUrl());
        response.setStatus(application.getStatus());
        return response;

    }

}
