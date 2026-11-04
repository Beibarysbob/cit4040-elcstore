package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="delivery_options") @Getter @Setter @NoArgsConstructor
public class DeliveryOption {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String name;
  @Column(nullable=false, unique=true) private String code;
  @Column(columnDefinition="text") private String description;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal basePrice;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal freeThreshold;
  @Column(nullable=false) private Integer estimatedDaysMin;
  @Column(nullable=false) private Integer estimatedDaysMax;
  @Column(nullable=false) private Boolean isActive = true;
}
