package com.yash.jobtracker.service;

import com.yash.jobtracker.dto.CompanyPatchRequest;
import com.yash.jobtracker.dto.CompanyRequest;
import com.yash.jobtracker.dto.CompanyResponse;
import com.yash.jobtracker.entity.Company;
import com.yash.jobtracker.exception.CompanyNotFoundException;
import com.yash.jobtracker.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {
    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
   private CompanyService companyService;

    @Test
    public void findByIdWhenCompanyExist(){
        //Arrange
        Company company=new Company();
        company.setId(1L);
        company.setName("Infosys");
        company.setLocation("Pune");
        company.setWebsite("www.infosys.com");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        //Act
        CompanyResponse response=companyService.getById(1L);

        //Asseration
        assertEquals(1,response.getId());
        assertEquals("Infosys",response.getName());
        assertEquals("Pune",response.getLocation());
        assertEquals("www.infosys.com",response.getWebsite());
    }

    @Test
    public void findByIdWhenCompanyNotExist(){
        //Arrange
        when(companyRepository.findById(100L)).thenReturn(Optional.empty());

        //Act
        assertThrows(CompanyNotFoundException.class,()->companyService.getById(100L));

        //Verify that service has been called exactly once
       verify(companyRepository,times(1)).findById(100L);

    }

    @Test
    public void saveCompanySuccessfully(){
        //Arrange
        CompanyRequest request=new CompanyRequest("TCS","Pune","www.tcs.com");
        Company company=new Company();
        company.setId(1L);
        company.setName(request.getName());
        company.setLocation(request.getLocation());
        company.setWebsite(request.getWebsite());
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        //Act
        CompanyResponse response=companyService.saveCompany(request);

        //Assertion
        assertEquals(1L,response.getId());
        assertEquals("TCS",response.getName());
        assertEquals("Pune",response.getLocation());
        assertEquals("www.tcs.com",response.getWebsite());

        //verify that repository method was called only once
        verify(companyRepository,times(1)).save(any(Company.class));
    }

    @Test
    public void updateCompanySuccessfully(){
        //Arrange
        Company company=new Company();
        company.setId(1L);
        company.setName("TCS");
        company.setLocation("Pune");
        company.setWebsite("www.tcs.com");

        CompanyRequest request=new CompanyRequest("Infosys","Mumbai","tcs.com");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        CompanyResponse response=companyService.update(1L,request);
      //  assertEquals(1L,response.getId());
        assertEquals("Infosys",response.getName());
        assertEquals("Mumbai",response.getLocation());
        assertEquals("tcs.com",response.getWebsite());

        //verify that repository method was called only once
        verify(companyRepository,times(1)).save(any(Company.class));

    }

    @Test
    public void patchCompanySuccessfully(){
        //Arrange
        Company company=new Company();
        company.setId(1L);
        company.setName("TCS");
        company.setLocation("Pune");
        company.setWebsite("www.tcs.com");

        CompanyPatchRequest request=new CompanyPatchRequest(null,"Mumbai",null);
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        CompanyResponse response=companyService.patch(1L,request);

        //Assertion
        assertEquals(1L,response.getId());
        assertEquals("TCS",response.getName());
        assertEquals("Mumbai",response.getLocation());
        assertEquals("www.tcs.com",response.getWebsite());

        //verify
        verify(companyRepository,times(1)).save(any(Company.class));
        verify(companyRepository,times(1)).findById(1L);

    }

    @Test
    public void updateCompanyNotFound(){
        //Arrange
        when(companyRepository.findById(100L)).thenReturn(Optional.empty());
        CompanyRequest request=new CompanyRequest("TCS","Pune","www.tcs.com");

        //Act
        assertThrows(CompanyNotFoundException.class,()->companyService.update(100L,request));

        //verify
        verify(companyRepository,times(1)).findById(100L);
        verify(companyRepository,never()).save(any(Company.class));
    }

    @Test
    public void patchCompanyNotFound(){
        //Arrange
        when(companyRepository.findById(100L)).thenReturn(Optional.empty());
        CompanyPatchRequest request=new CompanyPatchRequest(null,"Mumbai",null);

        //Act
        assertThrows(CompanyNotFoundException.class,()->companyService.patch(100L,request));

        //verify
        verify(companyRepository,times(1)).findById(100L);
        verify(companyRepository,never()).save(any(Company.class));
    }

    @Test
    public void deleteCompanySuccessfully(){
        //Arrange
        when(companyRepository.existsById(1L)).thenReturn(true);

        //Act
        companyService.deleteById(1L);
        //verify
        verify(companyRepository,times(1)).deleteById(1L);
        verify(companyRepository,times(1)).existsById(1L);
    }
    @Test
    public void deleteCompanyNotFound(){
        //Arrange
        when(companyRepository.existsById(100L)).thenReturn(false);
        //Act
        assertThrows(CompanyNotFoundException.class,()->companyService.deleteById(100L));
        //verify
        verify(companyRepository,times(1)).existsById(100L);
        verify(companyRepository,never()).deleteById(100L);
    }

    @Test
    public void getAllCompaniesSuccessFully(){
        //Arrange
        Company company=new Company();
        company.setId(1L);
        company.setName("Infosys");
        company.setLocation("Pune");
        company.setWebsite("www.infosys.com");
        Company company2=new Company();
        company2.setId(2L);
        company2.setName("TCS");
        company2.setLocation("Pune");
        company2.setWebsite("www.tcs.com");
        when(companyRepository.findAll()).thenReturn(List.of(company,company2));
        //Act
        List<CompanyResponse> responses=companyService.getAll();
        //Assertion
        assertEquals(2,responses.size());
        assertEquals(1,responses.get(0).getId());
        assertEquals("Infosys",responses.get(0).getName());
        assertEquals("Pune",responses.get(0).getLocation());
        assertEquals("www.infosys.com",responses.get(0).getWebsite());

        assertEquals(2,responses.get(1).getId());
        assertEquals("TCS",responses.get(1).getName());
        assertEquals("Pune",responses.get(1).getLocation());
        assertEquals("www.tcs.com",responses.get(1).getWebsite());

        //verify
        verify(companyRepository,times(1)).findAll();
    }
}
