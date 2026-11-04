package com.example.shop.validator;
import com.example.shop.repository.UserRepository;
import jakarta.validation.*;
import org.springframework.stereotype.Component;
@Component public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail,String> {
 private final UserRepository repository;
 public UniqueEmailValidator(UserRepository repository){this.repository=repository;}
 public boolean isValid(String email,ConstraintValidatorContext c){return email==null || !repository.existsByEmailIgnoreCase(email);}
}
