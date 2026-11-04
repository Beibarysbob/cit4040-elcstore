package com.example.shop.service;
import com.example.shop.dto.ApiDto.*;
import com.example.shop.mapper.ApiMapper;
import com.example.shop.exception.ApiException;
import com.example.shop.model.entity.*;
import com.example.shop.repository.*;
import com.example.shop.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service @Transactional
public class UserService {
 private final CurrentUser current;private final AddressRepository addresses;private final UserRepository users;private final ApiMapper mapper;
 public UserService(CurrentUser current,AddressRepository addresses,UserRepository users,ApiMapper mapper){this.current=current;this.addresses=addresses;this.users=users;this.mapper=mapper;}
 public UserResponse me(){return mapper.user(current.get());}
 public UserResponse update(UpdateUserRequest r){User u=current.get();u.setFirstName(r.firstName());u.setLastName(r.lastName());u.setPhone(r.phone());return mapper.user(u);}
 public void disable(){User u=current.get();u.setEnabled(false);}
 public List<AddressResponse> addresses(){return addresses.findByUserId(current.get().getId()).stream().map(mapper::address).toList();}
 public AddressResponse create(AddressRequest r){Address a=new Address();a.setUser(current.get());fill(a,r);return mapper.address(addresses.save(a));}
 public AddressResponse update(Long id,AddressRequest r){Address a=own(id);fill(a,r);return mapper.address(a);}
 public void delete(Long id){addresses.delete(own(id));}
 public AddressResponse setDefault(Long id){Address a=own(id);for(Address x:addresses.findByUserId(a.getUser().getId()))x.setIsDefault(x.getId().equals(id));return mapper.address(a);}
 public Address own(Long id){return addresses.findByIdAndUserId(id,current.get().getId()).orElseThrow(()->ApiException.notFound("Address not found"));}
 private void fill(Address a,AddressRequest r){a.setTitle(r.title());a.setCountry(r.country());a.setCity(r.city());a.setStreet(r.street());a.setBuilding(r.building());a.setApartment(r.apartment());a.setPostalCode(r.postalCode());a.setRecipientName(r.recipientName());a.setRecipientPhone(r.recipientPhone());a.setIsDefault(r.isDefault());}
}
