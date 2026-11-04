package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="users") @Getter @Setter @NoArgsConstructor
public class User {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false, unique=true) private String email;
  @Column(nullable=false) private String password;
  @Column(nullable=false) private String firstName;
  @Column(nullable=false) private String lastName;
  private String phone;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private UserRole role;
  @Column(nullable=false) private Boolean enabled = true;
  @Column(nullable=false) private Boolean emailVerified = false;
  private LocalDateTime lastLoginAt;
  @Column(nullable=false) private LocalDateTime createdAt;
  @Column(nullable=false) private LocalDateTime updatedAt;
  @OneToMany(mappedBy="user") private List<Address> addresses = new ArrayList<>();
  @PrePersist void onCreate() { if (createdAt == null) createdAt = LocalDateTime.now(); updatedTimestamp(); }
  @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }
  private void updatedTimestamp() { if (updatedAt == null) updatedAt = LocalDateTime.now(); }
}
