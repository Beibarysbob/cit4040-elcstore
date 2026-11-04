package com.example.shop.service;
import com.example.shop.dto.ApiDto.*;
import com.example.shop.exception.ApiException;
import com.example.shop.model.entity.*;
import com.example.shop.model.enums.*;
import com.example.shop.repository.*;
import com.example.shop.security.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;

@Service @Transactional
public class AuthService {
 private final UserRepository users;private final RefreshTokenRepository refreshTokens;private final PasswordEncoder encoder;private final JwtTokenProvider jwt;private final CurrentUser current;
 public AuthService(UserRepository users,RefreshTokenRepository refreshTokens,PasswordEncoder encoder,JwtTokenProvider jwt,CurrentUser current){this.users=users;this.refreshTokens=refreshTokens;this.encoder=encoder;this.jwt=jwt;this.current=current;}
 public AuthResponse register(RegisterRequest r){
  if(users.existsByEmailIgnoreCase(r.email()))throw ApiException.conflict("Email already registered");
  User user=new User();user.setEmail(r.email().strip().toLowerCase());user.setPassword(encoder.encode(r.password()));user.setFirstName(r.firstName());user.setLastName(r.lastName());user.setPhone(r.phone());user.setRole(UserRole.CUSTOMER);user.setEnabled(true);user.setEmailVerified(false);
  return tokens(users.save(user));
 }
 public AuthResponse login(LoginRequest r){
  User u=users.findByEmailIgnoreCase(r.email()).orElseThrow(()->ApiException.unauthorized("Invalid credentials"));
  if(!u.getEnabled() || !encoder.matches(r.password(),u.getPassword()))throw ApiException.unauthorized("Invalid credentials");
  u.setLastLoginAt(LocalDateTime.now());return tokens(u);
 }
 public AuthResponse rotate(RefreshRequest r){
  var claims=checkRefresh(r.refreshToken());
  var record=refreshTokens.findByToken(r.refreshToken()).orElseThrow(()->ApiException.unauthorized("Unknown refresh token"));
  if(Boolean.TRUE.equals(record.getRevoked()) || record.getExpiryDate().isBefore(LocalDateTime.now()) || !record.getUser().getEmail().equals(claims.getSubject()))throw ApiException.unauthorized("Refresh token expired or revoked");
  record.setRevoked(true);return tokens(record.getUser());
 }
 public void logout(RefreshRequest r){
  refreshTokens.findByToken(r.refreshToken()).ifPresent(t->{t.setRevoked(true);});
 }
 public void changePassword(ChangePasswordRequest r){
  User u=current.get();if(!encoder.matches(r.oldPassword(),u.getPassword()))throw ApiException.unauthorized("Incorrect current password");
  u.setPassword(encoder.encode(r.newPassword()));
  // invalidate all previous refresh tokens (including tokens from other devices)
  for(RefreshToken token: refreshTokens.findAll())if(token.getUser().getId().equals(u.getId()))token.setRevoked(true);
 }
 private io.jsonwebtoken.Claims checkRefresh(String token){
  try {var claims=jwt.parse(token);if(!"refresh".equals(claims.get("type",String.class)))throw ApiException.unauthorized("Wrong token type");return claims;}
  catch(io.jsonwebtoken.JwtException | IllegalArgumentException ex){throw ApiException.unauthorized("Invalid refresh token");}
 }
 private AuthResponse tokens(User u){
  String refresh=jwt.refresh(u);RefreshToken entry=new RefreshToken();entry.setToken(refresh);entry.setUser(u);entry.setExpiryDate(LocalDateTime.now().plus(jwt.refreshTtl()));entry.setRevoked(false);refreshTokens.save(entry);
  return new AuthResponse(jwt.access(u),refresh,"Bearer",900);
 }
}
