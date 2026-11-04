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
@RestController @RequiredArgsConstructor @RequestMapping("/api/wishlist")
public class WishlistController {
 private final CartService carts;

 @GetMapping public Page<WishlistResponse> list(@PageableDefault(size=20) Pageable p){return carts.wishlist(p);}
 @PostMapping("/{productId}") public ResponseEntity<Void> add(@PathVariable Long productId){carts.addWishlist(productId);return ResponseEntity.created(URI.create("/api/wishlist/"+productId)).build();}
 @DeleteMapping("/{productId}") public ResponseEntity<Void> remove(@PathVariable Long productId){carts.removeWishlist(productId);return ResponseEntity.noContent().build();}
 @PostMapping("/{productId}/move-to-cart") public CartResponse move(@PathVariable Long productId){return carts.moveToCart(productId);}

}
