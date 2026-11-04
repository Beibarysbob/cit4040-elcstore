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
@RestController @RequiredArgsConstructor @RequestMapping("/api/orders")
public class OrderController {
 private final OrderService orders;

 @PostMapping public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest r){OrderResponse o=orders.create(r);return ResponseEntity.created(URI.create("/api/orders/"+o.id())).body(o);}
 @GetMapping public Page<OrderResponse> mine(@PageableDefault(size=20,sort="createdAt",direction=Sort.Direction.DESC) Pageable p){return orders.mine(p);}
 @GetMapping("/{id}") public OrderResponse one(@PathVariable Long id){return orders.one(id);}
 @GetMapping("/number/{orderNumber}") public OrderResponse number(@PathVariable String orderNumber){return orders.byNumber(orderNumber);}
 @PutMapping("/{id}/cancel") public OrderResponse cancel(@PathVariable Long id){return orders.cancel(id);}
 @PostMapping("/{id}/reorder") public ResponseEntity<OrderResponse> reorder(@PathVariable Long id){var o=orders.reorder(id);return ResponseEntity.created(URI.create("/api/orders/"+o.id())).body(o);}

}
