package com.yash.jobtracker.exception;

import com.yash.jobtracker.dto.ErrorResponse;
import com.yash.jobtracker.dto.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validationException(MethodArgumentNotValidException exception){
        List<FieldError> l=exception.getBindingResult().getFieldErrors();
        Map<String,List<String>>m=new HashMap<>();
        for(FieldError f:l){
          //  System.out.println(f.getField()+": "+f.getDefaultMessage());
            if(m.get(f.getField())==null){
                List<String> errors=new ArrayList<>();
                errors.add(f.getDefaultMessage());
                m.put(f.getField(),errors);
            }else{
                List<String> existingList=m.get(f.getField());
                existingList.add(f.getDefaultMessage());
            }
        }
        ValidationErrorResponse response=new ValidationErrorResponse(400,"Validation Error",m);

        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> genericException(Exception ex){
        ErrorResponse er=new ErrorResponse(500,"Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> typeMismatchException(MethodArgumentTypeMismatchException ex){
        ErrorResponse er=new ErrorResponse(400,"Invalid Parameter Value");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> httpMessageNotReadableException(HttpMessageNotReadableException ex){
        ErrorResponse er=new ErrorResponse(400,"Invalid or Malformed Request Body");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }


    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<Object>exception(CompanyNotFoundException ex){
        ErrorResponse response=new ErrorResponse(404,ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(JobApplicationNotFound.class)
    public ResponseEntity<Object>exception(JobApplicationNotFound ex){
        ErrorResponse response=new ErrorResponse(404,ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentException(IllegalArgumentException ex){
        ErrorResponse er=new ErrorResponse(400,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }
}
