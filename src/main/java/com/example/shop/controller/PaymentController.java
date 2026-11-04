package com.example.shop.controller;
import com.example.shop.dto.ApiDto.*;
import com.example.shop.service.*;
import com.example.shop.model.enums.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import java.net.URI;
import java.util.*;
import java.math.*;
@RestController @RequiredArgsConstructor @RequestMapping("/api/payments")
public class PaymentController {
 private final PaymentService payments;

 @PostMapping("/process") public PaymentResponse process(@Valid @RequestBody PaymentRequest r){return payments.process(r);}
 @GetMapping("/{orderId}/status") public PaymentResponse status(@PathVariable Long orderId){return payments.status(orderId);}
 @GetMapping("/methods") public Page<String> methods(@PageableDefault(size=20) Pageable page){return com.example.shop.config.PageUtil.fromList(payments.methods(),page);}

}
