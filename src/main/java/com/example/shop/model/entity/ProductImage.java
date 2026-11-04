package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="product_images") @Getter @Setter @NoArgsConstructor
public class ProductImage {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String imageUrl;
  private String thumbnailUrl;
  private String altText;
  @Column(nullable=false) private Boolean isPrimary = false;
  @Column(nullable=false) private Integer sortOrder = 0;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id", nullable=false) private Product product;
}
