package com.bank.bootcamp.bankcustomers.service;

import java.util.Optional;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.bank.bootcamp.bankcustomers.entity.DocumentType;
import com.bank.bootcamp.bankcustomers.entity.PersonCustomer;
import com.bank.bootcamp.bankcustomers.exception.BankValidationException;
import com.bank.bootcamp.bankcustomers.repository.PersonCustomerRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PersonCustomerService {

  private final PersonCustomerRepository personCustomerRepository;

  public Mono<PersonCustomer> createPersonCustomer(PersonCustomer customer) {
    return Mono.just(customer)
      .then(check(customer, cust -> Optional.of(cust).isEmpty(), "Customer has not data"))
      .then(check(customer, cust -> ObjectUtils.isEmpty(cust.getDocumentType()), "Document type is required"))
      .then(check(customer, cust -> ObjectUtils.isEmpty(cust.getDocumentNumber()), "Document number is required"))
      .then(check(customer, cust -> ObjectUtils.isEmpty(cust.getName()), "Name is required"))
      .then(check(customer, cust -> ObjectUtils.isEmpty(cust.getLastName()), "Last name is required"))
        .then(personCustomerRepository.findFirstByDocumentTypeAndDocumentNumber(customer.getDocumentType(), customer.getDocumentNumber())
            .<PersonCustomer>handle((record, sink) -> sink.error(new BankValidationException("Customer already exists")))
            .switchIfEmpty(personCustomerRepository.save(customer)));
  }
  
  public Mono<PersonCustomer> findByDocument(DocumentType documentType, String documentNumber) {
    return personCustomerRepository.findFirstByDocumentTypeAndDocumentNumber(documentType, documentNumber);
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
