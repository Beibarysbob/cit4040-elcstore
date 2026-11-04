package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="cart_items") @Getter @Setter @NoArgsConstructor
public class CartItem {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private Integer quantity;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal priceAtAdd;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="cart_id", nullable=false) private Cart cart;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id", nullable=false) private Product product;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="variant_id", nullable=true) private ProductVariant variant;
}
