package com.bank.bootcamp.bankcustomers.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import com.bank.bootcamp.bankcustomers.entity.DocumentType;
import com.bank.bootcamp.bankcustomers.entity.PersonCustomer;
import reactor.core.publisher.Mono;

@Repository
public interface PersonCustomerRepository extends ReactiveMongoRepository<PersonCustomer, String> {

  public Mono<PersonCustomer> findFirstByDocumentTypeAndDocumentNumber(DocumentType documentType, String documentNumber);
}
