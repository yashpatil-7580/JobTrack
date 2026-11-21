package com.yash.jobtracker.service;

import com.yash.jobtracker.dto.CompanyPatchRequest;
import com.yash.jobtracker.dto.CompanyRequest;
import com.yash.jobtracker.dto.CompanyResponse;
import com.yash.jobtracker.entity.Company;
import com.yash.jobtracker.exception.CompanyNotFoundException;
import com.yash.jobtracker.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository){
        this.companyRepository=companyRepository;
    }

   public CompanyResponse saveCompany(CompanyRequest companyRequest){
        Company company=mapToRequest(companyRequest);
        Company savedCompany=companyRepository.save(company);
        CompanyResponse response=mapToResponse(savedCompany);
        return response;
      }


    public List<CompanyResponse> getAll(){
        List<Company> c=companyRepository.findAll();
        List<CompanyResponse> responses=new ArrayList<>();
        for(Company i:c){
            responses.add(mapToResponse(i));
        }
        return responses;
    }

    public CompanyResponse getById(Long id){
        Company company=companyRepository.findById(id).orElseThrow(()-> new CompanyNotFoundException("Company with Id "+id+" not found."));
            CompanyResponse response=mapToResponse(company);
            return response;

    }

    public void deleteById(Long id){
        if(companyRepository.existsById(id)){
            companyRepository.deleteById(id);
        }else{
            throw new CompanyNotFoundException("Company with Id "+id+" not found.");
        }
    }

    public CompanyResponse update(Long id,CompanyRequest companyRequest){
        Optional<Company> company=companyRepository.findById(id);
        if(company.isPresent()){
            Company company1=company.get();
            company1.setName(companyRequest.getName());
            company1.setWebsite(companyRequest.getWebsite());
            company1.setLocation(companyRequest.getLocation());
            Company savedCompany=companyRepository.save(company1);
            CompanyResponse response=mapToResponse(savedCompany);
            return response;
        }
        throw new CompanyNotFoundException("Company with Id "+id+" not found");

    }
    public CompanyResponse patch(Long id, CompanyPatchRequest request){
        Optional<Company> c=companyRepository.findById(id);
        if(c.isPresent()){
            Company company=c.get();
            if(request.getName()!=null){
                company.setName(request.getName());
            }
            if(request.getLocation()!=null){
                company.setLocation(request.getLocation());
            }
            if(request.getWebsite()!=null){
                company.setWebsite(request.getWebsite());
            }
            Company c1=companyRepository.save(company);
            CompanyResponse response=mapToResponse(c1);
            return response;
        }else{
            throw new CompanyNotFoundException("Company with Id "+id+" not found");
        }

    }

    private Company mapToRequest(CompanyRequest companyRequest){
      Company company=new Company();
      company.setName(companyRequest.getName());
      company.setLocation(companyRequest.getLocation());
      company.setWebsite(companyRequest.getWebsite());
      return company;
    }

    private CompanyResponse mapToResponse(Company company){
            CompanyResponse companyResponse=new CompanyResponse();
            companyResponse.setId(company.getId());
            companyResponse.setName(company.getName());
            companyResponse.setLocation(company.getLocation());
            companyResponse.setWebsite(company.getWebsite());
            return companyResponse;
    }

}
