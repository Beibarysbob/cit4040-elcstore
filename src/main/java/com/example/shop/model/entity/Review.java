package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="reviews") @Getter @Setter @NoArgsConstructor
public class Review {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private Integer rating;
  @Column(nullable=false) private String title;
  @Column(columnDefinition="text") private String comment;
  @Column(columnDefinition="text") private String pros;
  @Column(columnDefinition="text") private String cons;
  @Column(columnDefinition="text") private String images;
  @Column(nullable=false) private Boolean isVerifiedPurchase = false;
  @Column(nullable=false) private Integer helpfulCount = 0;
  @Column(nullable=false) private Integer notHelpfulCount = 0;
  @Column(columnDefinition="text") private String adminReply;
  private LocalDateTime adminReplyAt;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private ReviewStatus status;
  @Column(nullable=false) private LocalDateTime createdAt;
  @Column(nullable=false) private LocalDateTime updatedAt;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id", nullable=false) private Product product;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=false) private User user;
  @PrePersist void onCreate() { if (createdAt == null) createdAt = LocalDateTime.now(); updatedTimestamp(); }
  @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }
  private void updatedTimestamp() { if (updatedAt == null) updatedAt = LocalDateTime.now(); }
}
