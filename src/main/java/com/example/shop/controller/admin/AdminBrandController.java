package com.example.shop.controller.admin;
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
@RestController @RequiredArgsConstructor @RequestMapping("/api/admin/brands")
public class AdminBrandController {
 private final CatalogService service;

 @PostMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<BrandResponse> create(@Valid @RequestBody BrandRequest r){var x=service.createBrand(r);return ResponseEntity.created(URI.create("/api/brands/"+x.id())).body(x);}
 @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public BrandResponse update(@PathVariable Long id,@Valid @RequestBody BrandRequest r){return service.updateBrand(id,r);}
 @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<Void> delete(@PathVariable Long id){service.deleteBrand(id);return ResponseEntity.noContent().build();}

}
