package com.example.shop;
import com.example.shop.model.enums.OrderStatus;
import com.example.shop.service.CardRules;
import com.example.shop.service.OrderRules;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class RulesTest {
 @Test void validAndInvalidTransitions() {
  assertTrue(OrderRules.allowed(OrderStatus.PENDING,OrderStatus.CONFIRMED));
  assertTrue(OrderRules.allowed(OrderStatus.CONFIRMED,OrderStatus.PAID));
  assertFalse(OrderRules.allowed(OrderStatus.DELIVERED,OrderStatus.PENDING));
  assertFalse(OrderRules.allowed(OrderStatus.CANCELLED,OrderStatus.PAID));
 }
 @Test void luhnAndScenario() {
  assertTrue(CardRules.validLuhn("4111111111111111"));
  assertFalse(CardRules.validLuhn("4111111111111112"));
  assertEquals("INSUFFICIENT_FUNDS",CardRules.scenario("4000000000000002"));
  assertEquals("NETWORK_ERROR",CardRules.scenario("4000000000000069"));
  assertEquals("FRAUD_SUSPECTED",CardRules.scenario("4000000000000077"));
 }
}
