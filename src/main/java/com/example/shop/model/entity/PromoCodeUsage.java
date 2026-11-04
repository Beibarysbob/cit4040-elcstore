package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="promo_code_usages") @Getter @Setter @NoArgsConstructor
public class PromoCodeUsage {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal discountApplied;
  @Column(nullable=false) private LocalDateTime usedAt;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="promo_code_id", nullable=false) private PromoCode promoCode;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=false) private User user;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="order_id", nullable=false) private Order order;
  @PrePersist void onCreate() { if (usedAt == null) usedAt = LocalDateTime.now(); }
}
