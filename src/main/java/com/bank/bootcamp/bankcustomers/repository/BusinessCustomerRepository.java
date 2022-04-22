package com.bank.bootcamp.bankcustomers.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import com.bank.bootcamp.bankcustomers.entity.BusinessCustomer;
import reactor.core.publisher.Mono;

@Repository
public interface BusinessCustomerRepository extends ReactiveMongoRepository<BusinessCustomer, String> {

  public Mono<BusinessCustomer> findFirstByRuc(String ruc);
}
