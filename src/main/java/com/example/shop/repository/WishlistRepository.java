package com.example.shop.repository;
import com.example.shop.model.entity.*;
import com.example.shop.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.*;
import java.util.*;
public interface WishlistRepository extends JpaRepository<Wishlist,Long> { Page<Wishlist> findByUserId(Long userId, Pageable pageable); Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId); }
