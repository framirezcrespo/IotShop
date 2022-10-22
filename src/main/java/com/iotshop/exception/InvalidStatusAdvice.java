package com.iotshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidStatusAdvice {

  @ResponseBody
  @ExceptionHandler(InvalidStatusException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String employeeNotFoundHandler(InvalidStatusException ex) {
    return ex.getMessage();
  }
}
