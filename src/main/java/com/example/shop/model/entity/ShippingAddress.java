package com.example.shop.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
@Embeddable @Getter @Setter @NoArgsConstructor
public class ShippingAddress {
 @Column(name="shipping_recipient_name") private String recipientName;
 @Column(name="shipping_recipient_phone") private String recipientPhone;
 @Column(name="shipping_country") private String country;
 @Column(name="shipping_city") private String city;
 @Column(name="shipping_street") private String street;
 @Column(name="shipping_building") private String building;
 @Column(name="shipping_apartment") private String apartment;
 @Column(name="shipping_postal_code") private String postalCode;
}
