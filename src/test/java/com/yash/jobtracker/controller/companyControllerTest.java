package com.yash.jobtracker.controller;


import com.yash.jobtracker.dto.CompanyPatchRequest;
import com.yash.jobtracker.dto.CompanyRequest;
import com.yash.jobtracker.dto.CompanyResponse;
import com.yash.jobtracker.entity.Company;
import com.yash.jobtracker.exception.CompanyNotFoundException;
import com.yash.jobtracker.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
public class companyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompanyService companyService;

    @Test
    public void getAllCompaniesSuccessfully() throws Exception {
        //Arrange
        CompanyResponse company=new CompanyResponse();
        company.setId(1L);
        company.setName("Infosys");
        company.setLocation("Pune");
        company.setWebsite("www.infosys.com");
        CompanyResponse company2=new CompanyResponse();
        company2.setId(2L);
        company2.setName("TCS");
        company2.setLocation("Pune");
        company2.setWebsite("www.tcs.com");
        when(companyService.getAll()).thenReturn(List.of(company,company2));

        //Act
        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                        .andExpect(jsonPath("$.message").value("Companies Retrieved Successfully"))
                                .andExpect(jsonPath("$.data[0].name").value("Infosys"))
                                        .andExpect(jsonPath("$.data[1].name").value("TCS"))
                                                .andExpect(jsonPath("$.data.length()").value(2));

        //Verify
        verify(companyService,times(1)).getAll();
    }

    @Test
    public void createCompanySuccessfully() throws Exception {
        //Arrange
        CompanyRequest request=new CompanyRequest("Wipro","Pune","www.wipro.com");
        CompanyResponse response=new CompanyResponse();
        response.setId(1L);
        response.setName(request.getName());
        response.setWebsite(request.getWebsite());
        response.setLocation(request.getLocation());
        when(companyService.saveCompany(any(CompanyRequest.class))).thenReturn(response);

        //Act
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Data Inserted Successfully"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Wipro"));

        //verify
        verify(companyService,times(1)).saveCompany(any(CompanyRequest.class));

    }

    @Test
    public void createCompanyWithBlankNameShouldReturn400() throws Exception {
        //Arrange
        CompanyRequest request=new CompanyRequest(" ","Pune","www.wipro.com");
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        //Act and Assert
        mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value("Validation Error"))
                                .andExpect(jsonPath("$.errors.name").exists());
        //verify
        verify(companyService,never()).saveCompany(any(CompanyRequest.class));

    }

    @Test
    public void getCompanyByIdSuccessfully() throws Exception {
        //Arrange
        CompanyRequest request=new CompanyRequest("TCS","Pune","www.tcs.com");
        CompanyResponse response=new CompanyResponse();
        response.setId(1L);
        response.setName(request.getName());
        response.setWebsite(request.getWebsite());
        response.setLocation(request.getLocation());
        when(companyService.getById(1L)).thenReturn(response);
        //Act
        mockMvc.perform(get("/companies/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Company Retrieved Successfully"))
                        .andExpect(jsonPath("$.data.id").value(1L))
                                .andExpect(jsonPath("$.data.name").value("TCS"));


        //verify
        verify(companyService,times(1)).getById(1L);
    }

    @Test
    public void getByIdNotFound() throws Exception {
        when(companyService.getById(100L)).thenThrow(new CompanyNotFoundException("Company with Id 100 not found"));

        mockMvc.perform(get("/companies/100")).andExpect(status().isNotFound());

        verify(companyService,times(1)).getById(100L);
    }

    @Test
    public void deleteByIdSuccessfully() throws Exception {
        doNothing().when(companyService).deleteById(1L);

        mockMvc.perform(delete("/companies/1")).andExpect(status().isNoContent());

                verify(companyService,times(1)).deleteById(1L);
    }

    @Test
    public void deleteByIdNotFound()throws Exception{
        doThrow(new CompanyNotFoundException("Company with Id 100 not found")).when(companyService).deleteById(100L);

        mockMvc.perform(delete("/companies/100")).andExpect(status().isNotFound());

        verify(companyService,times(1)).deleteById(100L);

    }

    @Test
    public void updateCompanySuccessfully() throws Exception {
        //Arrange
        CompanyRequest request=new CompanyRequest("TCS","Pune","www.tcs.com");
        CompanyResponse response=new CompanyResponse();
        response.setId(1L);
        response.setName(request.getName());
        response.setWebsite(request.getWebsite());
        response.setLocation(request.getLocation());
        when(companyService.update(eq(1L),any(CompanyRequest.class))).thenReturn(response);
        //Act
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        mockMvc.perform(put("/companies/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Company Updated Successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("TCS"));

        //verify
        verify(companyService,times(1)).update(eq(1L),any(CompanyRequest.class));
    }

    @Test
    public void updateCompanyNotFound() throws Exception {
        CompanyRequest request = new CompanyRequest("TCS", "Pune", "www.tcs.com");
        when(companyService.update(eq(100L),any(CompanyRequest.class))).thenThrow(new CompanyNotFoundException("Company with Id 100 not found"));
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);
        mockMvc.perform(put("/companies/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());

        verify(companyService,times(1)).update(eq(100L),any(CompanyRequest.class));

    }

    @Test
    public void patchCompanySuccessfully() throws Exception {
        CompanyPatchRequest request=new CompanyPatchRequest(null,"Mumbai",null);
        CompanyResponse response=new CompanyResponse(1L,"TCS",request.getLocation(),"www.tcs.com");
        when(companyService.patch(eq(1L),any(CompanyPatchRequest.class))).thenReturn(response);

        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        mockMvc.perform(patch("/companies/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Company Updated Successfully"))
                .andExpect(jsonPath("$.data.location").value("Mumbai"));

        verify(companyService,times(1)).patch(eq(1L),any(CompanyPatchRequest.class));
    }

    @Test
    public void patchCompanyNotFound() throws Exception {
        CompanyPatchRequest request=new CompanyPatchRequest(null,"Mumbai",null);
        when(companyService.patch(eq(100L),any(CompanyPatchRequest.class))).thenThrow(new CompanyNotFoundException("Company with Id 100 not found"));

        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(request);
        mockMvc.perform(patch("/companies/100").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
        //verify
        verify(companyService,times(1)).patch(eq(100L),any(CompanyPatchRequest.class));
    }
}