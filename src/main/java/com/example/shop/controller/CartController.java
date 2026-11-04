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
@RestController @RequiredArgsConstructor @RequestMapping("/api/cart")
public class CartController {
 private final CartService carts;

 @GetMapping public CartResponse one(){return carts.show();}
 @PostMapping("/items") public ResponseEntity<CartResponse> add(@Valid @RequestBody CartItemRequest r){return ResponseEntity.status(201).body(carts.add(r));}
 @PutMapping("/items/{itemId}") public CartResponse update(@PathVariable Long itemId,@Valid @RequestBody QuantityRequest r){return carts.update(itemId,r);}
 @DeleteMapping("/items/{itemId}") public ResponseEntity<Void> remove(@PathVariable Long itemId){carts.remove(itemId);return ResponseEntity.noContent().build();}
 @DeleteMapping public ResponseEntity<Void> clear(){carts.clear();return ResponseEntity.noContent().build();}
 @PostMapping("/apply-promo") public CartResponse apply(@Valid @RequestBody PromoRequest r){return carts.apply(r);}
 @DeleteMapping("/promo") public CartResponse removePromo(){return carts.removePromo();}
 @PostMapping("/merge") public CartResponse merge(@Valid @RequestBody MergeRequest r){return carts.merge(r);}
 @GetMapping("/guest") public CartResponse guest(@RequestParam String sessionId){return carts.showGuest(sessionId);}
 @PostMapping("/guest/items") public ResponseEntity<CartResponse> guestAdd(@RequestParam String sessionId,@Valid @RequestBody CartItemRequest r){return ResponseEntity.status(201).body(carts.add(carts.guest(sessionId),r));}

}
