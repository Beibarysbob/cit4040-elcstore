package com.example.shop.service;
import com.example.shop.dto.ApiDto.*;
import com.example.shop.exception.ApiException;
import com.example.shop.mapper.ApiMapper;
import com.example.shop.model.entity.DeliveryOption;
import com.example.shop.repository.DeliveryOptionRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
@Service
public class DeliveryService {
 private final DeliveryOptionRepository options;private final ApiMapper mapper;
 public DeliveryService(DeliveryOptionRepository options,ApiMapper mapper){this.options=options;this.mapper=mapper;}
 public List<DeliveryResponse> methods(){return options.findByIsActiveTrue().stream().map(mapper::delivery).toList();}
 public DeliveryOption find(String code){return options.findByCode(code).filter(d->Boolean.TRUE.equals(d.getIsActive())).orElseThrow(()->ApiException.notFound("Delivery method not found"));}
 public BigDecimal cost(DeliveryOption option,BigDecimal subtotal){return subtotal.compareTo(option.getFreeThreshold())>=0?BigDecimal.ZERO:option.getBasePrice();}
 public DeliveryCalculateResponse calculate(DeliveryCalculateRequest r){var d=find(r.code());return new DeliveryCalculateResponse(cost(d,r.subtotal()),d.getEstimatedDaysMin(),d.getEstimatedDaysMax(),LocalDate.now().plusDays(d.getEstimatedDaysMin()),LocalDate.now().plusDays(d.getEstimatedDaysMax()));}
}
