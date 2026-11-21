package com.yash.jobtracker.dto;

import java.util.List;

public class PaginatedResponse {
    private List<JobApplicationResponse> applications;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isFirst;
    private boolean isLast;

    public PaginatedResponse(){}
    public PaginatedResponse( List<JobApplicationResponse> applications,int currentPage,
                               int pageSize,long totalElements,int totalPages,boolean isFirst,boolean isLast){
             this.applications=applications;
             this.currentPage=currentPage;
             this.pageSize=pageSize;
             this.totalElements=totalElements;
             this.totalPages=totalPages;
             this.isFirst=isFirst;
             this.isLast=isLast;
    }

    public List<JobApplicationResponse> getApplications() {
        return applications;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public boolean isLast() {
        return isLast;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
