package com.bank.bootcamp.bankcustomers.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bank.bootcamp.bankcustomers.entity.BusinessCustomer;
import com.bank.bootcamp.bankcustomers.service.BusinessCustomerService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customer/business")
@RequiredArgsConstructor
public class BusinessCustomerController {

  private final BusinessCustomerService businessCustomerService;
  
  @PostMapping
  public Mono<BusinessCustomer> create(@RequestBody BusinessCustomer customer) {
    return businessCustomerService.createBusinessCustomer(customer);
  }
  
  @GetMapping("/findByRuc/{ruc}")
  public Mono<BusinessCustomer> findByRuc(@PathVariable("ruc") String ruc) {
    return businessCustomerService.findByRuc(ruc);
  }


}
