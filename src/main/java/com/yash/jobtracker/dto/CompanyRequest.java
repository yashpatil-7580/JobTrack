package com.yash.jobtracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CompanyRequest {
    @NotBlank
    @Size(min = 2,max=100)
    private String name;
    @Size(max=100)
    private String location;
    @Size(max=255)
    private String website;
    public CompanyRequest(){}
    public CompanyRequest(String name,String location,String website){
        this.name=name;
        this.location=location;
        this.website=website;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
