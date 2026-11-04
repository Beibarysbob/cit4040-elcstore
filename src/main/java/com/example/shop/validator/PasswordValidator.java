package com.example.shop.validator;
import jakarta.validation.*;
public class PasswordValidator implements ConstraintValidator<ValidPassword,String> {
 @Override public boolean isValid(String s,ConstraintValidatorContext ctx){return s!=null && s.length()>=8 && s.matches(".*[A-Z].*") && s.matches(".*[a-z].*") && s.matches(".*[0-9].*") && s.matches(".*[^a-zA-Z0-9].*");}
}
