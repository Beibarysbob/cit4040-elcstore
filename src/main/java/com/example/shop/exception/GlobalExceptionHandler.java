package com.example.shop.exception;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(ApiException.class)
 public ResponseEntity<ErrorBody> api(ApiException e, HttpServletRequest req){return ResponseEntity.status(e.status()).body(ErrorBody.of(e.status().value(),e.code(),e.getMessage(),req.getRequestURI(),Map.of()));}
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<ErrorBody> validation(MethodArgumentNotValidException e,HttpServletRequest req){
  Map<String,String> details=new LinkedHashMap<>();
  for(var x:e.getBindingResult().getFieldErrors())details.put(x.getField(),String.valueOf(x.getDefaultMessage()));
  return ResponseEntity.badRequest().body(ErrorBody.of(400,"VALIDATION_ERROR","Request validation failed",req.getRequestURI(),details));
 }
 @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class, IllegalArgumentException.class})
 public ResponseEntity<ErrorBody> bad(Exception e,HttpServletRequest req){return ResponseEntity.badRequest().body(ErrorBody.of(400,"BAD_REQUEST","Malformed or invalid request",req.getRequestURI(),Map.of()));}
 @ExceptionHandler(DataIntegrityViolationException.class)
 public ResponseEntity<ErrorBody> constraint(Exception e,HttpServletRequest req){return ResponseEntity.status(409).body(ErrorBody.of(409,"CONFLICT","Database constraint violation",req.getRequestURI(),Map.of()));}
 @ExceptionHandler(Exception.class)
 public ResponseEntity<ErrorBody> unexpected(Exception e,HttpServletRequest req){return ResponseEntity.status(500).body(ErrorBody.of(500,"INTERNAL_ERROR","Unexpected server error",req.getRequestURI(),Map.of()));}
}
