package com.bibtex.controller;

import javax.validation.ConstraintViolationException;

import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.bibtex.entity.ApiErrorResponse;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  @ExceptionHandler(value = {ConstraintViolationException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiErrorResponse constraintViolationException(ConstraintViolationException ex) {
    return new ApiErrorResponse(500, 5001, ex.getMessage());
  }

  @ExceptionHandler(value = {NoHandlerFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiErrorResponse noHandlerFoundException(Exception ex) {
    return new ApiErrorResponse(404, 4041, ex.getMessage());
  }
  
  
  @ExceptionHandler(value = {HttpMessageNotReadableException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiErrorResponse requestBodyNotAvailable(Exception ex) {
    return new ApiErrorResponse(400, 4001, "Request Body cannot be null");
  }
  @ExceptionHandler(value = {Exception.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiErrorResponse unknownException(Exception ex) {
    return new ApiErrorResponse(500, 5002, ex.getMessage());
  }
  
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ExceptionHandler(ResourceNotFoundException.class)
  public void bookNotFound() {
    
  }


  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public ApiErrorResponse invalidRequestBody(Exception ex) {
    return new ApiErrorResponse(400, 4002, ex.getMessage());
  }
}
