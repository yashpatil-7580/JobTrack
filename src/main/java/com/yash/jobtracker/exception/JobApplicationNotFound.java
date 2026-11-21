package com.yash.jobtracker.exception;

public class JobApplicationNotFound extends RuntimeException {
    public JobApplicationNotFound(String message) {
        super(message);
    }
}
