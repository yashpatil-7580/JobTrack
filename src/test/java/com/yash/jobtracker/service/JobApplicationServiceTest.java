package com.yash.jobtracker.service;

import com.yash.jobtracker.dto.JobApplicationResponse;
import com.yash.jobtracker.dto.PaginatedResponse;
import com.yash.jobtracker.entity.ApplicationStatus;
import com.yash.jobtracker.entity.Company;
import com.yash.jobtracker.entity.JobApplication;
import com.yash.jobtracker.repository.JobApplicationRepository;
import com.yash.jobtracker.repository.JobApplicationSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class JobApplicationServiceTest {
    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Mock
    private JobApplicationSpecification jobApplicationSpecification;

    @InjectMocks
    private JobApplicationService jobApplicationService;

    @Test
    public void getAllApplications(){
        //Arrange
        when(jobApplicationRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(Page.empty());

        //Act
        PaginatedResponse applicationResponses=jobApplicationService.getAllApplications(0,10,"id","asc",null,null,null);

        //Assert
        assertEquals(0,applicationResponses.getTotalElements());
        verify(jobApplicationRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));

    }
    @Test
    public void getAllApplicationWithStatusFilter(){
        //Arrange
        when(jobApplicationSpecification.statusEquals(ApplicationStatus.APPLIED)).thenReturn(Specification.unrestricted());
        when(jobApplicationRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(Page.empty());

        //Act
        PaginatedResponse response=jobApplicationService.getAllApplications(0,10,"id","asc",ApplicationStatus.APPLIED,null,null);

        //Assert

        assertEquals(0,response.getTotalElements());
        verify(jobApplicationRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
        verify(jobApplicationSpecification,times(1)).statusEquals(ApplicationStatus.APPLIED);
    }

    @Test
    public void getAllApplicationReturnsApplications(){
        //Arrange
        JobApplication application1=new JobApplication();
        application1.setId(1L);
        application1.setJobTitle("Java Developer");
        application1.setStatus(ApplicationStatus.APPLIED);
        application1.setAppliedDate(LocalDate.now());
        application1.setNotes("Applied through company website");
        Company c1=new Company();
        c1.setId(1L);
        c1.setName("TCS");
        c1.setLocation("Pune");
        c1.setWebsite("www.tcs.com");
        application1.setCompany(c1);

        JobApplication application2=new JobApplication();
        application2.setId(2L);
        application2.setJobTitle("Python Developer");
        application2.setStatus(ApplicationStatus.APPLIED);
        application2.setAppliedDate(LocalDate.now());
        application2.setNotes("Applied through company website");
        Company c2=new Company();
        c2.setId(2L);
        c2.setName("Infosys");
        c2.setLocation("Pune");
        c2.setWebsite("www.infosys.com");
        application2.setCompany(c2);

        Page<JobApplication> page=new PageImpl<>(List.of(application1,application2));
        when(jobApplicationRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(page);

        //Act
        PaginatedResponse response=jobApplicationService.getAllApplications(0,10,"id","asc",null,null,null);

        //assert
        System.out.println(response.getApplications());
        assertEquals("Java Developer", response.getApplications().get(0).getJobTitle());

        assertEquals("TCS", response.getApplications().get(0).getCompanyName());
        assertEquals(2,response.getTotalElements());
        assertEquals(2,response.getPageSize());

        verify(jobApplicationRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));

    }

    @Test
    public void jobApplicationWithCompanyIdFilter(){
        //Arrange
        when(jobApplicationSpecification.companyEquals(1L)).thenReturn(Specification.unrestricted());
        when(jobApplicationRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(Page.empty());
        //Act
        PaginatedResponse response=jobApplicationService.getAllApplications(0,10,"id","asc",null,1L,null);

        //assert
        assertEquals(0,response.getTotalElements());
        verify(jobApplicationRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    public void jobApplicationWithDaysFilter(){
        //Arrange
        when(jobApplicationSpecification.appliedBetween(any(LocalDate.class),any(LocalDate.class))).thenReturn(Specification.unrestricted());
        when(jobApplicationRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(Page.empty());

        //Act
        PaginatedResponse response=jobApplicationService.getAllApplications(0,10,"id","asc",null,null,7);

        //assert
        assertEquals(0,response.getTotalElements());
        verify(jobApplicationRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
        verify(jobApplicationSpecification,times(1)).appliedBetween(any(LocalDate.class),any(LocalDate.class));
    }

    @Test
    public void getAllApplicationsStatusAndCompanyFilter(){
        //Arrange
        when(jobApplicationSpecification.statusEquals(ApplicationStatus.APPLIED)).thenReturn(Specification.unrestricted());
        when(jobApplicationSpecification.companyEquals(1L)).thenReturn(Specification.unrestricted());
        when(jobApplicationRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(Page.empty());

        //Act
        PaginatedResponse response=jobApplicationService.getAllApplications(0,10,"id","asc",ApplicationStatus.APPLIED,1L,null);

        //assert
        assertEquals(0,response.getTotalElements());
        verify(jobApplicationSpecification,times(1)).statusEquals(ApplicationStatus.APPLIED);
        verify(jobApplicationSpecification,times(1)).companyEquals(1L);
        verify(jobApplicationRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
    }


    @Test
    public void getAllApplicationsAllFilter(){
        //Arrange
        when(jobApplicationSpecification.appliedBetween(any(LocalDate.class),any(LocalDate.class))).thenReturn(Specification.unrestricted());
        when(jobApplicationSpecification.statusEquals(ApplicationStatus.APPLIED)).thenReturn(Specification.unrestricted());
        when(jobApplicationSpecification.companyEquals(1L)).thenReturn(Specification.unrestricted());
        when(jobApplicationRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(Page.empty());

        //Act
        PaginatedResponse response=jobApplicationService.getAllApplications(0,10,"id","asc",ApplicationStatus.APPLIED,1L,7);

        //assert
        assertEquals(0,response.getTotalElements());
        verify(jobApplicationSpecification,times(1)).statusEquals(ApplicationStatus.APPLIED);
        verify(jobApplicationSpecification,times(1)).companyEquals(1L);
        verify(jobApplicationRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
        verify(jobApplicationSpecification,times(1)).appliedBetween(any(LocalDate.class),any(LocalDate.class));

    }

    @Test
    public void getAllApplicationsPaginated(){
        //Arrange
        when(jobApplicationRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(Page.empty());
        //Act
        PaginatedResponse response=jobApplicationService.getAllApplications(1,5,"id","asc",null,null,null);
        ArgumentCaptor<Pageable> captor=ArgumentCaptor.forClass(Pageable.class);

        verify(jobApplicationRepository).findAll(any(Specification.class),captor.capture());
        Pageable pageable=captor.getValue();
        assertEquals(1,pageable.getPageNumber());
        assertEquals(5,pageable.getPageSize());

        verify(jobApplicationRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));

    }
    @Test
    public void getAllApplicationSortedAscending(){
        //Arrange
        when(jobApplicationRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(Page.empty());
        //Act
        PaginatedResponse response=jobApplicationService.getAllApplications(1,5,"jobTitle","asc",null,null,null);
        ArgumentCaptor<Pageable> captor=ArgumentCaptor.forClass(Pageable.class);

        verify(jobApplicationRepository).findAll(any(Specification.class),captor.capture());

        Pageable pageable=captor.getValue();
        Sort.Order order=pageable.getSort().getOrderFor("jobTitle");
        assertNotNull(order);
        assertEquals("jobTitle",order.getProperty());
        assertEquals(Sort.Direction.ASC,order.getDirection());

        verify(jobApplicationRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));

    }

    @Test
    public void getAllApplicationSortedDescending(){
        //Arrange
        when(jobApplicationRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(Page.empty());
        //Act
        PaginatedResponse response=jobApplicationService.getAllApplications(1,5,"jobTitle","desc",null,null,null);
        ArgumentCaptor<Pageable> captor=ArgumentCaptor.forClass(Pageable.class);

        verify(jobApplicationRepository).findAll(any(Specification.class),captor.capture());

        Pageable pageable=captor.getValue();
        Sort.Order order=pageable.getSort().getOrderFor("jobTitle");
        assertNotNull(order);
        assertEquals("jobTitle",order.getProperty());
        assertEquals(Sort.Direction.DESC,order.getDirection());

        verify(jobApplicationRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));

    }

    @Test
    public void getAllApplicationsWithInvalidPageNumber(){

        assertThrows(IllegalArgumentException.class,()->{
              jobApplicationService.getAllApplications(-1,5,"id","asc",null,null,null);
        });

        verify(jobApplicationRepository,never()).findAll(any(Specification.class),any(Pageable.class));

    }

    @Test
    public void getAllApplicationsWithInvalidPageSize(){

        assertThrows(IllegalArgumentException.class,()->{
            jobApplicationService.getAllApplications(1,101,"id","asc",null,null,null);
        });

        verify(jobApplicationRepository,never()).findAll(any(Specification.class),any(Pageable.class));

    }

    @Test
    public void getAllApplicationsWithZeroPageSize(){

        assertThrows(IllegalArgumentException.class,()->{
            jobApplicationService.getAllApplications(1,0,"id","asc",null,null,null);
        });

        verify(jobApplicationRepository,never()).findAll(any(Specification.class),any(Pageable.class));

    }

    @Test
    public void getAllApplicationWithInvalidCompanyId(){
        assertThrows(IllegalArgumentException.class,()->{
           jobApplicationService.getAllApplications(0,1,"id","asc",null,-1l,null);

        });
        verify(jobApplicationRepository,never()).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    public void getAllApplicationWithInvalidDays(){
        assertThrows(IllegalArgumentException.class,()->{
            jobApplicationService.getAllApplications(0,1,"id","asc",null,1l,-1);

        });
        verify(jobApplicationRepository,never()).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    public void getAllApplicationWithInvalidSortField(){
        assertThrows(IllegalArgumentException.class,()->{
            jobApplicationService.getAllApplications(0,1,"salary","asc",null,1l,-1);

        });
        verify(jobApplicationRepository,never()).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    public void getAllApplicationWithInvalidSortDirection(){
        assertThrows(IllegalArgumentException.class,()->{
            jobApplicationService.getAllApplications(0,1,"id","random",null,1l,-1);

        });
        verify(jobApplicationRepository,never()).findAll(any(Specification.class),any(Pageable.class));
    }

}
