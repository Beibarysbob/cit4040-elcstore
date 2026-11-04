package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import com.example.shop.model.enums.*;
@Entity @Table(name="categories") @Getter @Setter @NoArgsConstructor
public class Category {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String name;
  @Column(nullable=false, unique=true) private String slug;
  @Column(columnDefinition="text") private String description;
  private String imageUrl;
  @Column(nullable=false) private Integer sortOrder = 0;
  @Column(nullable=false) private Boolean isActive = true;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="parent_category_id", nullable=true) private Category parentCategory;
  @OneToMany(mappedBy="parentCategory") private List<Category> children = new ArrayList<>();
}
