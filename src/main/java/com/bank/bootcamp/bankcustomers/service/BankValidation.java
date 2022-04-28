package com.bank.bootcamp.bankcustomers.service;

import java.util.function.Predicate;
import com.bank.bootcamp.bankcustomers.exception.BankValidationException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BankValidation {

  public static <T> Mono<Void> check(T t, Predicate<T> predicate, String messageForException) {
    return Mono.create(sink -> {
      if (predicate.test(t)) {
        sink.error(new BankValidationException(messageForException));
        return;
      } else {
        sink.success();
      }
    });
  }
  
  public static <T> Flux<Void> checkFlux(T t, Predicate<T> predicate, String messageForException) {
    return Flux.create(sink -> {
      if (predicate.test(t)) {
        sink.error(new BankValidationException(messageForException));
        return;
      } else {
        sink.complete();
      }
    });
  }
}
