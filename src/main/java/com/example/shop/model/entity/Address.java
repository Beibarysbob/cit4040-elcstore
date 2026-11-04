package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="addresses") @Getter @Setter @NoArgsConstructor
public class Address {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String title;
  @Column(nullable=false) private String country;
  @Column(nullable=false) private String city;
  @Column(nullable=false) private String street;
  @Column(nullable=false) private String building;
  private String apartment;
  private String postalCode;
  @Column(nullable=false) private String recipientName;
  @Column(nullable=false) private String recipientPhone;
  @Column(nullable=false) private Boolean isDefault = false;
  @Column(precision=14, scale=2) private BigDecimal latitude;
  @Column(precision=14, scale=2) private BigDecimal longitude;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=false) private User user;
}
