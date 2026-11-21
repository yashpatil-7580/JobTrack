package com.yash.jobtracker.integration;

import com.yash.jobtracker.dto.CompanyPatchRequest;
import com.yash.jobtracker.dto.CompanyRequest;
import com.yash.jobtracker.dto.JobApplicationPatchRequest;
import com.yash.jobtracker.dto.JobApplicationRequest;
import com.yash.jobtracker.entity.ApplicationStatus;
import com.yash.jobtracker.entity.Company;
import com.yash.jobtracker.entity.JobApplication;
import com.yash.jobtracker.repository.CompanyRepository;
import com.yash.jobtracker.repository.JobApplicationRepository;
import com.yash.jobtracker.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class JobApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Test
    void contextLoad(){
    }

    @Test
    void createCompanySuccessfully()throws Exception{
        CompanyRequest request=new CompanyRequest("Infosys","Pune","www.infosys.com");
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        //Act
        mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Data Inserted Successfully"))
                .andExpect(jsonPath("$.data.name").value("Infosys"));

        //verify
        Optional<Company> company1 =companyRepository.findAll().stream().filter(company -> company.getName().equals("Infosys")).findFirst();
         assertTrue(company1.isPresent());
         Company savedComapany=company1.get();
         assertEquals("Infosys",savedComapany.getName());
         assertEquals("www.infosys.com",savedComapany.getWebsite());
         assertEquals("Pune",savedComapany.getLocation());
    }

    @Test
    void getAllCompaniesSuccessfully() throws Exception {
        Company company=new Company("TCS","www.tcs.com","Mumbai");
        companyRepository.save(company);
        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Companies Retrieved Successfully"))
                .andExpect(jsonPath("$.data[0].name").value("TCS"))
                .andExpect(jsonPath("$.data[0].location").value("Mumbai"))
                .andExpect(jsonPath("$.data[0].website").value("www.tcs.com"));

    }

    @Test
    void getCompanyByIdSuccessfully() throws Exception {
        Company company=new Company("TCS","www.tcs.com","Mumbai");
        Company savedCompany=companyRepository.save(company);
        mockMvc.perform(get("/companies/"+savedCompany.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Company Retrieved Successfully"))
                .andExpect(jsonPath("$.data.name").value("TCS"))
                .andExpect(jsonPath("$.data.location").value("Mumbai"))
                .andExpect(jsonPath("$.data.website").value("www.tcs.com"));


    }

    @Test
    void updateCompanySuccessfully() throws Exception {
        Company company=new Company("TCS","Mumbai","www.tcs.com");
        Company savedCompany=companyRepository.save(company);
        CompanyRequest request=new CompanyRequest("TCS Technologies","Pune","www.tcs-tech.com");
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        mockMvc.perform(put("/companies/"+savedCompany.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Company Updated Successfully"));

       Optional<Company> company1=companyRepository.findById(savedCompany.getId());

        assertTrue(company1.isPresent());
        Company savedComapany=company1.get();
        assertEquals("TCS Technologies",savedComapany.getName());
        assertEquals("www.tcs-tech.com",savedComapany.getWebsite());
        assertEquals("Pune",savedComapany.getLocation());

    }

    @Test
    void patchCompanySuccessfully() throws Exception {
        Company company=new Company("TCS","www.tcs.com","Mumbai");
        Company savedCompany=companyRepository.save(company);
        CompanyPatchRequest request=new CompanyPatchRequest();
        request.setName("TCS Technologies");
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        mockMvc.perform(patch("/companies/"+savedCompany.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Company Updated Successfully"));

        Optional<Company> company1=companyRepository.findById(savedCompany.getId());

        assertTrue(company1.isPresent());
        Company savedComapany=company1.get();
        assertEquals("TCS Technologies",savedComapany.getName());
        assertEquals("www.tcs.com",savedComapany.getWebsite());
        assertEquals("Mumbai",savedComapany.getLocation());

    }

    @Test
    void deleteCompanySuccessfully() throws Exception {
        Company company=new Company("TCS","Mumbai","www.tcs.com");
        Company savedCompany=companyRepository.save(company);

        mockMvc.perform(delete("/companies/"+savedCompany.getId()))
                .andExpect(status().isNoContent());
        boolean isExist=companyRepository.existsById(savedCompany.getId());
        assertFalse(isExist);
    }

    @Test
    void getCompanyByIdNotFound() throws Exception {
        assertFalse(companyRepository.existsById(999L));
        mockMvc.perform(get("/companies/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Company with Id 999 not found."));

    }

    @Test
    void putCompanyByIdNotFound() throws Exception {
        assertFalse(companyRepository.existsById(999L));
        CompanyRequest request=new CompanyRequest("TCS Technologies","Pune","www.tcs-tech.com");
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        mockMvc.perform(put("/companies/999").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Company with Id 999 not found"));

    }
    @Test
    void patchCompanyByIdNotFound() throws Exception {
        assertFalse(companyRepository.existsById(999L));

        CompanyPatchRequest request=new CompanyPatchRequest();
        request.setName("TCS Technologies");
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        mockMvc.perform(patch("/companies/999").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Company with Id 999 not found"));

    }

    @Test
    void deleteCompanyByIdNotFound() throws Exception {
        assertFalse(companyRepository.existsById(999L));
        mockMvc.perform(delete("/companies/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Company with Id 999 not found."));

    }

    @Test
    void createCompanyWithInvalidData() throws Exception {
        CompanyRequest request=new CompanyRequest(" ","Pune","www.tcs-tech.com");
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation Error"));
        long countBefore = companyRepository.count();
        assertEquals(countBefore, companyRepository.count());
    }

    @Test
    void createApplicationSuccessfully() throws Exception{
        Company company=new Company("TCS","Mumbai","www.tcs.com");
        Company savedCompany=companyRepository.save(company);
        JobApplicationRequest request=new JobApplicationRequest("Java developer", ApplicationStatus.APPLIED, LocalDate.now(),"www.tcs.com","Applied through career page",savedCompany.getId());
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);

        //Act
        mockMvc.perform(post("/applications").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Application Created Successfully"))
                .andExpect(jsonPath("$.data.jobTitle").value("Java developer"))
                .andExpect(jsonPath("$.data.status").value("APPLIED"))
                .andExpect(jsonPath("$.data.companyId").value(savedCompany.getId()));

        //verfiy
       Optional<JobApplication> savedApplication=jobApplicationRepository.findAll().stream().filter(application -> application.getJobTitle().equals("Java developer") && application.getCompany().getId().equals(savedCompany.getId())).findFirst();
       assertTrue(savedApplication.isPresent());

        assertEquals("Java developer",savedApplication.get().getJobTitle());
        assertEquals(ApplicationStatus.APPLIED,savedApplication.get().getStatus());
        assertEquals(savedCompany.getId(),savedApplication.get().getCompany().getId());

    }

    @Test
    void getAllApplicationsSuccessfully() throws Exception {
        Company company=new Company("TCS","Mumbai","www.tcs.com");
        Company savedCompany=companyRepository.save(company);
        JobApplication application=new JobApplication("Java developer",ApplicationStatus.APPLIED,LocalDate.now(),"www.java.com","applied through career page",savedCompany);
        JobApplication savedApplication=jobApplicationRepository.save(application);
        mockMvc.perform(get("/applications").param("page","0").param("size","5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Applications Retrieved Successfully"))
                .andExpect(jsonPath("$.data.applications[0].jobTitle").value("Java developer"))
                .andExpect(jsonPath("$.data.applications[0].companyName").value("TCS"))
                .andExpect(jsonPath("$.data.applications[0].jobUrl").value("www.java.com"))
                .andExpect(jsonPath("$.data.applications[0].status").value("APPLIED"))
                .andExpect(jsonPath("$.data.applications[0].companyId").value(savedCompany.getId()));

    }

    @Test
    void getApplicationByIdSucessfully() throws Exception {
        Company company=new Company("TCS","Mumbai","www.tcs.com");
        Company savedCompany=companyRepository.save(company);
        JobApplication application=new JobApplication("Java developer",ApplicationStatus.APPLIED,LocalDate.now(),"www.java.com","applied through career page",savedCompany);
        JobApplication savedApplication=jobApplicationRepository.save(application);
        mockMvc.perform(get("/applications/"+savedApplication.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application Retrieved Successfully"))
                .andExpect(jsonPath("$.data.jobTitle").value("Java developer"))
                .andExpect(jsonPath("$.data.companyName").value("TCS"))
                .andExpect(jsonPath("$.data.jobUrl").value("www.java.com"))
                .andExpect(jsonPath("$.data.status").value("APPLIED"))
                .andExpect(jsonPath("$.data.companyId").value(savedCompany.getId()));

    }

    @Test
    void putApplicationSuccessfully() throws Exception {
        Company company=new Company("TCS","Mumbai","www.tcs.com");
        Company savedCompany=companyRepository.save(company);
        JobApplication application=new JobApplication("Java developer",ApplicationStatus.APPLIED,LocalDate.now(),"www.java.com","applied through career page",savedCompany);
        JobApplication savedApplication=jobApplicationRepository.save(application);
        JobApplicationRequest request=new JobApplicationRequest("Python developer", ApplicationStatus.INTERVIEW, LocalDate.now(),"www.tcs.com","Applied through career page",savedCompany.getId());
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);

        mockMvc.perform(put("/applications/"+savedApplication.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application Updated Successfully"))
                .andExpect(jsonPath("$.data.jobTitle").value("Python developer"))
                .andExpect(jsonPath("$.data.jobUrl").value("www.tcs.com"))
                .andExpect(jsonPath("$.data.status").value("INTERVIEW"))
                .andExpect(jsonPath("$.data.companyId").value(savedCompany.getId()));


        Optional<JobApplication> application1=jobApplicationRepository.findById(savedApplication.getId());
        assertTrue(application1.isPresent());

        assertEquals("Python developer",application1.get().getJobTitle());
        assertEquals(ApplicationStatus.INTERVIEW,application1.get().getStatus());
        assertEquals(savedCompany.getId(),application1.get().getCompany().getId());
    }

    @Test
    void patchApplicationSuccessfully() throws Exception {
        Company company=new Company("TCS","Mumbai","www.tcs.com");
        Company savedCompany=companyRepository.save(company);
        JobApplication application=new JobApplication("Java developer",ApplicationStatus.APPLIED,LocalDate.now(),"www.java.com","applied through career page",savedCompany);
        JobApplication savedApplication=jobApplicationRepository.save(application);
        JobApplicationPatchRequest request=new JobApplicationPatchRequest();
        request.setJobTitle("Python developer");
        request.setStatus(ApplicationStatus.INTERVIEW);
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);

        mockMvc.perform(patch("/applications/"+savedApplication.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application Updated Successfully"))
                .andExpect(jsonPath("$.data.jobTitle").value("Python developer"))
                .andExpect(jsonPath("$.data.status").value("INTERVIEW"))
                .andExpect(jsonPath("$.data.companyId").value(savedCompany.getId()));


        Optional<JobApplication> application1=jobApplicationRepository.findById(savedApplication.getId());
        assertTrue(application1.isPresent());

        assertEquals("Python developer",application1.get().getJobTitle());
        assertEquals(ApplicationStatus.INTERVIEW,application1.get().getStatus());
        assertEquals(savedCompany.getId(),application1.get().getCompany().getId());
        assertEquals("www.java.com", application1.get().getJobUrl());
        assertEquals("applied through career page", application1.get().getNotes());
    }

     @Test
    void deleteApplicationSuccessfully() throws Exception {
         Company company=new Company("TCS","Mumbai","www.tcs.com");
         Company savedCompany=companyRepository.save(company);
         JobApplication application=new JobApplication("Java developer",ApplicationStatus.APPLIED,LocalDate.now(),"www.java.com","applied through career page",savedCompany);
         JobApplication savedApplication=jobApplicationRepository.save(application);

         mockMvc.perform(delete("/applications/"+savedApplication.getId()))
                 .andExpect(status().isNoContent());
         boolean isExist=jobApplicationRepository.existsById(savedApplication.getId());
         assertFalse(isExist);
     }

     @Test
    void getApplicationByIdNotFound() throws Exception {
         assertFalse(jobApplicationRepository.existsById(999L));
         mockMvc.perform(get("/applications/999"))
                 .andExpect(status().isNotFound())
                 .andExpect(jsonPath("$.message").value("Job Application not found with Id 999"));
     }

     @Test
     void putApplicationByIdNotFound() throws Exception {
         assertFalse(jobApplicationRepository.existsById(999L));
         Company company=new Company("TCS","Mumbai","www.tcs.com");
         Company savedCompany=companyRepository.save(company);
         JobApplicationRequest request=new JobApplicationRequest("Python developer", ApplicationStatus.INTERVIEW, LocalDate.now(),"www.tcs.com","Applied through career page",savedCompany.getId());
         ObjectMapper mapper=new ObjectMapper();
         String json=mapper.writeValueAsString(request);
         mockMvc.perform(put("/applications/999").contentType(MediaType.APPLICATION_JSON).content(json))
                 .andExpect(status().isNotFound())
                 .andExpect(jsonPath("$.message").value("Job Application not found with Id 999"));

     }

    @Test
    void patchApplicationByIdNotFound() throws Exception {
        assertFalse(jobApplicationRepository.existsById(999L));
        JobApplicationPatchRequest request=new JobApplicationPatchRequest();
        request.setJobTitle("Python developer");
        request.setStatus(ApplicationStatus.INTERVIEW);
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        mockMvc.perform(patch("/applications/999").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Job Application not found with Id 999"));

    }

    @Test
    void deleteApplicationByIdNotFound() throws Exception {
        assertFalse(jobApplicationRepository.existsById(999L));
        mockMvc.perform(delete("/applications/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Job Application not found with Id 999"));

    }

    @Test
    void createApplicationWithInvalidData() throws Exception {
        Company company=new Company("TCS","Mumbai","www.tcs.com");
        Company savedCompany=companyRepository.save(company);
        JobApplicationRequest request=new JobApplicationRequest("  ", ApplicationStatus.APPLIED, LocalDate.now(),"www.tcs.com","Applied through career page",savedCompany.getId());
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);

        //Act
        mockMvc.perform(post("/applications").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation Error"));

        long countBefore = jobApplicationRepository.count();
        assertEquals(countBefore, jobApplicationRepository.count());
    }
}