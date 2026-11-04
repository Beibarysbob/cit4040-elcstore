package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="payments") @Getter @Setter @NoArgsConstructor
public class Payment {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(unique=true) private String transactionId;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private PaymentMethod paymentMethod;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private PaymentStatus status;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal amount;
  @Column(nullable=false) private String currency;
  @Column(nullable=false) private Integer bonusPointsUsed = 0;
  private String cardLastFourDigits;
  private LocalDateTime paidAt;
  private String errorMessage;
  private String errorCode;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal refundedAmount = BigDecimal.ZERO;
  @Column(columnDefinition="text") private String metadata;
  @OneToOne @JoinColumn(name="order_id", nullable=false) private Order order;
}
