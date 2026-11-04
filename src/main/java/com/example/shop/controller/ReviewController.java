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
@RestController @RequiredArgsConstructor @RequestMapping("/api/products/{productId}/reviews")
public class ReviewController {
 private final ReviewService reviews;

 @GetMapping public Page<ReviewResponse> list(@PathVariable Long productId,@PageableDefault(size=20) Pageable p){return reviews.list(productId,p);}
 @GetMapping("/summary") public ReviewSummary summary(@PathVariable Long productId){return reviews.summary(productId);}
 @PostMapping public ResponseEntity<ReviewResponse> create(@PathVariable Long productId,@Valid @RequestBody ReviewRequest r){var x=reviews.create(productId,r);return ResponseEntity.created(URI.create("/api/products/"+productId+"/reviews/"+x.id())).body(x);}
 @PutMapping("/{id}") public ReviewResponse update(@PathVariable Long productId,@PathVariable Long id,@Valid @RequestBody ReviewRequest r){return reviews.update(productId,id,r);}
 @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long productId,@PathVariable Long id){reviews.delete(productId,id);return ResponseEntity.noContent().build();}

}
