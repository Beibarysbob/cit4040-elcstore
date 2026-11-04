package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="wishlists") @Getter @Setter @NoArgsConstructor
public class Wishlist {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal priceAtAdd;
  @Column(nullable=false) private Boolean notifyOnPriceDrop = false;
  @Column(nullable=false) private Boolean notifyOnBackInStock = false;
  @Column(nullable=false) private LocalDateTime addedAt;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=false) private User user;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id", nullable=false) private Product product;
  @PrePersist void onCreate() { if (addedAt == null) addedAt = LocalDateTime.now(); }
}
