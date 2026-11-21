package com.yash.jobtracker.dto;

public class CompanyResponse {
     private Long id;
     private String name;
     private String location;
     private String website;
     public CompanyResponse(){}
    public CompanyResponse(Long id,String name,String location,String website){
         this.id=id;
         this.name=name;
         this.location=location;
         this.website=website;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Long getId() {
        return id;
    }


    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }


}
