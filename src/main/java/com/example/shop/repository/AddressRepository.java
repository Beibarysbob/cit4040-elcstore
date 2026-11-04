package com.example.shop.repository;
import com.example.shop.model.entity.*;
import com.example.shop.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.*;
import java.util.*;
public interface AddressRepository extends JpaRepository<Address,Long> { List<Address> findByUserId(Long userId); Optional<Address> findByIdAndUserId(Long id, Long userId); }
