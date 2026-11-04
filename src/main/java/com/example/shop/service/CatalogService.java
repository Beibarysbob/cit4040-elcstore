package com.example.shop.service;
import com.example.shop.dto.ApiDto.*;
import com.example.shop.exception.ApiException;
import com.example.shop.mapper.ApiMapper;
import com.example.shop.model.entity.*;
import com.example.shop.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.math.*;
import java.util.*;
@Service @Transactional
public class CatalogService {
 private final ProductRepository products;private final BrandRepository brands;private final CategoryRepository categories;private final ProductVariantRepository variants;private final ProductImageRepository images;private final ProductSpecificationRepository specs;private final ApiMapper mapper;
 public CatalogService(ProductRepository products,BrandRepository brands,CategoryRepository categories,ProductVariantRepository variants,ProductImageRepository images,ProductSpecificationRepository specs,ApiMapper mapper){this.products=products;this.brands=brands;this.categories=categories;this.variants=variants;this.images=images;this.specs=specs;this.mapper=mapper;}
 public Product getProduct(Long id){return products.findById(id).filter(p->Boolean.TRUE.equals(p.getIsActive())).orElseThrow(()->ApiException.notFound("No product with id "+id));}
 public Product getAnyProduct(Long id){return products.findById(id).orElseThrow(()->ApiException.notFound("No product with id "+id));}
 public Brand getBrand(Long id){return brands.findById(id).orElseThrow(()->ApiException.notFound("No brand with id "+id));}
 public Category getCategory(Long id){return categories.findById(id).orElseThrow(()->ApiException.notFound("No category with id "+id));}
 public Page<ProductResponse> search(Long categoryId,Long brandId,BigDecimal minPrice,BigDecimal maxPrice,Boolean inStock,BigDecimal rating,String search,Boolean featured,Boolean newArrival,Pageable pageable){
  Specification<Product> spec=(root,q,cb)->{
   List<Predicate> predicates=new ArrayList<>();predicates.add(cb.isTrue(root.get("isActive")));
   if(categoryId!=null)predicates.add(cb.equal(root.get("category").get("id"),categoryId));
   if(brandId!=null)predicates.add(cb.equal(root.get("brand").get("id"),brandId));
   if(minPrice!=null)predicates.add(cb.greaterThanOrEqualTo(root.get("basePrice"),minPrice));
   if(maxPrice!=null)predicates.add(cb.lessThanOrEqualTo(root.get("basePrice"),maxPrice));
   if(Boolean.TRUE.equals(inStock))predicates.add(cb.greaterThan(root.get("stockQuantity"),0));
   if(rating!=null)predicates.add(cb.greaterThanOrEqualTo(root.get("averageRating"),rating));
   if(search!=null && !search.isBlank()){String pattern="%"+search.toLowerCase()+"%";predicates.add(cb.or(cb.like(cb.lower(root.get("name")),pattern),cb.like(cb.lower(root.get("sku")),pattern)));}
   if(featured!=null)predicates.add(cb.equal(root.get("isFeatured"),featured));
   if(newArrival!=null)predicates.add(cb.equal(root.get("isNewArrival"),newArrival));
   return cb.and(predicates.toArray(new Predicate[0]));};
  return products.findAll(spec,pageable).map(mapper::product);
 }
 public ProductResponse one(Long id){return mapper.product(getProduct(id));}
 public Page<BrandResponse> brands(Pageable p){return brands.findAll(p).map(mapper::brand);}
 public BrandResponse brand(Long id){return mapper.brand(getBrand(id));}
 public Page<CategoryResponse> categories(Pageable p){return categories.findAll(p).map(mapper::category);}
 public CategoryResponse category(Long id){return mapper.category(getCategory(id));}
 public CategoryResponse categorySlug(String slug){return mapper.category(categories.findBySlug(slug).orElseThrow(()->ApiException.notFound("Category not found")));}
 public Page<ProductResponse> byBrand(Long id,Pageable page){getBrand(id);return search(null,id,null,null,null,null,null,null,null,page);}
 public Page<ProductResponse> byCategory(Long id,Pageable page){getCategory(id);return search(id,null,null,null,null,null,null,null,null,page);}
 public List<VariantResponse> variants(Long productId){getProduct(productId);return variants.findByProductId(productId).stream().map(mapper::variant).toList();}
 public List<ImageResponse> images(Long productId){getProduct(productId);return images.findByProductIdOrderBySortOrderAsc(productId).stream().map(mapper::image).toList();}
 public List<SpecificationResponse> specifications(Long productId){getProduct(productId);return specs.findByProductIdOrderBySortOrderAsc(productId).stream().map(mapper::specification).toList();}
 public ProductResponse create(ProductRequest r){
  if(products.findBySku(r.sku()).isPresent())throw ApiException.conflict("SKU already exists");
  Product p=new Product();fill(p,r);return mapper.product(products.save(p));
 }
 public ProductResponse update(Long id,ProductRequest r){Product p=getAnyProduct(id);if(products.findBySku(r.sku()).filter(x->!x.getId().equals(id)).isPresent())throw ApiException.conflict("SKU already exists");fill(p,r);return mapper.product(p);}
 private void fill(Product p,ProductRequest r){p.setName(r.name());p.setSlug(r.slug());p.setSku(r.sku());p.setDescription(r.description());p.setShortDescription(r.shortDescription());p.setBasePrice(r.basePrice());p.setDiscountPrice(r.discountPrice());p.setCategory(getCategory(r.categoryId()));p.setBrand(r.brandId()==null?null:getBrand(r.brandId()));p.setStockQuantity(r.stockQuantity()==null?0:r.stockQuantity());p.setIsFeatured(Boolean.TRUE.equals(r.featured()));p.setIsNewArrival(Boolean.TRUE.equals(r.newArrival()));}
 public void softDelete(Long id){getAnyProduct(id).setIsActive(false);}
 public ProductResponse activate(Long id,boolean active){Product p=getAnyProduct(id);p.setIsActive(active);return mapper.product(p);}
 public ProductResponse setStock(Long id,int stock){Product p=getAnyProduct(id);if(stock<p.getReservedQuantity())throw ApiException.conflict("Stock below reserved quantity");p.setStockQuantity(stock);return mapper.product(p);}
 public VariantResponse createVariant(Long pid,VariantRequest r){ProductVariant v=new ProductVariant();v.setProduct(getAnyProduct(pid));fillVariant(v,r);return mapper.variant(variants.save(v));}
 public VariantResponse updateVariant(Long pid,Long vid,VariantRequest r){ProductVariant v=getVariant(pid,vid);fillVariant(v,r);return mapper.variant(v);}
 public void deleteVariant(Long pid,Long vid){ProductVariant v=getVariant(pid,vid);v.setIsActive(false);}
 private ProductVariant getVariant(Long pid,Long vid){ProductVariant v=variants.findById(vid).orElseThrow(()->ApiException.notFound("Variant not found"));if(!v.getProduct().getId().equals(pid))throw ApiException.notFound("Variant not found");return v;}
 private void fillVariant(ProductVariant v,VariantRequest r){v.setSku(r.sku());v.setName(r.name());v.setPrice(r.price());v.setAttributes(r.attributes());v.setImageUrl(r.imageUrl());v.setStockQuantity(r.stockQuantity()==null?0:r.stockQuantity());}
 public BrandResponse createBrand(BrandRequest r){Brand b=new Brand();fillBrand(b,r);return mapper.brand(brands.save(b));}
 public BrandResponse updateBrand(Long id,BrandRequest r){Brand b=getBrand(id);fillBrand(b,r);return mapper.brand(b);}
 public void deleteBrand(Long id){getBrand(id).setIsActive(false);}
 private void fillBrand(Brand b,BrandRequest r){b.setName(r.name());b.setSlug(r.slug());b.setDescription(r.description());b.setCountry(r.country());b.setLogoUrl(r.logoUrl());b.setWebsiteUrl(r.websiteUrl());}
 public CategoryResponse createCategory(CategoryRequest r){Category c=new Category();fillCategory(c,r);return mapper.category(categories.save(c));}
 public CategoryResponse updateCategory(Long id,CategoryRequest r){Category c=getCategory(id);fillCategory(c,r);return mapper.category(c);}
 public void deleteCategory(Long id){getCategory(id).setIsActive(false);}
 public void reorder(ReorderCategoriesRequest r){for(int i=0;i<r.categoryIds().size();i++)getCategory(r.categoryIds().get(i)).setSortOrder(i);}
 private void fillCategory(Category c,CategoryRequest r){c.setName(r.name());c.setSlug(r.slug());c.setDescription(r.description());c.setSortOrder(r.sortOrder()==null?0:r.sortOrder());c.setParentCategory(r.parentId()==null?null:getCategory(r.parentId()));if(c.getId()!=null && r.parentId()!=null && c.getId().equals(r.parentId()))throw ApiException.bad("Category cannot parent itself");}
}
