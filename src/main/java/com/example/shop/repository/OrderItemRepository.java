package com.example.shop.repository;
import com.example.shop.model.entity.*;
import com.example.shop.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.*;
import java.util.*;
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> { List<OrderItem> findByOrderId(Long orderId); }
