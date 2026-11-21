package com.yash.jobtracker.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CompanyPatchRequest {
    @Size(min = 2, max = 100)
    @Pattern(regexp = ".*\\S.*", message = "Name must not be blank")
    private String name;
    @Size(max=100)
    private String location;
    @Size(max=255)
    private String website;

    public CompanyPatchRequest(){}
    public CompanyPatchRequest(String name,String location,String website){
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
