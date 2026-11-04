package com.example.shop.repository;
import com.example.shop.model.entity.*;
import com.example.shop.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.*;
import java.util.*;
public interface ReviewRepository extends JpaRepository<Review,Long> { Page<Review> findByProductIdAndStatus(Long productId, ReviewStatus status, Pageable pageable); List<Review> findByProductIdAndStatus(Long productId, ReviewStatus status); Optional<Review> findByIdAndUserId(Long id, Long userId); }
