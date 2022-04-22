package com.bank.bootcamp.bankcustomers.businesslogic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.bank.bootcamp.bankcustomers.entity.BusinessCustomer;
import com.bank.bootcamp.bankcustomers.exception.BankValidationException;
import com.bank.bootcamp.bankcustomers.repository.BusinessCustomerRepository;
import com.bank.bootcamp.bankcustomers.service.BusinessCustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BusinessCustomerTests {

  private static BusinessCustomerService customerService;
  private static BusinessCustomerRepository customerRepository;
  private ObjectMapper mapper = new ObjectMapper();

  @BeforeAll
  public static void setup() {
    customerRepository = mock(BusinessCustomerRepository.class);
    customerService = new BusinessCustomerService(customerRepository);
  }
  
  private BusinessCustomer getCustomer() {
    var customer = new BusinessCustomer();
    customer.setRuc("10706824630");
    customer.setBusinessName("BLC Empresarial");
    return customer;
  }

  @Test
  public void createBusinessCustomerWithAllData() throws Exception {
    
    var customer = getCustomer();

    var savedCustomer = mapper.readValue(mapper.writeValueAsString(customer), BusinessCustomer.class);
    savedCustomer.setId(UUID.randomUUID().toString());

    when(customerRepository.findFirstByRuc(customer.getRuc())).thenReturn(Mono.empty());
    when(customerRepository.save(customer)).thenReturn(Mono.just(savedCustomer));

    var mono = customerService.createBusinessCustomer(customer);
    StepVerifier.create(mono).assertNext((saved) -> {
      assertThat(saved.getId()).isNotBlank();
    }).verifyComplete();
  }

  @Test
  public void createBusinessCustomerWithIncompleteData() throws Exception {
    var customer = getCustomer();
    customer.setRuc(null);

    var savedCustomer = mapper.readValue(mapper.writeValueAsString(customer), BusinessCustomer.class);
    savedCustomer.setId(UUID.randomUUID().toString());

    when(customerRepository.findFirstByRuc(customer.getRuc())).thenReturn(Mono.empty());
    when(customerRepository.save(customer)).thenReturn(Mono.just(savedCustomer));

    var mono = customerService.createBusinessCustomer(customer);
    StepVerifier.create(mono).expectError(BankValidationException.class).verify();
  }
  
  @Test
  public void personBusinessAlreadyExists() throws Exception {
    var customer = getCustomer();

    var savedCustomer = mapper.readValue(mapper.writeValueAsString(customer), BusinessCustomer.class);
    savedCustomer.setId(UUID.randomUUID().toString());

    when(customerRepository.findFirstByRuc(customer.getRuc())).thenReturn(Mono.just(savedCustomer));
    when(customerRepository.save(customer)).thenReturn(Mono.just(savedCustomer));

    var mono = customerService.createBusinessCustomer(customer);
    StepVerifier.create(mono).expectError(BankValidationException.class).verify();
  }
}
