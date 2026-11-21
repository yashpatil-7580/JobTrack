package com.yash.jobtracker.dto;

import java.util.List;
import java.util.Map;

public class ValidationErrorResponse {
    private int status;
    private String message;
    private Map<String, List<String>> errors;
    public ValidationErrorResponse(){}
    public ValidationErrorResponse(int status, String message,Map<String, List<String>> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String,List<String>> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
