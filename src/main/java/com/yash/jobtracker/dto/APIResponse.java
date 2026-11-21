package com.yash.jobtracker.dto;

public class APIResponse<T> {
    private final int status;
    private final String message;
    private final T data;
    public APIResponse(int status,String message, T data){
          this.status=status;
          this.message=message;
          this.data=data;
  }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }


}
