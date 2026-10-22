package com.springboot.taskapi.config;

import com.springboot.taskapi.dto.response.ErrorDto;
import com.springboot.taskapi.exception.InvalidCallException;
import com.springboot.taskapi.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger log = LoggerFactory.getLogger("GlobalExceptionHandler.class");

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFoundException(
            ResourceNotFoundException e
    ){
        //always reply the object not the string
        log.warn("Id not found in DB");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorDto(e.getMessage(),
                                "Id not found in DB",
                                Instant.now())
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ){
        Map<String,String> map = new HashMap<>();

        BindingResult bindingResult =  e.getBindingResult();
        List<FieldError> list = bindingResult.getFieldErrors();
        list.forEach(fieldError -> {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
            log.warn("Field {} , failed validation: {}", fieldError.getField(),fieldError.getDefaultMessage());
            }
        );

        return ResponseEntity
                .badRequest()
                .body(map );
    }

    @ExceptionHandler(InvalidCallException.class)
    public ResponseEntity<ErrorDto> handleInvalidCallException(
            InvalidCallException e
    ){
        //always reply the object not the string

        return ResponseEntity
                .badRequest()
                .body(
                        new ErrorDto(e.getMessage(),
                                "Call is Invalid due to malformed data",
                                Instant.now())
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(
            Exception e
    ){
        //always reply the object not the string

        return ResponseEntity
                .badRequest()
                .body(
                        new ErrorDto(e.getMessage(),
                                "Invalid data passed",
                                Instant.now())
                );
    }
}
