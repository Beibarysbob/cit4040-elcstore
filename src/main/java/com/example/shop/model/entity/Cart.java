package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="carts") @Getter @Setter @NoArgsConstructor
public class Cart {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(unique=true) private String sessionId;
  private String appliedPromoCode;
  @Column(nullable=false) private LocalDateTime createdAt;
  @Column(nullable=false) private LocalDateTime updatedAt;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=true) private User user;
  @OneToMany(mappedBy="cart", cascade=CascadeType.ALL, orphanRemoval=true) private List<CartItem> items = new ArrayList<>();
  @PrePersist void onCreate() { if (createdAt == null) createdAt = LocalDateTime.now(); updatedTimestamp(); }
  @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }
  private void updatedTimestamp() { if (updatedAt == null) updatedAt = LocalDateTime.now(); }
}
