package com.example.shop.service;
import com.example.shop.dto.ApiDto.*;
import com.example.shop.exception.ApiException;
import com.example.shop.mapper.ApiMapper;
import com.example.shop.model.entity.*;
import com.example.shop.model.enums.*;
import com.example.shop.repository.*;
import com.example.shop.security.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.util.*;
@Service @Transactional(noRollbackFor=ApiException.class)
public class PaymentService {
 private final PaymentRepository payments;private final OrderService orders;private final CurrentUser current;private final ApiMapper mapper;
 public PaymentService(PaymentRepository payments,OrderService orders,CurrentUser current,ApiMapper mapper){this.payments=payments;this.orders=orders;this.current=current;this.mapper=mapper;}
 public PaymentResponse process(PaymentRequest r){
  Order order=orders.own(r.orderId());if(order.getStatus()!=OrderStatus.CONFIRMED)throw ApiException.conflict("Order must be CONFIRMED for payment");
  if(payments.findByOrderId(order.getId()).filter(x->x.getStatus()==PaymentStatus.COMPLETED).isPresent())throw ApiException.conflict("Order already paid");
  Payment payment=payments.findByOrderId(order.getId()).orElseGet(Payment::new);payment.setOrder(order);payment.setPaymentMethod(r.paymentMethod());payment.setAmount(order.getTotalAmount());payment.setCurrency("USD");payment.setStatus(PaymentStatus.PROCESSING);payment.setTransactionId("TX-"+UUID.randomUUID());
  if(r.paymentMethod()==PaymentMethod.CARD){
   String card=r.cardNumber();if(!CardRules.validLuhn(card))throw ApiException.bad("Card number failed Luhn validation");
   payment.setCardLastFourDigits(card.substring(card.length()-4));
   // Only test numbers enter this simulator. Never persist/log the full PAN.
   try{Thread.sleep(1000+java.util.concurrent.ThreadLocalRandom.current().nextInt(2001));}catch(InterruptedException ex){Thread.currentThread().interrupt();throw ApiException.conflict("Payment interrupted");}
   String scenario=CardRules.scenario(card);
   if(!"SUCCESS".equals(scenario)){payment.setStatus(PaymentStatus.FAILED);payment.setErrorCode(scenario);payment.setErrorMessage("Simulated payment decline");payments.save(payment);throw new ApiException(HttpStatus.PAYMENT_REQUIRED,"PAYMENT_FAILED",scenario);}
  }
  payment.setErrorCode(null);payment.setErrorMessage(null);payment.setStatus(PaymentStatus.COMPLETED);payment.setPaidAt(java.time.LocalDateTime.now());payments.save(payment);orders.paidAfterPayment(order);return mapper.payment(payment);
 }
 public PaymentResponse status(Long orderId){Order order=orders.own(orderId);return payments.findByOrderId(order.getId()).map(mapper::payment).orElseThrow(()->ApiException.notFound("Payment not found"));}
 public List<String> methods(){return List.of("CARD","PAYPAL","CASH_ON_DELIVERY","BONUS_POINTS");}
}
