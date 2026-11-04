package com.example.shop.repository;
import com.example.shop.model.entity.*;
import com.example.shop.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.*;
import java.util.*;
public interface CartItemRepository extends JpaRepository<CartItem,Long> { List<CartItem> findByCartId(Long cartId); }
