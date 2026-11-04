package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="brands") @Getter @Setter @NoArgsConstructor
public class Brand {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String name;
  @Column(nullable=false, unique=true) private String slug;
  @Column(columnDefinition="text") private String description;
  private String logoUrl;
  private String websiteUrl;
  private String country;
  @Column(nullable=false) private Boolean isActive = true;
}
