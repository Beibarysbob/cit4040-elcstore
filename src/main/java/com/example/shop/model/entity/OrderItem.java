package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="order_items") @Getter @Setter @NoArgsConstructor
public class OrderItem {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private Integer quantity;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal priceAtPurchase;
  @Column(columnDefinition="text", nullable=false) private String productSnapshot;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="order_id", nullable=false) private Order order;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id", nullable=false) private Product product;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="variant_id", nullable=true) private ProductVariant variant;
}
