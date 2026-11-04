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
@RestController @RequiredArgsConstructor @RequestMapping("/api/admin/categories")
public class AdminCategoryController {
 private final CatalogService service;

 @PostMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest r){CategoryResponse c=service.createCategory(r);return ResponseEntity.created(URI.create("/api/categories/"+c.id())).body(c);}
 @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public CategoryResponse update(@PathVariable Long id,@Valid @RequestBody CategoryRequest r){return service.updateCategory(id,r);}
 @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<Void> delete(@PathVariable Long id){service.deleteCategory(id);return ResponseEntity.noContent().build();}
 @PutMapping("/reorder") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<Void> reorder(@Valid @RequestBody ReorderCategoriesRequest r){service.reorder(r);return ResponseEntity.noContent().build();}

}
