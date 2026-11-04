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
@RestController @RequiredArgsConstructor @RequestMapping("/api/products")
public class ProductController {
 private final CatalogService service;

 @GetMapping public Page<ProductResponse> list(@RequestParam(required=false) Long category,@RequestParam(required=false) Long brand,@RequestParam(required=false) BigDecimal minPrice,@RequestParam(required=false) BigDecimal maxPrice,@RequestParam(required=false) Boolean inStock,@RequestParam(required=false) BigDecimal rating,@RequestParam(required=false) String search,@PageableDefault(size=20,sort="id") Pageable page){return service.search(category,brand,minPrice,maxPrice,inStock,rating,search,null,null,page);}
 @GetMapping("/search") public Page<ProductResponse> search(@RequestParam String q,@PageableDefault(size=20,sort="id") Pageable page){return service.search(null,null,null,null,null,null,q,null,null,page);}
 @GetMapping("/featured") public Page<ProductResponse> featured(@PageableDefault(size=20) Pageable page){return service.search(null,null,null,null,null,null,null,true,null,page);}
 @GetMapping("/new-arrivals") public Page<ProductResponse> newArrivals(@PageableDefault(size=20) Pageable page){return service.search(null,null,null,null,null,null,null,null,true,page);}
 @GetMapping("/best-sellers") public Page<ProductResponse> bestSellers(@PageableDefault(size=20,sort="salesCount",direction=Sort.Direction.DESC) Pageable page){return service.search(null,null,null,null,null,null,null,null,null,page);}
 @GetMapping("/{id}") public ProductResponse one(@PathVariable Long id){return service.one(id);}
 @GetMapping("/{id}/variants") public Page<VariantResponse> variants(@PathVariable Long id,@PageableDefault(size=20) Pageable page){return com.example.shop.config.PageUtil.fromList(service.variants(id),page);}
 @GetMapping("/{id}/images") public Page<ImageResponse> images(@PathVariable Long id,@PageableDefault(size=20) Pageable page){return com.example.shop.config.PageUtil.fromList(service.images(id),page);}
 @GetMapping("/{id}/specifications") public Page<SpecificationResponse> specs(@PathVariable Long id,@PageableDefault(size=20) Pageable page){return com.example.shop.config.PageUtil.fromList(service.specifications(id),page);}
 @GetMapping("/{id}/related") public Page<ProductResponse> related(@PathVariable Long id,@PageableDefault(size=10) Pageable page){return service.byCategory(service.getProduct(id).getCategory().getId(),page);}

}
