package com.example.shop.config;
import org.springframework.data.domain.*;
import java.util.*;
public final class PageUtil {
 private PageUtil(){}
 public static <T> Page<T> fromList(List<T> values,Pageable pageable){
  int start=(int)Math.min((long)values.size(),pageable.getOffset());
  int end=Math.min(values.size(),start+pageable.getPageSize());
  return new PageImpl<>(values.subList(start,end),pageable,values.size());
 }
}
