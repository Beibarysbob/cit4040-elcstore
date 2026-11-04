package com.example.shop.mapper;
import com.example.shop.dto.ApiDto.*;
import com.example.shop.model.entity.*;
import java.math.*;
import java.util.*;
import org.springframework.stereotype.Component;
@Component
public class ApiMapper {
 public static BigDecimal nz(BigDecimal x){return x==null?BigDecimal.ZERO:x;}
 public UserResponse user(User x){return new UserResponse(x.getId(),x.getEmail(),x.getFirstName(),x.getLastName(),x.getPhone(),x.getRole());}
 public AddressResponse address(Address x){return new AddressResponse(x.getId(),x.getTitle(),x.getCountry(),x.getCity(),x.getStreet(),x.getBuilding(),x.getApartment(),x.getPostalCode(),x.getRecipientName(),x.getRecipientPhone(),Boolean.TRUE.equals(x.getIsDefault()));}
 public BrandResponse brand(Brand x){return new BrandResponse(x.getId(),x.getName(),x.getSlug(),x.getDescription(),x.getCountry(),Boolean.TRUE.equals(x.getIsActive()));}
 public CategoryResponse category(Category x){return new CategoryResponse(x.getId(),x.getName(),x.getSlug(),x.getDescription(),x.getParentCategory()==null?null:x.getParentCategory().getId(),Boolean.TRUE.equals(x.getIsActive()));}
 public BigDecimal price(Product x){return x.getDiscountPrice()!=null && x.getDiscountPrice().signum()>=0 && (x.getDiscountStartDate()==null || !x.getDiscountStartDate().isAfter(java.time.LocalDateTime.now())) && (x.getDiscountEndDate()==null || !x.getDiscountEndDate().isBefore(java.time.LocalDateTime.now())) ? x.getDiscountPrice():x.getBasePrice();}
 public ProductResponse product(Product x){return new ProductResponse(x.getId(),x.getName(),x.getSlug(),x.getSku(),x.getDescription(),x.getShortDescription(),x.getBasePrice(),price(x),x.getStockQuantity(),x.getStockQuantity()-x.getReservedQuantity(),Boolean.TRUE.equals(x.getIsActive()),Boolean.TRUE.equals(x.getIsFeatured()),Boolean.TRUE.equals(x.getIsNewArrival()),nz(x.getAverageRating()),x.getReviewCount(),x.getCategory().getId(),x.getBrand()==null?null:x.getBrand().getId());}
 public VariantResponse variant(ProductVariant x){return new VariantResponse(x.getId(),x.getProduct().getId(),x.getSku(),x.getName(),x.getPrice(),x.getStockQuantity()-x.getReservedQuantity(),x.getAttributes(),x.getImageUrl(),Boolean.TRUE.equals(x.getIsActive()));}
 public ImageResponse image(ProductImage x){return new ImageResponse(x.getId(),x.getImageUrl(),x.getThumbnailUrl(),x.getAltText(),Boolean.TRUE.equals(x.getIsPrimary()),x.getSortOrder());}
 public SpecificationResponse specification(ProductSpecification x){return new SpecificationResponse(x.getId(),x.getName(),x.getValue(),x.getGroupName(),x.getSortOrder());}
 public ReviewResponse review(Review x){return new ReviewResponse(x.getId(),x.getProduct().getId(),x.getUser().getId(),x.getRating(),x.getTitle(),x.getComment(),Boolean.TRUE.equals(x.getIsVerifiedPurchase()),x.getStatus(),x.getCreatedAt());}
 public DeliveryResponse delivery(DeliveryOption x){return new DeliveryResponse(x.getId(),x.getName(),x.getCode(),x.getBasePrice(),x.getFreeThreshold(),x.getEstimatedDaysMin(),x.getEstimatedDaysMax());}
 public AdminPromoResponse promo(PromoCode p){return new AdminPromoResponse(p.getId(),p.getCode(),p.getDiscountType(),p.getDiscountValue(),Boolean.TRUE.equals(p.getIsActive()));}
 public PaymentResponse payment(Payment p){return new PaymentResponse(p.getOrder().getId(),p.getStatus(),p.getTransactionId(),p.getAmount(),p.getCardLastFourDigits(),p.getErrorCode());}
 public OrderResponse order(Order o){return new OrderResponse(o.getId(),o.getOrderNumber(),o.getStatus(),o.getItems().stream().map(i->new OrderItemResponse(i.getProduct().getId(),i.getProduct().getName(),i.getQuantity(),i.getPriceAtPurchase(),i.getPriceAtPurchase().multiply(BigDecimal.valueOf(i.getQuantity())))).toList(),o.getSubtotal(),o.getDiscount(),o.getShippingCost(),o.getTotalAmount(),o.getDeliveryMethod(),o.getDeliveryTrackingNumber(),o.getEstimatedDeliveryDate(),o.getCreatedAt());}
}
