package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="products") @Getter @Setter @NoArgsConstructor
public class Product {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String name;
  @Column(nullable=false, unique=true) private String slug;
  @Column(columnDefinition="text") private String description;
  @Column(columnDefinition="text") private String shortDescription;
  @Column(nullable=false, unique=true) private String sku;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal basePrice;
  @Column(precision=14, scale=2) private BigDecimal discountPrice;
  private LocalDateTime discountStartDate;
  private LocalDateTime discountEndDate;
  @Column(nullable=false) private Integer stockQuantity = 0;
  @Column(nullable=false) private Integer reservedQuantity = 0;
  @Column(nullable=false) private Integer lowStockThreshold = 5;
  @Column(precision=14, scale=2) private BigDecimal weight;
  private String dimensions;
  @Column(nullable=false) private Boolean isActive = true;
  @Column(nullable=false) private Boolean isFeatured = false;
  @Column(nullable=false) private Boolean isNewArrival = false;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal averageRating = BigDecimal.ZERO;
  @Column(nullable=false) private Integer reviewCount = 0;
  @Column(nullable=false) private Integer viewCount = 0;
  @Column(nullable=false) private Integer salesCount = 0;
  private String metaTitle;
  @Column(columnDefinition="text") private String metaDescription;
  @Column(nullable=false) private LocalDateTime createdAt;
  @Column(nullable=false) private LocalDateTime updatedAt;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="brand_id", nullable=true) private Brand brand;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="category_id", nullable=false) private Category category;
  @OneToMany(mappedBy="product") private List<ProductImage> images = new ArrayList<>();
  @OneToMany(mappedBy="product") private List<ProductVariant> variants = new ArrayList<>();
  @OneToMany(mappedBy="product") private List<ProductSpecification> specifications = new ArrayList<>();
  @PrePersist void onCreate() { if (createdAt == null) createdAt = LocalDateTime.now(); updatedTimestamp(); }
  @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }
  private void updatedTimestamp() { if (updatedAt == null) updatedAt = LocalDateTime.now(); }
}
