package com.example.shop.validator;
import jakarta.validation.*;
import java.lang.annotation.*;
@Target({ElementType.FIELD,ElementType.PARAMETER,ElementType.RECORD_COMPONENT}) @Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=PasswordValidator.class)
public @interface ValidPassword {String message() default "Password needs 8+ chars, uppercase, lowercase, digit and symbol";Class<?>[] groups() default {};Class<? extends Payload>[] payload() default {};}
