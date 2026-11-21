package com.yash.jobtracker.controller;

import com.yash.jobtracker.dto.*;
import com.yash.jobtracker.entity.ApplicationStatus;
import com.yash.jobtracker.service.JobApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="Job Applications",description = "Manage Job Applications and their information")
@RestController
@RequestMapping("/applications")
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService){
        this.jobApplicationService=jobApplicationService;
    }
   @Operation(summary = "Create Job Application",description = "Job Application Created Successfully")
   @ApiResponse(responseCode = "201",description = "Application Created Successfully")
   @ApiResponse(responseCode = "400",description = "Invalid data")
    @PostMapping
    public ResponseEntity<Object> createApplication(@RequestBody @Valid JobApplicationRequest request){
        JobApplicationResponse response=jobApplicationService.createApplication(request);
        APIResponse<JobApplicationResponse> apiResponse=new APIResponse<>(201,"Application Created Successfully",response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);

    }

    @Operation(summary = "Get Job Application by Id",description = "Job Application Retrieved")
    @ApiResponse(responseCode = "200",description = "Application Retrieved Successfully")
    @ApiResponse(responseCode = "404",description = "Application not found")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getApplicationById(@PathVariable Long id){
        JobApplicationResponse response=jobApplicationService.getApplicationById(id);
        APIResponse<JobApplicationResponse> apiResponse=new APIResponse<>(200,"Application Retrieved Successfully",response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Get Job Applications",description = "Job Applications Retrieved")
    @ApiResponse(responseCode = "200",description = "Applications Retrieved Successfully")
    @ApiResponse(responseCode = "400",description = "Invalid pagination or sorting parameters")
    @GetMapping
    public ResponseEntity<Object> getAllApplications(@RequestParam(required = false)ApplicationStatus status,@RequestParam(required = false)Long companyId,@RequestParam(required = false) Integer days,@RequestParam int page, @RequestParam int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction){
        PaginatedResponse responses=jobApplicationService.getAllApplications(page,size,sortBy,direction,status,companyId,days);
        APIResponse<PaginatedResponse> apiResponse=new APIResponse<>(200,"Applications Retrieved Successfully",responses);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Update Job Application By Id",description = "Job Application Updated Successfully")
    @ApiResponse(responseCode = "200",description = "Application Updated Successfully")
    @ApiResponse(responseCode = "400",description = "Invalid data")
    @ApiResponse(responseCode = "404",description = "Application not found")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateApplication(@PathVariable Long id,@RequestBody @Valid JobApplicationRequest request){
        JobApplicationResponse response=jobApplicationService.updateApplication(id,request);
        APIResponse<JobApplicationResponse> apiResponse=new APIResponse<>(200,"Application Updated Successfully",response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Delete Job Application By Id",description = "Job Application Deleted Successfully")
    @ApiResponse(responseCode = "204",description = "Application Deleted Successfully")
    @ApiResponse(responseCode = "404",description = "Application not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteApplication(@PathVariable Long id){
        jobApplicationService.deleteApplication(id);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Update Application Partially",description = "Job Application Partially Updated Successfully")
    @ApiResponse(responseCode = "200",description = "Application Updated Successfully")
    @ApiResponse(responseCode = "400",description = "Invalid data")
    @ApiResponse(responseCode = "404",description = "Application not found")
    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchApplication(@PathVariable Long id, @RequestBody @Valid JobApplicationPatchRequest request){
        JobApplicationResponse response=jobApplicationService.patchApplication(id,request);
        APIResponse<JobApplicationResponse> apiResponse=new APIResponse<>(200,"Application Updated Successfully",response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


}
