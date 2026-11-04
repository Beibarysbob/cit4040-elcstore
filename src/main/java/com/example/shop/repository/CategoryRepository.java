package com.example.shop.repository;
import com.example.shop.model.entity.*;
import com.example.shop.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.*;
import java.util.*;
public interface CategoryRepository extends JpaRepository<Category,Long> { Optional<Category> findBySlug(String slug); List<Category> findByParentCategoryIsNull(); }
