package com.example.shop.service;
import com.example.shop.dto.ApiDto.*;
import com.example.shop.exception.ApiException;
import com.example.shop.mapper.ApiMapper;
import com.example.shop.model.entity.*;
import com.example.shop.repository.*;
import com.example.shop.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.util.*;
import java.time.*;
import org.springframework.data.domain.Page;
@Service @Transactional
public class CartService {
 private final CartRepository carts;private final CartItemRepository items;private final CatalogService catalogue;private final ProductVariantRepository variants;private final WishlistRepository wishlists;private final CurrentUser current;private final ApiMapper mapper;private final PromoService promo;
 public CartService(CartRepository carts,CartItemRepository items,CatalogService catalogue,ProductVariantRepository variants,WishlistRepository wishlists,CurrentUser current,ApiMapper mapper,PromoService promo){this.carts=carts;this.items=items;this.catalogue=catalogue;this.variants=variants;this.wishlists=wishlists;this.current=current;this.mapper=mapper;this.promo=promo;}
 public Cart get(){User user=current.get();return carts.findByUserId(user.getId()).orElseGet(()->{Cart cart=new Cart();cart.setUser(user);return carts.save(cart);});}
 public Cart guest(String sessionId){if(sessionId==null || sessionId.isBlank())throw ApiException.bad("Guest session id required");return carts.findBySessionId(sessionId).orElseGet(()->{Cart cart=new Cart();cart.setSessionId(sessionId);return carts.save(cart);});}
 public CartResponse show(){return showCart(get());}
 public CartResponse showGuest(String sessionId){return showCart(guest(sessionId));}
 public CartResponse showCart(Cart cart){List<CartItemResponse> rows=new ArrayList<>();BigDecimal total=BigDecimal.ZERO;
  for(CartItem i:items.findByCartId(cart.getId())){
   BigDecimal now=currentPrice(i);BigDecimal line=now.multiply(BigDecimal.valueOf(i.getQuantity()));total=total.add(line);
   rows.add(new CartItemResponse(i.getId(),i.getProduct().getId(),i.getProduct().getName(),i.getVariant()==null?null:i.getVariant().getId(),i.getQuantity(),i.getPriceAtAdd(),now,i.getPriceAtAdd().compareTo(now)!=0,line));
  }
  BigDecimal disc=BigDecimal.ZERO;if(cart.getAppliedPromoCode()!=null){try{var code=promo.require(cart.getAppliedPromoCode(),total,cart.getUser()==null?null:cart.getUser().getId());disc=promo.discount(code,promo.eligibleSubtotal(code,items.findByCartId(cart.getId()),this::currentPrice));}catch(ApiException ignore){ /* expired promo is shown as invalid on checkout */ }}
  return new CartResponse(cart.getId(),rows,total,cart.getAppliedPromoCode(),disc,total.subtract(disc));
 }
 public BigDecimal currentPrice(CartItem i){return i.getVariant()!=null?i.getVariant().getPrice():mapper.price(i.getProduct());}
 public void checkStock(Product p,ProductVariant v,int quantity){if(quantity<=0)throw ApiException.bad("Quantity must be positive");
  if(!Boolean.TRUE.equals(p.getIsActive()) || p.getStockQuantity()-p.getReservedQuantity()<quantity)throw ApiException.conflict("Insufficient stock for "+p.getName());
  if(v!=null && (!Boolean.TRUE.equals(v.getIsActive()) || v.getStockQuantity()-v.getReservedQuantity()<quantity))throw ApiException.conflict("Insufficient variant stock");
 }
 public CartResponse add(Cart cart,CartItemRequest r){Product p=catalogue.getProduct(r.productId());ProductVariant v=null;
  if(r.variantId()!=null){v=variants.findById(r.variantId()).orElseThrow(()->ApiException.notFound("Variant not found"));if(!v.getProduct().getId().equals(p.getId()))throw ApiException.bad("Variant belongs to a different product");}
  final ProductVariant chosen=v;
  var existing=items.findByCartId(cart.getId()).stream().filter(i->i.getProduct().getId().equals(p.getId()) && Objects.equals(i.getVariant()==null?null:i.getVariant().getId(),chosen==null?null:chosen.getId())).findFirst();
  int quantity=r.quantity()+(existing.isPresent()?existing.get().getQuantity():0);checkStock(p,v,quantity);
  CartItem item=existing.orElseGet(CartItem::new);item.setCart(cart);item.setProduct(p);item.setVariant(v);item.setQuantity(quantity);if(existing.isEmpty())item.setPriceAtAdd(v==null?mapper.price(p):v.getPrice());items.save(item);
  return showCart(cart);
 }
 public CartResponse add(CartItemRequest r){return add(get(),r);}
 public CartResponse update(Long itemId,QuantityRequest r){Cart cart=get();CartItem item=own(cart,itemId);checkStock(item.getProduct(),item.getVariant(),r.quantity());item.setQuantity(r.quantity());return showCart(cart);}
 public CartResponse remove(Long itemId){Cart c=get();items.delete(own(c,itemId));items.flush();return showCart(c);}
 public void clear(){Cart cart=get();items.deleteAll(items.findByCartId(cart.getId()));cart.setAppliedPromoCode(null);}
 public CartResponse apply(PromoRequest request){Cart cart=get();BigDecimal subtotal=showCart(cart).subtotal();var code=promo.require(request.code(),subtotal,current.get().getId());promo.eligibleSubtotal(code,items.findByCartId(cart.getId()),this::currentPrice);cart.setAppliedPromoCode(request.code().toUpperCase());return showCart(cart);}
 public CartResponse removePromo(){Cart c=get();c.setAppliedPromoCode(null);return showCart(c);}
 public CartResponse merge(MergeRequest r){Cart dest=get();Cart src=carts.findBySessionId(r.guestSessionId()).orElseThrow(()->ApiException.notFound("Guest cart not found"));
  if(src.getUser()!=null)throw ApiException.forbidden("Not a guest cart");
  for(CartItem i:items.findByCartId(src.getId()))add(dest,new CartItemRequest(i.getProduct().getId(),i.getVariant()==null?null:i.getVariant().getId(),i.getQuantity()));
  items.deleteAll(items.findByCartId(src.getId()));carts.delete(src);return showCart(dest);
 }
 private CartItem own(Cart cart,Long id){CartItem i=items.findById(id).orElseThrow(()->ApiException.notFound("Cart item not found"));if(!i.getCart().getId().equals(cart.getId()))throw ApiException.notFound("Cart item not found");return i;}
 public Page<WishlistResponse> wishlist(org.springframework.data.domain.Pageable p){return wishlists.findByUserId(current.get().getId(),p).map(x->new WishlistResponse(x.getId(),x.getProduct().getId(),x.getProduct().getName(),x.getPriceAtAdd(),mapper.price(x.getProduct())));}
 public void addWishlist(Long id){User u=current.get();Product p=catalogue.getProduct(id);if(wishlists.findByUserIdAndProductId(u.getId(),id).isPresent())return;Wishlist w=new Wishlist();w.setUser(u);w.setProduct(p);w.setPriceAtAdd(mapper.price(p));wishlists.save(w);}
 public void removeWishlist(Long id){var x=wishlists.findByUserIdAndProductId(current.get().getId(),id).orElseThrow(()->ApiException.notFound("Wishlist item not found"));wishlists.delete(x);}
 public CartResponse moveToCart(Long id){removeWishlist(id);return add(new CartItemRequest(id,null,1));}
}
