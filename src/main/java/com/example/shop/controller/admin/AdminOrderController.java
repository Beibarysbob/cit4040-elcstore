package com.example.shop.controller.admin;
import com.example.shop.dto.ApiDto.*;
import com.example.shop.service.*;
import com.example.shop.model.enums.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import java.net.URI;
import java.util.*;
import java.math.*;
@RestController @RequiredArgsConstructor @RequestMapping("/api/admin/orders")
public class AdminOrderController {
 private final OrderService orders;

 @GetMapping public Page<OrderResponse> list(@PageableDefault(size=20,sort="createdAt",direction=Sort.Direction.DESC) Pageable p){return orders.all(p);}
 @PutMapping("/{id}/status") public OrderResponse status(@PathVariable Long id,@Valid @RequestBody OrderStatusRequest r){return orders.adminStatus(id,r.status());}
 @PutMapping("/{id}/tracking") public OrderResponse tracking(@PathVariable Long id,@RequestBody TrackingRequest r){return orders.tracking(id,r);}
 @PutMapping("/{id}/note") public OrderResponse note(@PathVariable Long id,@RequestBody AdminNoteRequest r){return orders.note(id,r);}

}
