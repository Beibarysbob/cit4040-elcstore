package com.example.shop.validator;
import jakarta.validation.*;
import java.lang.annotation.*;
@Target({ElementType.FIELD,ElementType.PARAMETER,ElementType.RECORD_COMPONENT}) @Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=UniqueEmailValidator.class)
public @interface UniqueEmail {String message() default "Email already registered";Class<?>[] groups() default {};Class<? extends Payload>[] payload() default {};}
