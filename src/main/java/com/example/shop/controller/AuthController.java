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
@RestController @RequiredArgsConstructor @RequestMapping("/api/auth")
public class AuthController {
 private final AuthService auth;

 @PostMapping("/register") public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest r){return ResponseEntity.created(URI.create("/api/users/me")).body(auth.register(r));}
 @PostMapping("/login") public AuthResponse login(@Valid @RequestBody LoginRequest r){return auth.login(r);}
 @PostMapping("/refresh") public AuthResponse refresh(@Valid @RequestBody RefreshRequest r){return auth.rotate(r);}
 @PostMapping("/logout") public ResponseEntity<Void> logout(@Valid @RequestBody RefreshRequest r){auth.logout(r);return ResponseEntity.noContent().build();}
 @PostMapping("/change-password") public ResponseEntity<Void> change(@Valid @RequestBody ChangePasswordRequest r){auth.changePassword(r);return ResponseEntity.noContent().build();}

}
