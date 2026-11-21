package com.yash.jobtracker.controller;

import com.yash.jobtracker.dto.APIResponse;
import com.yash.jobtracker.dto.CompanyPatchRequest;
import com.yash.jobtracker.dto.CompanyRequest;
import com.yash.jobtracker.dto.CompanyResponse;
import com.yash.jobtracker.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name="Companies",description="Manage Companies and their information")
@RestController
@RequestMapping("companies")
public class CompanyController {
    private final CompanyService companyService;
    public CompanyController(CompanyService companyService){
        this.companyService=companyService;
    }
    @Operation(summary = "Get Company by Id",description = "Retrieves Company by Id")
    @ApiResponse(responseCode = "200",description = "Company Retrieved Successfully")
    @ApiResponse(responseCode = "404",description = "Company not found")
   @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id){
        CompanyResponse response=companyService.getById(id);
        APIResponse<CompanyResponse> api=new APIResponse<>(200,"Company Retrieved Successfully",response);
        return ResponseEntity.ok(api);
   }
   @Operation(summary = "Get all companies",description = "Retrieves all companies")
   @ApiResponse(responseCode = "200",description = "Companies Retrieved Successfully")
   @GetMapping
   public ResponseEntity<Object> getAll(){
        List<CompanyResponse> responseList=companyService.getAll();
        APIResponse<List<CompanyResponse>> api=new APIResponse<>(200,"Companies Retrieved Successfully",responseList);
        return ResponseEntity.ok().body(api);
   }

    @Operation(summary = "Create Company",description = "Company Created Successfully")
    @ApiResponse(responseCode = "201",description = "Data Inserted Successfully")
    @ApiResponse(responseCode = "400",description = "Invalid data")
   @PostMapping
    public ResponseEntity<Object> saveCompany(@RequestBody @Valid CompanyRequest companyRequest){
        CompanyResponse response=companyService.saveCompany(companyRequest);
       APIResponse<CompanyResponse> api=new APIResponse<>(201,"Data Inserted Successfully",response);
       return ResponseEntity.status(HttpStatus.CREATED).body(api);
   }
    @Operation(summary = "Delete Company",description = "Company deleted Successfully")
    @ApiResponse(responseCode = "204",description = "Company deleted Successfully")
    @ApiResponse(responseCode = "404",description = "Company Not Found")
   @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id){
        companyService.deleteById(id);
        return ResponseEntity.noContent().build();
   }

    @Operation(summary = "Update Company",description = "Company Updated Successfully")
    @ApiResponse(responseCode = "200",description = "Company Updated Successfully")
    @ApiResponse(responseCode = "404",description = "Company not found")
    @ApiResponse(responseCode = "400",description = "Invalid data")
   @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable Long id,@RequestBody @Valid CompanyRequest companyRequest){
        CompanyResponse response=companyService.update(id,companyRequest);
       APIResponse<CompanyResponse> api=new APIResponse<>(200,"Company Updated Successfully",response);
       return ResponseEntity.ok().body(api);

   }

    @Operation(summary = "Update Company Partially",description = "Company Partially Updated Successfully")
    @ApiResponse(responseCode = "200",description = "Company Partially Updated Successfully")
    @ApiResponse(responseCode = "404",description = "Company not found")
    @ApiResponse(responseCode = "400",description = "Invalid data")
    @PatchMapping("{id}")
    public ResponseEntity<Object> patch(@PathVariable Long id, @RequestBody @Valid CompanyPatchRequest request){
        CompanyResponse response=companyService.patch(id,request);
       APIResponse<CompanyResponse> api=new APIResponse<>(200,"Company Updated Successfully",response);
       return ResponseEntity.ok().body(api);
   }
}
