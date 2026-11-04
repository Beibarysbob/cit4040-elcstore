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
@RestController @RequiredArgsConstructor @RequestMapping("/api/admin/products")
public class AdminProductController {
 private final CatalogService service;

 @PostMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest r){ProductResponse p=service.create(r);return ResponseEntity.created(URI.create("/api/products/"+p.id())).body(p);}
 @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ProductResponse update(@PathVariable Long id,@Valid @RequestBody ProductRequest r){return service.update(id,r);}
 @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<Void> delete(@PathVariable Long id){service.softDelete(id);return ResponseEntity.noContent().build();}
 @PatchMapping("/{id}/stock") @PreAuthorize("hasRole('ADMIN')") public ProductResponse stock(@PathVariable Long id,@Valid @RequestBody StockRequest r){return service.setStock(id,r.stockQuantity());}
 @PatchMapping("/{id}/activate") @PreAuthorize("hasRole('ADMIN')") public ProductResponse activate(@PathVariable Long id){return service.activate(id,true);}
 @PatchMapping("/{id}/deactivate") @PreAuthorize("hasRole('ADMIN')") public ProductResponse deactivate(@PathVariable Long id){return service.activate(id,false);}
 @PostMapping("/{id}/variants") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<VariantResponse> variant(@PathVariable Long id,@Valid @RequestBody VariantRequest r){VariantResponse v=service.createVariant(id,r);return ResponseEntity.created(URI.create("/api/products/"+id+"/variants")).body(v);}
 @PutMapping("/{id}/variants/{variantId}") @PreAuthorize("hasRole('ADMIN')") public VariantResponse updateVariant(@PathVariable Long id,@PathVariable Long variantId,@Valid @RequestBody VariantRequest r){return service.updateVariant(id,variantId,r);}
 @DeleteMapping("/{id}/variants/{variantId}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<Void> deleteVariant(@PathVariable Long id,@PathVariable Long variantId){service.deleteVariant(id,variantId);return ResponseEntity.noContent().build();}

}
