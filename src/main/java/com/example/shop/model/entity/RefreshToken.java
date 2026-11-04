package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="refresh_tokens") @Getter @Setter @NoArgsConstructor
public class RefreshToken {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false, unique=true) private String token;
  @Column(nullable=false) private LocalDateTime expiryDate;
  @Column(nullable=false) private Boolean revoked = false;
  @Column(nullable=false) private LocalDateTime createdAt;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=false) private User user;
  @PrePersist void onCreate() { if (createdAt == null) createdAt = LocalDateTime.now(); updatedTimestamp(); }
  private void updatedTimestamp() {}
}
