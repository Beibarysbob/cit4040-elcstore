package com.example.shop.repository;
import com.example.shop.model.entity.*;
import com.example.shop.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.*;
import java.util.*;
public interface UserRepository extends JpaRepository<User,Long> { Optional<User> findByEmailIgnoreCase(String email); boolean existsByEmailIgnoreCase(String email); }
