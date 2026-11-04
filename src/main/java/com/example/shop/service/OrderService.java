package com.example.shop.service;
import com.example.shop.dto.ApiDto.*;
import com.example.shop.exception.ApiException;
import com.example.shop.mapper.ApiMapper;
import com.example.shop.model.entity.*;
import com.example.shop.model.enums.*;
import com.example.shop.repository.*;
import com.example.shop.security.CurrentUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*;
import java.util.*;
import java.math.*;
import java.time.*;
@Service @Transactional
public class OrderService {
 private final OrderRepository orders;private final OrderItemRepository orderItems;private final CartService carts;private final CartItemRepository cartItems;private final UserService users;private final CurrentUser current;private final DeliveryService delivery;private final PromoService promos;private final ApiMapper mapper;private final ObjectMapper json;
 public OrderService(OrderRepository orders,OrderItemRepository orderItems,CartService carts,CartItemRepository cartItems,UserService users,CurrentUser current,DeliveryService delivery,PromoService promos,ApiMapper mapper,ObjectMapper json){this.orders=orders;this.orderItems=orderItems;this.carts=carts;this.cartItems=cartItems;this.users=users;this.current=current;this.delivery=delivery;this.promos=promos;this.mapper=mapper;this.json=json;}
 public OrderResponse create(OrderRequest r){
  User user=current.get();Cart cart=carts.get();var cartRows=cartItems.findByCartId(cart.getId());if(cartRows.isEmpty())throw ApiException.bad("Cannot order from an empty cart");
  Address addr=users.own(r.addressId());DeliveryOption method=delivery.find(r.deliveryCode());BigDecimal subtotal=BigDecimal.ZERO;
  for(CartItem row:cartRows){carts.checkStock(row.getProduct(),row.getVariant(),row.getQuantity());subtotal=subtotal.add(carts.currentPrice(row).multiply(BigDecimal.valueOf(row.getQuantity())));}
  PromoCode promo=null;BigDecimal discount=BigDecimal.ZERO;
  if(cart.getAppliedPromoCode()!=null){promo=promos.require(cart.getAppliedPromoCode(),subtotal,user.getId());discount=promos.discount(promo,promos.eligibleSubtotal(promo,cartRows,carts::currentPrice));}
  BigDecimal shipping=promo!=null && promo.getDiscountType()==DiscountType.FREE_SHIPPING ? BigDecimal.ZERO : delivery.cost(method,subtotal); // threshold uses subtotal BEFORE discount
  int points=r.requestedBonusPoints()==null?0:r.requestedBonusPoints();
  // Bonus account is an optional module; points cannot be spent in the required core.
  if(points>0)throw ApiException.bad("Bonus points are not enabled in the required core");
  BigDecimal total=subtotal.subtract(discount).add(shipping).setScale(2,RoundingMode.HALF_UP);
  Order order=new Order();order.setUser(user);order.setOrderNumber("ORD-"+UUID.randomUUID().toString().substring(0,12).toUpperCase());order.setStatus(OrderStatus.PENDING);order.setDeliveryMethod(DeliveryMethod.valueOf(method.getCode()));order.setEstimatedDeliveryDate(LocalDate.now().plusDays(method.getEstimatedDaysMax()));order.setCustomerNote(r.customerNote());order.setSubtotal(subtotal);order.setDiscount(discount);order.setShippingCost(shipping);order.setTotalAmount(total);order.setPromoCode(promo==null?null:promo.getCode());
  ShippingAddress snapshot=new ShippingAddress();snapshot.setRecipientName(addr.getRecipientName());snapshot.setRecipientPhone(addr.getRecipientPhone());snapshot.setCountry(addr.getCountry());snapshot.setCity(addr.getCity());snapshot.setStreet(addr.getStreet());snapshot.setBuilding(addr.getBuilding());snapshot.setApartment(addr.getApartment());snapshot.setPostalCode(addr.getPostalCode());order.setShippingAddress(snapshot);
  orders.save(order);
  for(CartItem row:cartRows){
   Product product=row.getProduct();ProductVariant variant=row.getVariant();int quantity=row.getQuantity();BigDecimal price=carts.currentPrice(row);
   OrderItem oi=new OrderItem();oi.setOrder(order);oi.setProduct(product);oi.setVariant(variant);oi.setPriceAtPurchase(price);oi.setQuantity(quantity);
   try{oi.setProductSnapshot(json.writeValueAsString(Map.of("name",product.getName(),"sku",product.getSku(),"price",price)));}catch(com.fasterxml.jackson.core.JsonProcessingException ex){throw new IllegalStateException("Snapshot serialization failed",ex);}
   orderItems.save(oi);order.getItems().add(oi);
   product.setReservedQuantity(product.getReservedQuantity()+quantity);
   if(variant!=null)variant.setReservedQuantity(variant.getReservedQuantity()+quantity);
  }
  if(promo!=null)promos.registerUsage(promo,user,order,discount);
  cartItems.deleteAll(cartRows);cart.setAppliedPromoCode(null);
  return mapper.order(order);
 }
 public Order own(Long id){return orders.findByIdAndUserId(id,current.get().getId()).orElseThrow(()->ApiException.notFound("Order not found"));}
 public Order any(Long id){return orders.findById(id).orElseThrow(()->ApiException.notFound("Order not found"));}
 public OrderResponse one(Long id){return mapper.order(own(id));}
 public OrderResponse byNumber(String number){Order order=orders.findByOrderNumber(number).orElseThrow(()->ApiException.notFound("Order not found"));if(!order.getUser().getId().equals(current.get().getId()))throw ApiException.notFound("Order not found");return mapper.order(order);}
 public Page<OrderResponse> mine(Pageable pageable){return orders.findByUserId(current.get().getId(),pageable).map(mapper::order);}
 public Page<OrderResponse> all(Pageable pageable){return orders.findAll(pageable).map(mapper::order);}
 public OrderResponse cancel(Long id){Order order=own(id);transition(order,OrderStatus.CANCELLED);return mapper.order(order);}
 public OrderResponse reorder(Long id){Order previous=own(id);for(OrderItem i:previous.getItems())carts.add(new CartItemRequest(i.getProduct().getId(),i.getVariant()==null?null:i.getVariant().getId(),i.getQuantity()));return create(new OrderRequest("COURIER",defaultAddressId(),"Repeat of "+previous.getOrderNumber(),0));}
 private Long defaultAddressId(){return users.addresses().stream().filter(AddressResponse::isDefault).findFirst().or(()->users.addresses().stream().findFirst()).map(AddressResponse::id).orElseThrow(()->ApiException.bad("Create an address before reordering"));}
 public OrderResponse adminStatus(Long id,OrderStatus status){Order order=any(id);transition(order,status);return mapper.order(order);}
 public OrderResponse tracking(Long id,TrackingRequest r){Order order=any(id);order.setDeliveryTrackingNumber(r.trackingNumber()==null||r.trackingNumber().isBlank()?"TRK-"+UUID.randomUUID().toString().substring(0,8).toUpperCase():r.trackingNumber());return mapper.order(order);}
 public OrderResponse note(Long id,AdminNoteRequest r){Order order=any(id);order.setAdminNote(r.note());return mapper.order(order);}
 public void transition(Order order,OrderStatus next){
  OrderStatus from=order.getStatus();if(!OrderRules.allowed(from,next))throw ApiException.conflict("Illegal order transition: "+from+" -> "+next);
  if(next==OrderStatus.PAID)throw ApiException.conflict("Only a successful payment can mark an order PAID");
  // A delivered order may only be refunded by the OPTIONAL returns workflow.
  if(next==OrderStatus.REFUNDED)throw ApiException.conflict("Refund requires the returns workflow");
  order.setStatus(next);
  if(next==OrderStatus.CANCELLED){for(OrderItem i:order.getItems())release(i);}
  if(next==OrderStatus.SHIPPED){order.setShippedAt(LocalDateTime.now());if(order.getDeliveryTrackingNumber()==null)order.setDeliveryTrackingNumber("TRK-"+UUID.randomUUID().toString().substring(0,8).toUpperCase());}
  if(next==OrderStatus.DELIVERED){order.setDeliveredAt(LocalDateTime.now());order.setActualDeliveryDate(LocalDate.now());for(OrderItem i:order.getItems())fulfil(i);}
 }
 public void paidAfterPayment(Order order){if(order.getStatus()!=OrderStatus.CONFIRMED)throw ApiException.conflict("Order must be CONFIRMED before payment");order.setStatus(OrderStatus.PAID);order.setPaidAt(LocalDateTime.now());}
 private void release(OrderItem i){Product p=i.getProduct();p.setReservedQuantity(Math.max(0,p.getReservedQuantity()-i.getQuantity()));if(i.getVariant()!=null){var v=i.getVariant();v.setReservedQuantity(Math.max(0,v.getReservedQuantity()-i.getQuantity()));}}
 private void fulfil(OrderItem i){Product p=i.getProduct();p.setReservedQuantity(Math.max(0,p.getReservedQuantity()-i.getQuantity()));p.setStockQuantity(p.getStockQuantity()-i.getQuantity());p.setSalesCount(p.getSalesCount()+i.getQuantity());if(i.getVariant()!=null){var v=i.getVariant();v.setReservedQuantity(Math.max(0,v.getReservedQuantity()-i.getQuantity()));v.setStockQuantity(v.getStockQuantity()-i.getQuantity());}}
}
