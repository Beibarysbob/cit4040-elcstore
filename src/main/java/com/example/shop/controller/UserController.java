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
@RestController @RequiredArgsConstructor @RequestMapping("/api/users")
public class UserController {
 private final UserService users;private final AuthService auth;

 @GetMapping("/me") public UserResponse me(){return users.me();}
 @PutMapping("/me") public UserResponse update(@Valid @RequestBody UpdateUserRequest r){return users.update(r);}
 @PutMapping("/me/password") public ResponseEntity<Void> change(@Valid @RequestBody ChangePasswordRequest r){auth.changePassword(r);return ResponseEntity.noContent().build();}
 @DeleteMapping("/me") public ResponseEntity<Void> delete(){users.disable();return ResponseEntity.noContent().build();}
 @GetMapping("/me/addresses") public Page<AddressResponse> addresses(@PageableDefault(size=20) Pageable page){return com.example.shop.config.PageUtil.fromList(users.addresses(),page);}
 @PostMapping("/me/addresses") public ResponseEntity<AddressResponse> create(@Valid @RequestBody AddressRequest r){AddressResponse a=users.create(r);return ResponseEntity.created(URI.create("/api/users/me/addresses/"+a.id())).body(a);}
 @PutMapping("/me/addresses/{id}") public AddressResponse updateAddress(@PathVariable Long id,@Valid @RequestBody AddressRequest r){return users.update(id,r);}
 @DeleteMapping("/me/addresses/{id}") public ResponseEntity<Void> deleteAddress(@PathVariable Long id){users.delete(id);return ResponseEntity.noContent().build();}
 @PutMapping("/me/addresses/{id}/default") public AddressResponse setDefault(@PathVariable Long id){return users.setDefault(id);}

}
