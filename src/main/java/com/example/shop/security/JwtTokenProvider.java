package com.example.shop.security;
import com.example.shop.model.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class JwtTokenProvider {
 private final SecretKey key; private final Duration accessTtl; private final Duration refreshTtl;
 public JwtTokenProvider(@Value("${jwt.secret}") String secret,@Value("${jwt.access-token-expiration}") Duration accessTtl,@Value("${jwt.refresh-token-expiration}") Duration refreshTtl){
  if(secret.length()<32)throw new IllegalArgumentException("JWT_SECRET must have at least 32 bytes");
  this.key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));this.accessTtl=accessTtl;this.refreshTtl=refreshTtl;
 }
 public String access(User u){return create(u,"access",accessTtl);}
 public String refresh(User u){return create(u,"refresh",refreshTtl);}
 private String create(User u,String type,Duration ttl){Instant now=Instant.now();return Jwts.builder().subject(u.getEmail()).claim("type",type).claim("uid",u.getId()).claim("role",u.getRole().name()).id(UUID.randomUUID().toString()).issuedAt(Date.from(now)).expiration(Date.from(now.plus(ttl))).signWith(key).compact();}
 public Claims parse(String jwt){return Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload();}
 public Duration refreshTtl(){return refreshTtl;}
}
