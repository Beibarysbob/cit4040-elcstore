package com.example.shop.security;
import com.example.shop.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service
public class CustomUserDetailsService implements UserDetailsService {
 private final UserRepository repo;
 public CustomUserDetailsService(UserRepository repo){this.repo=repo;}
 @Override public UserDetails loadUserByUsername(String email){
  var u=repo.findByEmailIgnoreCase(email).orElseThrow(()->new UsernameNotFoundException("Account does not exist"));
  return org.springframework.security.core.userdetails.User.withUsername(u.getEmail()).password(u.getPassword()).roles(u.getRole().name()).disabled(!u.getEnabled()).build();
 }
}
