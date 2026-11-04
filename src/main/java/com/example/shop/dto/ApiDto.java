package com.example.shop.dto;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import jakarta.validation.constraints.*;
import com.example.shop.validator.*;
import com.example.shop.model.enums.*;
public final class ApiDto {
 private ApiDto(){}
 public record RegisterRequest(@NotBlank @Email @UniqueEmail String email,@NotBlank @ValidPassword String password,@NotBlank String firstName,@NotBlank String lastName,String phone){}
 public record LoginRequest(@NotBlank String email,@NotBlank String password){}
 public record RefreshRequest(@NotBlank String refreshToken){}
 public record ChangePasswordRequest(@NotBlank String oldPassword,@NotBlank @ValidPassword String newPassword){}
 public record AuthResponse(String accessToken,String refreshToken,String tokenType,long expiresInSeconds){}
 public record UserResponse(Long id,String email,String firstName,String lastName,String phone,UserRole role){}
 public record UpdateUserRequest(@NotBlank String firstName,@NotBlank String lastName,String phone){}
 public record AddressRequest(@NotBlank String title,@NotBlank String country,@NotBlank String city,@NotBlank String street,@NotBlank String building,String apartment,String postalCode,@NotBlank String recipientName,@NotBlank String recipientPhone,boolean isDefault){}
 public record AddressResponse(Long id,String title,String country,String city,String street,String building,String apartment,String postalCode,String recipientName,String recipientPhone,boolean isDefault){}
 public record BrandRequest(@NotBlank String name,@NotBlank String slug,String description,String country,String logoUrl,String websiteUrl){}
 public record BrandResponse(Long id,String name,String slug,String description,String country,boolean active){}
 public record CategoryRequest(@NotBlank String name,@NotBlank String slug,String description,Long parentId,Integer sortOrder){}
 public record CategoryResponse(Long id,String name,String slug,String description,Long parentId,boolean active){}
 public record ReorderCategoriesRequest(@NotEmpty List<Long> categoryIds){}
 public record ProductRequest(@NotBlank String name,@NotBlank String slug,@NotBlank String sku,String description,String shortDescription,@NotNull @DecimalMin("0.01") BigDecimal basePrice,BigDecimal discountPrice,@NotNull Long categoryId,Long brandId,@Min(0) Integer stockQuantity,Boolean featured,Boolean newArrival){}
 public record StockRequest(@NotNull @Min(0) Integer stockQuantity){}
 public record ProductResponse(Long id,String name,String slug,String sku,String description,String shortDescription,BigDecimal basePrice,BigDecimal effectivePrice,Integer stockQuantity,Integer availableStock,boolean active,boolean featured,boolean newArrival,BigDecimal averageRating,Integer reviewCount,Long categoryId,Long brandId){}
 public record VariantRequest(@NotBlank String sku,@NotBlank String name,@NotNull @DecimalMin("0.01") BigDecimal price,@Min(0) Integer stockQuantity,String attributes,String imageUrl){}
 public record VariantResponse(Long id,Long productId,String sku,String name,BigDecimal price,int availableStock,String attributes,String imageUrl,boolean active){}
 public record ImageResponse(Long id,String imageUrl,String thumbnailUrl,String altText,boolean primary,Integer sortOrder){}
 public record SpecificationResponse(Long id,String name,String value,String groupName,Integer sortOrder){}
 public record ReviewRequest(@Min(1) @Max(5) int rating,@NotBlank String title,String comment,String pros,String cons){}
 public record ReviewResponse(Long id,Long productId,Long userId,int rating,String title,String comment,boolean verifiedPurchase,ReviewStatus status,LocalDateTime createdAt){}
 public record ReviewSummary(long count,BigDecimal averageRating){}
 public record CartItemRequest(@NotNull Long productId,Long variantId,@NotNull @Min(1) Integer quantity){}
 public record QuantityRequest(@NotNull @Min(1) Integer quantity){}
 public record CartItemResponse(Long id,Long productId,String name,Long variantId,int quantity,BigDecimal priceAtAdd,BigDecimal currentPrice,boolean priceChanged,BigDecimal lineTotal){}
 public record CartResponse(Long id,List<CartItemResponse> items,BigDecimal subtotal,String promoCode,BigDecimal discount,BigDecimal total){}
 public record PromoRequest(@NotBlank String code){}
 public record PromoResponse(String code,BigDecimal discount,BigDecimal totalAfterDiscount){}
 public record MergeRequest(@NotBlank String guestSessionId){}
 public record WishlistResponse(Long id,Long productId,String productName,BigDecimal priceAtAdd,BigDecimal currentPrice){}
 public record OrderRequest(@NotBlank String deliveryCode,@NotNull Long addressId,String customerNote,@Min(0) Integer requestedBonusPoints){}
 public record OrderItemResponse(Long productId,String name,int quantity,BigDecimal priceAtPurchase,BigDecimal lineTotal){}
 public record OrderResponse(Long id,String orderNumber,OrderStatus status,List<OrderItemResponse> items,BigDecimal subtotal,BigDecimal discount,BigDecimal shippingCost,BigDecimal totalAmount,DeliveryMethod deliveryMethod,String trackingNumber,LocalDate estimatedDeliveryDate,LocalDateTime createdAt){}
 public record OrderStatusRequest(@NotNull OrderStatus status){}
 public record TrackingRequest(String trackingNumber){}
 public record AdminNoteRequest(String note){}
 public record PaymentRequest(@NotNull Long orderId,@NotNull PaymentMethod paymentMethod,String cardNumber){}
 public record PaymentResponse(Long orderId,PaymentStatus status,String transactionId,BigDecimal amount,String cardLastFourDigits,String errorCode){}
 public record DeliveryResponse(Long id,String name,String code,BigDecimal basePrice,BigDecimal freeThreshold,int daysMin,int daysMax){}
 public record DeliveryCalculateRequest(@NotBlank String code,@NotNull @DecimalMin("0.00") BigDecimal subtotal){}
 public record DeliveryCalculateResponse(BigDecimal cost,int minDays,int maxDays,LocalDate estimatedFrom,LocalDate estimatedTo){}
 public record AdminPromoRequest(@NotBlank String code,@NotNull DiscountType discountType,@NotNull @DecimalMin("0.00") BigDecimal discountValue,BigDecimal minOrderAmount,BigDecimal maxDiscountAmount,Integer maxUsageCount,Integer maxUsagePerUser,LocalDateTime validFrom,LocalDateTime validTo){}
 public record AdminPromoResponse(Long id,String code,DiscountType type,BigDecimal value,boolean active){}
}
