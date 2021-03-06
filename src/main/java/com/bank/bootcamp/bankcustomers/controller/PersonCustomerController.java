package com.bank.bootcamp.bankcustomers.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bank.bootcamp.bankcustomers.entity.DocumentType;
import com.bank.bootcamp.bankcustomers.entity.PersonCustomer;
import com.bank.bootcamp.bankcustomers.service.PersonCustomerService;
import com.bank.bootcamp.bankcustomers.webclient.PersonalCustomerAccountsWebClient;
import com.bank.bootcamp.bankcustomers.webclient.dto.BalanceDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customer/person")
@RequiredArgsConstructor
public class PersonCustomerController {

  private final PersonCustomerService personCustomerService;
  private final PersonalCustomerAccountsWebClient personalCustomerAccountsWebClient;
  
  @PostMapping
  public Mono<PersonCustomer> create(@RequestBody PersonCustomer customer) {
    return personCustomerService.createPersonCustomer(customer);
  }
  
  @GetMapping("findByDocument/{documentType}/{documentNumber}")
  public Mono<PersonCustomer> findByDocument(
      @PathVariable("documentType") DocumentType documentType,
      @PathVariable("documentNumber") String documentNumber) {
    
    return personCustomerService.findByDocument(documentType, documentNumber);
  }
  
  @GetMapping("/{customerId}/allProducts")
  public Flux<BalanceDTO> getAllProducts(@PathVariable("customerId") String customerId) {
    return personalCustomerAccountsWebClient.getAllProductsInfo(customerId);
  }


}
