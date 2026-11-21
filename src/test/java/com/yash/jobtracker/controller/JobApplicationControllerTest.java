package com.yash.jobtracker.controller;

import com.yash.jobtracker.dto.*;
import com.yash.jobtracker.entity.ApplicationStatus;
import com.yash.jobtracker.exception.CompanyNotFoundException;
import com.yash.jobtracker.exception.JobApplicationNotFound;
import com.yash.jobtracker.service.JobApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import javax.net.ssl.SSLEngineResult;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobApplicationController.class)
public class JobApplicationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JobApplicationService jobApplicationService;


    @Test
    public void getAllApplication() throws Exception {
        JobApplicationResponse response=new JobApplicationResponse();
         response.setId(1L);
         response.setStatus(ApplicationStatus.APPLIED);
         response.setJobUrl("www.tcs.com");
         response.setNotes("Applied to job via external site");
         response.setJobTitle("Java Developer");
         response.setCompanyName("TCS");
         response.setCompanyId(1L);
         response.setAppliedDate(LocalDate.now());
         List<JobApplicationResponse> responseList=new ArrayList<>();
         responseList.add(response);
        PaginatedResponse paginatedResponse=new PaginatedResponse(responseList,0,1,1,1,true,true);
        when(jobApplicationService.getAllApplications(0,5,"id","asc",null,null,null)).thenReturn(paginatedResponse);

        //Act
        mockMvc.perform(get("/applications").param("page","0").param("size","5")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("Applications Retrieved Successfully"))
                        .andExpect(jsonPath("$.data.applications[0].companyName").value("TCS"))
                                .andExpect(jsonPath("$.data.applications[0].jobUrl").value("www.tcs.com"))
                                        .andExpect(jsonPath("$.data.applications[0].status").value("APPLIED"))
                                                .andExpect(jsonPath("$.data.applications[0].companyId").value(1));

        //verify
        verify(jobApplicationService,times(1)).getAllApplications(0,5,"id","asc",null,null,null);
    }

    @Test
    public void getApplicationById() throws Exception {
        //Arrange
        JobApplicationResponse response=new JobApplicationResponse();
        response.setId(1L);
        response.setStatus(ApplicationStatus.APPLIED);
        response.setJobUrl("www.tcs.com");
        response.setNotes("Applied to job via external site");
        response.setJobTitle("Java Developer");
        response.setCompanyName("TCS");
        response.setCompanyId(1L);
        when(jobApplicationService.getApplicationById(1L)).thenReturn(response);

        //Act
         mockMvc.perform(get("/applications/1")).
                 andExpect(status().isOk())
                 .andExpect(jsonPath("$.message").value("Application Retrieved Successfully"))
                 .andExpect(jsonPath("$.data.companyName").value("TCS"));

         //verify
        verify(jobApplicationService,times(1)).getApplicationById(1L);
    }

    @Test
    public void createApplicationSuccessfully() throws Exception {
        JobApplicationRequest request=new JobApplicationRequest("Java developer",ApplicationStatus.APPLIED,LocalDate.now(),"www.tcs.com","Applied through career page",1L);
        JobApplicationResponse response=new JobApplicationResponse();
        response.setId(1L);
        response.setStatus(request.getStatus());
        response.setJobUrl(request.getJobUrl());
        response.setNotes(request.getNotes());
        response.setJobTitle(request.getJobTitle());
        response.setCompanyName("TCS");
        response.setCompanyId(request.getCompanyId());
        when(jobApplicationService.createApplication(any(JobApplicationRequest.class))).thenReturn(response);

        //Act
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        mockMvc.perform(post("/applications").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Application Created Successfully"))
                .andExpect(jsonPath("$.data.companyName").value("TCS"));

        verify(jobApplicationService,times(1)).createApplication(any(JobApplicationRequest.class));
    }

    @Test
    public void applicationUpdatedSuccessfully() throws Exception {
        JobApplicationRequest request=new JobApplicationRequest("Java developer",ApplicationStatus.APPLIED,LocalDate.now(),"www.tcs.com","Applied through career page",1L);
        JobApplicationResponse response=new JobApplicationResponse();
        response.setId(1L);
        response.setStatus(request.getStatus());
        response.setJobUrl(request.getJobUrl());
        response.setNotes(request.getNotes());
        response.setJobTitle(request.getJobTitle());
        response.setCompanyName("TCS");
        response.setCompanyId(request.getCompanyId());
        when(jobApplicationService.updateApplication(eq(1L),any(JobApplicationRequest.class))).thenReturn(response);

        //Act
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        mockMvc.perform(put("/applications/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application Updated Successfully"))
                .andExpect(jsonPath("$.data.companyName").value("TCS"));

        verify(jobApplicationService,times(1)).updateApplication(eq(1L),any(JobApplicationRequest.class));
    }
    @Test
    public void applicationDeleteByIdSuccessfully() throws Exception {
        doNothing().when(jobApplicationService).deleteApplication(1L);

        mockMvc.perform(delete("/applications/1")).andExpect(status().isNoContent());
        verify(jobApplicationService,times(1)).deleteApplication(1L);
    }
    @Test
    public void patchApplicationSuccessfully() throws Exception {
        JobApplicationPatchRequest request=new JobApplicationPatchRequest("Python Developer",ApplicationStatus.INTERVIEW,null,null,null,null);
        JobApplicationResponse response=new JobApplicationResponse();
        response.setId(1L);
        response.setStatus(request.getStatus());
        response.setJobUrl("www.tcs.com");
        response.setNotes("Applied through career page");
        response.setJobTitle(request.getJobTitle());
        response.setCompanyName("TCS");
        response.setCompanyId(2L);
        when(jobApplicationService.patchApplication(eq(1L),any(JobApplicationPatchRequest.class))).thenReturn(response);

        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        mockMvc.perform(patch("/applications/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application Updated Successfully"))
                .andExpect(jsonPath("$.data.companyName").value("TCS"));

        verify(jobApplicationService,times(1)).patchApplication(eq(1L),any(JobApplicationPatchRequest.class));

    }

    @Test
    public void getAllApplicationWithAllFiltersAndPagination() throws Exception {
        JobApplicationResponse response=new JobApplicationResponse();
        response.setId(1L);
        response.setStatus(ApplicationStatus.APPLIED);
        response.setJobUrl("www.tcs.com");
        response.setNotes("Applied to job via external site");
        response.setJobTitle("Java Developer");
        response.setCompanyName("TCS");
        response.setCompanyId(1L);
        response.setAppliedDate(LocalDate.now());
        List<JobApplicationResponse> responseList=new ArrayList<>();
        responseList.add(response);
        PaginatedResponse paginatedResponse=new PaginatedResponse(responseList,0,1,1,1,true,true);
        when(jobApplicationService.getAllApplications(0,5,"id","asc",ApplicationStatus.APPLIED,1L,7)).thenReturn(paginatedResponse);

        //Act
        mockMvc.perform(get("/applications").param("status", "APPLIED")
                .param("companyId", "1")
                .param("days", "7")
                .param("page", "0")
                .param("size", "5")
                .param("sortBy", "id")
                .param("direction", "asc")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("Applications Retrieved Successfully"))
                .andExpect(jsonPath("$.data.applications[0].companyName").value("TCS"))
                .andExpect(jsonPath("$.data.applications[0].jobUrl").value("www.tcs.com"))
                .andExpect(jsonPath("$.data.applications[0].status").value("APPLIED"))
                .andExpect(jsonPath("$.data.applications[0].companyId").value(1));

        //verify
        verify(jobApplicationService,times(1)).getAllApplications(0,5,"id","asc",ApplicationStatus.APPLIED,1L,7);

    }

    @Test
    public void createApplicationWithInvalidRequest() throws Exception {
        //Arrange
        JobApplicationRequest request=new JobApplicationRequest("  ",ApplicationStatus.APPLIED,LocalDate.now(),"www.tcs.com","Applied through career page",1L);
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        //Act and Assert
        mockMvc.perform(post("/applications").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation Error"));
        //verify
        verify(jobApplicationService,never()).createApplication(any(JobApplicationRequest.class));
    }

    @Test
    public void patchApplicationWithInvalidRequest() throws Exception {
        //Arrange
        JobApplicationPatchRequest request=new JobApplicationPatchRequest("Java developer",ApplicationStatus.INTERVIEW,null,null,null,null);
        when(jobApplicationService.patchApplication(eq(100L),any(JobApplicationPatchRequest.class))).thenThrow(new JobApplicationNotFound("Application with Id 100 not found"));
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        //Act and Assert
        mockMvc.perform(patch("/applications/100L").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
        //verify
        verify(jobApplicationService,never()).patchApplication(eq(100L),any(JobApplicationPatchRequest.class));
    }

    @Test
    public void getApplicationByIdWhenNotFound() throws Exception {
        when(jobApplicationService.getApplicationById(100L)).thenThrow(new JobApplicationNotFound("Application with Id 100 not found"));
        mockMvc.perform(get("/applications/100")).andExpect(status().isNotFound());
        //verify
        verify(jobApplicationService,times(1)).getApplicationById(100L);
    }

    @Test
    public void DeleteApplicationByIdWhenNotFound() throws Exception {
        doNothing().when(jobApplicationService).deleteApplication(100L);
        mockMvc.perform(delete("/applications/100")).andExpect(status().isNoContent());
        //verify
        verify(jobApplicationService,times(1)).deleteApplication(100L);
    }
    @Test
    public void putApplicationByIdWhenNotFound() throws Exception {
        JobApplicationRequest request=new JobApplicationRequest("Java developer",ApplicationStatus.APPLIED,LocalDate.now(),"www.tcs.com","Applied through career page",1L);
        when(jobApplicationService.updateApplication(eq(100L),any(JobApplicationRequest.class))).thenThrow(new JobApplicationNotFound("Application with Id 100 not found"));
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        mockMvc.perform(put("/applications/100").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isNotFound());
        //verify
        verify(jobApplicationService,times(1)).updateApplication(eq(100L),any(JobApplicationRequest.class));
    }

    @Test
    public void patchApplicationWithIdNotFound() throws Exception {
        //Arrange
        JobApplicationPatchRequest request=new JobApplicationPatchRequest("Java developer",ApplicationStatus.INTERVIEW,null,null,null,null);
        when(jobApplicationService.patchApplication(eq(100L),any(JobApplicationPatchRequest.class))).thenThrow(new JobApplicationNotFound("Application with Id 100 not found"));
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        //Act and Assert
        mockMvc.perform(patch("/applications/100").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
        //verify
        verify(jobApplicationService,times(1)).patchApplication(eq(100L),any(JobApplicationPatchRequest.class));
    }
}
