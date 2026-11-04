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
@RestController @RequiredArgsConstructor @RequestMapping("/api/categories")
public class CategoryController {
 private final CatalogService service;

 @GetMapping public Page<CategoryResponse> list(@PageableDefault(size=20,sort="sortOrder") Pageable page){return service.categories(page);}
 @GetMapping("/{id}") public CategoryResponse one(@PathVariable Long id){return service.category(id);}
 @GetMapping("/slug/{slug}") public CategoryResponse slug(@PathVariable String slug){return service.categorySlug(slug);}
 @GetMapping("/{id}/products") public Page<ProductResponse> products(@PathVariable Long id,@PageableDefault(size=20) Pageable page){return service.byCategory(id,page);}

}
