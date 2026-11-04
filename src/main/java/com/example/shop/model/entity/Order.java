package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="shop_orders") @Getter @Setter @NoArgsConstructor
public class Order {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false, unique=true) private String orderNumber;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private OrderStatus status;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private DeliveryMethod deliveryMethod;
  private String deliveryTrackingNumber;
  private LocalDate estimatedDeliveryDate;
  private LocalDate actualDeliveryDate;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal subtotal;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal shippingCost;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal discount;
  @Column(nullable=false) private Integer bonusPointsUsed = 0;
  @Column(nullable=false) private Integer bonusPointsEarned = 0;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal totalAmount;
  private String promoCode;
  @Column(columnDefinition="text") private String customerNote;
  @Column(columnDefinition="text") private String adminNote;
  @Column(nullable=false) private LocalDateTime createdAt;
  @Column(nullable=false) private LocalDateTime updatedAt;
  private LocalDateTime paidAt;
  private LocalDateTime shippedAt;
  private LocalDateTime deliveredAt;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=false) private User user;
  @Embedded private ShippingAddress shippingAddress = new ShippingAddress();
  @OneToMany(mappedBy="order", cascade=CascadeType.ALL) private List<OrderItem> items = new ArrayList<>();
  @OneToOne(mappedBy="order") private Payment payment;
  @PrePersist void onCreate() { if (createdAt == null) createdAt = LocalDateTime.now(); updatedTimestamp(); }
  @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }
  private void updatedTimestamp() { if (updatedAt == null) updatedAt = LocalDateTime.now(); }
}
