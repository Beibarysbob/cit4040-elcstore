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
@RestController @RequiredArgsConstructor @RequestMapping("/api/admin/promo-codes")
public class AdminPromoCodeController {
 private final PromoService promos;

 @GetMapping @PreAuthorize("hasRole('ADMIN')") public Page<AdminPromoResponse> list(@PageableDefault(size=20) Pageable p){return promos.list(p);}
 @PostMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<AdminPromoResponse> create(@Valid @RequestBody AdminPromoRequest r){var p=promos.create(r);return ResponseEntity.created(URI.create("/api/admin/promo-codes/"+p.id())).body(p);}
 @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public AdminPromoResponse update(@PathVariable Long id,@Valid @RequestBody AdminPromoRequest r){return promos.update(id,r);}
 @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<Void> delete(@PathVariable Long id){promos.delete(id);return ResponseEntity.noContent().build();}

}
