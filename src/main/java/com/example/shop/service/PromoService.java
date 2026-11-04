package com.example.shop.service;
import com.example.shop.dto.ApiDto.*;
import com.example.shop.exception.ApiException;
import com.example.shop.mapper.ApiMapper;
import com.example.shop.model.entity.*;
import com.example.shop.model.enums.*;
import com.example.shop.repository.*;
import com.example.shop.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*;
import java.math.*;
import java.time.*;
import java.util.*;
import java.util.function.Function;
@Service @Transactional
public class PromoService {
 private final PromoCodeRepository promos;private final PromoCodeUsageRepository usages;private final CurrentUser current;private final ApiMapper mapper;
 public PromoService(PromoCodeRepository promos,PromoCodeUsageRepository usages,CurrentUser current,ApiMapper mapper){this.promos=promos;this.usages=usages;this.current=current;this.mapper=mapper;}
 public PromoCode require(String code,BigDecimal subtotal,Long userId){
  PromoCode p=promos.findByCodeIgnoreCase(code).orElseThrow(()->ApiException.bad("Promo code not found"));
  LocalDateTime now=LocalDateTime.now();
  if(!Boolean.TRUE.equals(p.getIsActive()) || now.isBefore(p.getValidFrom()) || now.isAfter(p.getValidTo()))throw ApiException.bad("Promo code inactive or expired");
  if(subtotal.compareTo(p.getMinOrderAmount())<0)throw ApiException.bad("Order amount below minimum");
  if(p.getMaxUsageCount()!=null && p.getCurrentUsageCount()>=p.getMaxUsageCount())throw ApiException.bad("Promo usage limit reached");
  if(userId!=null && p.getMaxUsagePerUser()!=null && usages.countByPromoCodeIdAndUserId(p.getId(),userId)>=p.getMaxUsagePerUser())throw ApiException.bad("Promo already used by this customer");
  return p;
 }
 public BigDecimal eligibleSubtotal(PromoCode p,List<CartItem> items,Function<CartItem,BigDecimal> pricing){
  var categories=p.getApplicableCategories().stream().map(Category::getId).collect(java.util.stream.Collectors.toSet());
  var products=p.getApplicableProducts().stream().map(Product::getId).collect(java.util.stream.Collectors.toSet());
  BigDecimal eligible=BigDecimal.ZERO;
  for(CartItem i:items){
   if((categories.isEmpty() && products.isEmpty()) || categories.contains(i.getProduct().getCategory().getId()) || products.contains(i.getProduct().getId()))
    eligible=eligible.add(pricing.apply(i).multiply(BigDecimal.valueOf(i.getQuantity())));
  }
  if(eligible.signum()==0)throw ApiException.bad("No eligible items for promo code");
  return eligible;
 }
 public BigDecimal discount(PromoCode p,BigDecimal subtotal){
  BigDecimal result=switch(p.getDiscountType()){
   case PERCENTAGE->subtotal.multiply(p.getDiscountValue()).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP);
   case FIXED_AMOUNT->p.getDiscountValue();case FREE_SHIPPING->BigDecimal.ZERO;};
  if(p.getMaxDiscountAmount()!=null)result=result.min(p.getMaxDiscountAmount());
  return result.max(BigDecimal.ZERO).min(subtotal);
 }
 public PromoResponse validate(PromoRequest r,BigDecimal subtotal){PromoCode p=require(r.code(),subtotal,current.get().getId());BigDecimal d=discount(p,subtotal);return new PromoResponse(p.getCode(),d,subtotal.subtract(d));}
 public Page<AdminPromoResponse> list(Pageable pageable){return promos.findAll(pageable).map(mapper::promo);}
 public AdminPromoResponse create(AdminPromoRequest r){PromoCode p=new PromoCode();fill(p,r);p.setCreatedBy(current.get());return mapper.promo(promos.save(p));}
 public AdminPromoResponse update(Long id,AdminPromoRequest r){PromoCode p=promos.findById(id).orElseThrow(()->ApiException.notFound("Promo not found"));fill(p,r);return mapper.promo(p);}
 public void delete(Long id){PromoCode p=promos.findById(id).orElseThrow(()->ApiException.notFound("Promo not found"));p.setIsActive(false);}
 private void fill(PromoCode p,AdminPromoRequest r){p.setCode(r.code().toUpperCase());p.setDiscountType(r.discountType());p.setDiscountValue(r.discountValue());p.setMinOrderAmount(r.minOrderAmount()==null?BigDecimal.ZERO:r.minOrderAmount());p.setMaxDiscountAmount(r.maxDiscountAmount());p.setMaxUsageCount(r.maxUsageCount());p.setMaxUsagePerUser(r.maxUsagePerUser()==null?1:r.maxUsagePerUser());p.setValidFrom(r.validFrom()==null?LocalDateTime.now():r.validFrom());p.setValidTo(r.validTo()==null?LocalDateTime.now().plusDays(90):r.validTo());p.setIsActive(true);}
 public void registerUsage(PromoCode p,User u,Order order,BigDecimal discount){PromoCodeUsage usage=new PromoCodeUsage();usage.setPromoCode(p);usage.setUser(u);usage.setOrder(order);usage.setDiscountApplied(discount);usages.save(usage);p.setCurrentUsageCount(p.getCurrentUsageCount()+1);}
}
