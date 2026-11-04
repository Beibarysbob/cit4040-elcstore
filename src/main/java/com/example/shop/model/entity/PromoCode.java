package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="promo_codes") @Getter @Setter @NoArgsConstructor
public class PromoCode {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false, unique=true) private String code;
  @Column(columnDefinition="text") private String description;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private DiscountType discountType;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal discountValue;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal minOrderAmount = BigDecimal.ZERO;
  @Column(precision=14, scale=2) private BigDecimal maxDiscountAmount;
  private Integer maxUsageCount;
  @Column(nullable=false) private Integer currentUsageCount = 0;
  @Column(nullable=false) private Integer maxUsagePerUser;
  @Column(nullable=false) private LocalDateTime validFrom;
  @Column(nullable=false) private LocalDateTime validTo;
  @Column(nullable=false) private Boolean isActive = true;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="created_by_id", nullable=true) private User createdBy;
  @ManyToMany @JoinTable(name="promo_applicable_categories",joinColumns=@JoinColumn(name="promo_id"),inverseJoinColumns=@JoinColumn(name="category_id"))
  private List<Category> applicableCategories = new ArrayList<>();
  @ManyToMany @JoinTable(name="promo_applicable_products",joinColumns=@JoinColumn(name="promo_id"),inverseJoinColumns=@JoinColumn(name="product_id"))
  private List<Product> applicableProducts = new ArrayList<>();
}
