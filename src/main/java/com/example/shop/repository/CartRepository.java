package com.example.shop.repository;
import com.example.shop.model.entity.*;
import com.example.shop.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.*;
import java.util.*;
public interface CartRepository extends JpaRepository<Cart,Long> { Optional<Cart> findByUserId(Long userId); Optional<Cart> findBySessionId(String sessionId); }
