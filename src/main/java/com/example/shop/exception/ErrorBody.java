package com.example.shop.exception;
import java.time.LocalDateTime;
import java.util.*;
public record ErrorBody(LocalDateTime timestamp,int status,String error,String message,String path,Map<String,String> details) {
 public static ErrorBody of(int status, String code,String message,String path,Map<String,String> details){ return new ErrorBody(LocalDateTime.now(),status,code,message,path,details); }
}
