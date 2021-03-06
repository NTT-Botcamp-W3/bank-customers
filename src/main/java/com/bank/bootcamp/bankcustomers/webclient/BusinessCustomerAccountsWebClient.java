package com.bank.bootcamp.bankcustomers.webclient;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import com.bank.bootcamp.bankcustomers.exception.BankValidationException;
import com.bank.bootcamp.bankcustomers.webclient.dto.BalanceDTO;
import reactor.core.publisher.Flux;

@Service
public class BusinessCustomerAccountsWebClient {

  private final ReactiveCircuitBreaker reactiveCircuitBreaker;
  private WebClient webClient;
  
  
  public BusinessCustomerAccountsWebClient(ReactiveResilience4JCircuitBreakerFactory reactiveCircuitBreakerFactory, Environment env) {
    this.reactiveCircuitBreaker = reactiveCircuitBreakerFactory.create("products");
    webClient = WebClient.create(env.getProperty("gateway.url"));
  }

  public Flux<BalanceDTO> getAllProductsInfo(String customerId) {
    if (ObjectUtils.isEmpty(customerId)) {
      return Flux.error(new BankValidationException("Customer ID is required"));
    } else {
      
      var currentAccounts = webClient.get()
          .uri("/currentAccounts/balance/byCustomer/{customerType}/{customerId}", "BUSINESS", customerId)
          .retrieve()
          .bodyToFlux(BalanceDTO.class)
          .transform(balance -> reactiveCircuitBreaker.run(balance, throwable -> Flux.empty()));
      
      var credits = webClient.get()
          .uri("/credits/balanceByCustomer/{customerId}/{creditType}", customerId, "BUSINESS")
          .retrieve()
          .bodyToFlux(BalanceDTO.class)
          .transform(balance -> reactiveCircuitBreaker.run(balance, throwable -> Flux.empty()));
      
      return Flux.merge(currentAccounts, credits)
      .parallel()
      .sequential();
    }
  }
}
