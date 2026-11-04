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
@Service @Transactional
public class ReviewService {
 private final ReviewRepository reviews;private final ProductRepository products;private final OrderRepository orders;private final CurrentUser current;private final ApiMapper mapper;private final CatalogService catalogue;
 public ReviewService(ReviewRepository reviews,ProductRepository products,OrderRepository orders,CurrentUser current,ApiMapper mapper,CatalogService catalogue){this.reviews=reviews;this.products=products;this.orders=orders;this.current=current;this.mapper=mapper;this.catalogue=catalogue;}
 public Page<ReviewResponse> list(Long id,Pageable p){catalogue.getProduct(id);return reviews.findByProductIdAndStatus(id,ReviewStatus.APPROVED,p).map(mapper::review);}
 public ReviewSummary summary(Long id){Product p=catalogue.getProduct(id);return new ReviewSummary(p.getReviewCount(),p.getAverageRating());}
 public ReviewResponse create(Long pid,ReviewRequest r){Product p=catalogue.getProduct(pid);User u=current.get();Review x=new Review();x.setProduct(p);x.setUser(u);fill(x,r);x.setIsVerifiedPurchase(orders.existsByUserIdAndStatusAndItemsProductId(u.getId(),OrderStatus.DELIVERED,pid));x.setStatus(ReviewStatus.APPROVED);x=reviews.save(x);updateAverage(p);return mapper.review(x);}
 public ReviewResponse update(Long pid,Long id,ReviewRequest r){Review x=own(pid,id);fill(x,r);updateAverage(x.getProduct());return mapper.review(x);}
 public void delete(Long pid,Long id){Review x=own(pid,id);Product p=x.getProduct();reviews.delete(x);reviews.flush();updateAverage(p);}
 private Review own(Long pid,Long id){Review x=reviews.findByIdAndUserId(id,current.get().getId()).orElseThrow(()->ApiException.notFound("Review not found"));if(!x.getProduct().getId().equals(pid))throw ApiException.notFound("Review not found");return x;}
 private void fill(Review x,ReviewRequest r){x.setRating(r.rating());x.setTitle(r.title());x.setComment(r.comment());x.setPros(r.pros());x.setCons(r.cons());}
 private void updateAverage(Product p){var approved=reviews.findByProductIdAndStatus(p.getId(),ReviewStatus.APPROVED);p.setReviewCount(approved.size());p.setAverageRating(approved.isEmpty()?BigDecimal.ZERO:BigDecimal.valueOf(approved.stream().mapToInt(Review::getRating).average().orElse(0)).setScale(2,RoundingMode.HALF_UP));}
}
