package com.example.shop.repository;
import com.example.shop.model.entity.*;
import com.example.shop.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.*;
import java.util.*;
public interface ProductImageRepository extends JpaRepository<ProductImage,Long> { List<ProductImage> findByProductIdOrderBySortOrderAsc(Long productId); }
