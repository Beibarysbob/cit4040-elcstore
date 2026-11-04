package com.example.shop.repository;
import com.example.shop.model.entity.*;
import com.example.shop.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.*;
import java.util.*;
public interface OrderRepository extends JpaRepository<Order,Long> { Page<Order> findByUserId(Long userId, Pageable pageable); Optional<Order> findByIdAndUserId(Long id, Long userId); Optional<Order> findByOrderNumber(String number); boolean existsByUserIdAndStatusAndItemsProductId(Long userId, OrderStatus status, Long productId); }
