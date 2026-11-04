package com.example.shop.security;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
 private final JwtTokenProvider tokens; private final CustomUserDetailsService users;
 public JwtAuthenticationFilter(JwtTokenProvider tokens, CustomUserDetailsService users){this.tokens=tokens;this.users=users;}
 @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)throws ServletException,IOException{
  String header=request.getHeader("Authorization");
  if(header!=null && header.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication()==null){
   try {var claims=tokens.parse(header.substring(7));if("access".equals(claims.get("type",String.class))) {
    UserDetails user=users.loadUserByUsername(claims.getSubject());if(user.isEnabled()){
     var authentication=new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());SecurityContextHolder.getContext().setAuthentication(authentication);
    }
   }}catch(JwtException|IllegalArgumentException|org.springframework.security.core.userdetails.UsernameNotFoundException ignored){}
  }
  chain.doFilter(request,response);
 }
}
