package com.example.shop.security;
import com.example.shop.exception.ApiException;
import com.example.shop.model.entity.User;
import com.example.shop.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
@Component
public class CurrentUser {
 private final UserRepository users;
 public CurrentUser(UserRepository users){this.users=users;}
 public User get(){
  var auth=SecurityContextHolder.getContext().getAuthentication();
  if(auth==null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal()))throw ApiException.unauthorized("Login required");
  return users.findByEmailIgnoreCase(auth.getName()).orElseThrow(()->ApiException.unauthorized("Account unavailable"));
 }
 public boolean staff(){return get().getRole()!=com.example.shop.model.enums.UserRole.CUSTOMER;}
}
