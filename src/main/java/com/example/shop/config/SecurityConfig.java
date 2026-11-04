package com.example.shop.config;
import com.example.shop.exception.ErrorBody;
import com.example.shop.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.Map;
@Configuration @EnableMethodSecurity
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean SecurityFilterChain filterChain(HttpSecurity http,JwtAuthenticationFilter jwtFilter,ObjectMapper mapper)throws Exception{
  return http.csrf(c->c.disable()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(a->a
    .requestMatchers("/api/auth/register","/api/auth/login","/api/auth/refresh","/api/auth/logout").permitAll()
    .requestMatchers(HttpMethod.GET,"/api/cart/guest").permitAll()
    .requestMatchers(HttpMethod.POST,"/api/cart/guest/items").permitAll()
    .requestMatchers(HttpMethod.GET,"/api/products/**","/api/categories/**","/api/brands/**","/api/delivery/methods","/api/payments/methods").permitAll()
    .requestMatchers("/api/admin/**").hasAnyRole("ADMIN","MANAGER")
    .anyRequest().authenticated())
   .exceptionHandling(e->e
    .authenticationEntryPoint((req,res,ex)->error(mapper,res,req.getRequestURI(),401,"UNAUTHORIZED","Login required"))
    .accessDeniedHandler((req,res,ex)->error(mapper,res,req.getRequestURI(),403,"FORBIDDEN","Access denied")))
   .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class).build();
 }
 private static void error(ObjectMapper mapper,jakarta.servlet.http.HttpServletResponse response,String path,int code,String label,String message)throws java.io.IOException{
  response.setStatus(code);response.setContentType(MediaType.APPLICATION_JSON_VALUE);
  mapper.writeValue(response.getOutputStream(),ErrorBody.of(code,label,message,path,Map.of()));
 }
}
