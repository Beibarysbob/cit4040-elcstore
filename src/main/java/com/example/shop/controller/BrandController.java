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
@RestController @RequiredArgsConstructor @RequestMapping("/api/brands")
public class BrandController {
 private final CatalogService service;

 @GetMapping public Page<BrandResponse> list(@PageableDefault(size=20) Pageable p){return service.brands(p);}
 @GetMapping("/{id}") public BrandResponse one(@PathVariable Long id){return service.brand(id);}
 @GetMapping("/{id}/products") public Page<ProductResponse> products(@PathVariable Long id,@PageableDefault(size=20) Pageable p){return service.byBrand(id,p);}

}
