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
@RestController @RequiredArgsConstructor @RequestMapping("/api/delivery")
public class DeliveryController {
 private final DeliveryService delivery;

 @GetMapping("/methods") public Page<DeliveryResponse> methods(@PageableDefault(size=20) Pageable page){return com.example.shop.config.PageUtil.fromList(delivery.methods(),page);}
 @PostMapping("/calculate") public DeliveryCalculateResponse calculate(@Valid @RequestBody DeliveryCalculateRequest r){return delivery.calculate(r);}

}
