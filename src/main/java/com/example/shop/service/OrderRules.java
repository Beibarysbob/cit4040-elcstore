package com.example.shop.service;
import com.example.shop.model.enums.OrderStatus;
import java.util.*;
public final class OrderRules {
 private OrderRules(){}
 private static final Map<OrderStatus,Set<OrderStatus>> ALLOWED=Map.of(
  OrderStatus.PENDING,Set.of(OrderStatus.CONFIRMED,OrderStatus.CANCELLED),
  OrderStatus.CONFIRMED,Set.of(OrderStatus.PAID,OrderStatus.CANCELLED),
  OrderStatus.PAID,Set.of(OrderStatus.PROCESSING,OrderStatus.REFUNDED),
  OrderStatus.PROCESSING,Set.of(OrderStatus.SHIPPED,OrderStatus.CANCELLED),
  OrderStatus.SHIPPED,Set.of(OrderStatus.IN_TRANSIT),
  OrderStatus.IN_TRANSIT,Set.of(OrderStatus.DELIVERED),
  OrderStatus.DELIVERED,Set.of(OrderStatus.REFUNDED));
 public static boolean allowed(OrderStatus from,OrderStatus to){return ALLOWED.getOrDefault(from,Set.of()).contains(to);}
}
