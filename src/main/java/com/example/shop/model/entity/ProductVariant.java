package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="product_variants") @Getter @Setter @NoArgsConstructor
public class ProductVariant {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false, unique=true) private String sku;
  @Column(nullable=false) private String name;
  @Column(columnDefinition="text") private String attributes;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal price;
  @Column(nullable=false) private Integer stockQuantity = 0;
  @Column(nullable=false) private Integer reservedQuantity = 0;
  private String imageUrl;
  @Column(nullable=false) private Boolean isActive = true;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id", nullable=false) private Product product;
}
