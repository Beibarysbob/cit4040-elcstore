package com.example.shop.repository;
import com.example.shop.model.entity.*;
import com.example.shop.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.*;
import java.util.*;
public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> { Optional<Product> findBySku(String sku); Optional<Product> findBySlug(String slug); }
