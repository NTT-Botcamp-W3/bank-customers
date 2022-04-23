package com.bank.bootcamp.bankcustomers.service;

import java.util.Optional;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.bank.bootcamp.bankcustomers.entity.BusinessCustomer;
import com.bank.bootcamp.bankcustomers.exception.BankValidationException;
import com.bank.bootcamp.bankcustomers.repository.BusinessCustomerRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BusinessCustomerService {

  private final BusinessCustomerRepository businessCustomerRepository;

  public Mono<BusinessCustomer> createBusinessCustomer(BusinessCustomer customer) {
    return Mono.just(customer)
      .then(check(customer, cust -> Optional.of(cust).isEmpty(), "Customer has not data"))
      .then(check(customer, cust -> ObjectUtils.isEmpty(cust.getRuc()), "RUC is required"))
      .then(check(customer, cust -> ObjectUtils.isEmpty(cust.getBusinessName()), "Business name is required"))
        .then(businessCustomerRepository.findFirstByRuc(customer.getRuc())
            .<BusinessCustomer>handle((record, sink) -> sink.error(new BankValidationException("Customer already exists")))
            .switchIfEmpty(businessCustomerRepository.save(customer)));
  }
  
  public Mono<BusinessCustomer> findByRuc(String ruc) {
    return businessCustomerRepository.findFirstByRuc(ruc);
  }

  private <T> Mono<Void> check(T customer, Predicate<T> predicate, String messageForException) {
    return Mono.create(sink -> {
      if (predicate.test(customer)) {
        sink.error(new BankValidationException(messageForException));
        return;
      } else {
        sink.success();
      }
    });
  }

}
