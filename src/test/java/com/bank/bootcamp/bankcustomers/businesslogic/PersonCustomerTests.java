package com.bank.bootcamp.bankcustomers.businesslogic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.bank.bootcamp.bankcustomers.entity.DocumentType;
import com.bank.bootcamp.bankcustomers.entity.PersonCustomer;
import com.bank.bootcamp.bankcustomers.exception.BankValidationException;
import com.bank.bootcamp.bankcustomers.repository.PersonCustomerRepository;
import com.bank.bootcamp.bankcustomers.service.PersonCustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class PersonCustomerTests {

  private static PersonCustomerService customerService;
  private static PersonCustomerRepository personCustomerRepository;
  private ObjectMapper mapper = new ObjectMapper();

  @BeforeAll
  public static void setup() {
    personCustomerRepository = mock(PersonCustomerRepository.class);
    customerService = new PersonCustomerService(personCustomerRepository);
  }
  
  private PersonCustomer getCustomer() {
    var customer = new PersonCustomer();
    customer.setDocumentType(DocumentType.DNI);
    customer.setDocumentNumber("70682463");
    customer.setName("Brayan");
    customer.setLastName("La Cruz");
    return customer;
  }

  @Test
  public void createPersonalCustomerWithAllData() throws Exception {
    
    var customer = getCustomer();

    var savedCustomer = mapper.readValue(mapper.writeValueAsString(customer), PersonCustomer.class);
    savedCustomer.setId(UUID.randomUUID().toString());

    when(personCustomerRepository.findFirstByDocumentTypeAndDocumentNumber(
        customer.getDocumentType(), customer.getDocumentNumber())).thenReturn(Mono.empty());
    when(personCustomerRepository.save(customer)).thenReturn(Mono.just(savedCustomer));

    var mono = customerService.createPersonCustomer(customer);
    StepVerifier.create(mono).assertNext((saved) -> {
      assertThat(saved.getId()).isNotBlank();
    }).verifyComplete();
  }

  @Test
  public void createPersonalCustomerWithIncompleteData() throws Exception {
    var customer = getCustomer();
    customer.setName(null);

    var savedCustomer = mapper.readValue(mapper.writeValueAsString(customer), PersonCustomer.class);
    savedCustomer.setId(UUID.randomUUID().toString());

    when(personCustomerRepository.findFirstByDocumentTypeAndDocumentNumber(
        customer.getDocumentType(), customer.getDocumentNumber())).thenReturn(Mono.empty());
    when(personCustomerRepository.save(customer)).thenReturn(Mono.just(savedCustomer));

    var mono = customerService.createPersonCustomer(customer);
    StepVerifier.create(mono).expectError(BankValidationException.class).verify();
  }
  
  @Test
  public void personCustomerAlreadyExists() throws Exception {
    var customer = getCustomer();

    var savedCustomer = mapper.readValue(mapper.writeValueAsString(customer), PersonCustomer.class);
    savedCustomer.setId(UUID.randomUUID().toString());

    when(personCustomerRepository.findFirstByDocumentTypeAndDocumentNumber(
        customer.getDocumentType(), customer.getDocumentNumber())).thenReturn(Mono.just(savedCustomer));
    when(personCustomerRepository.save(customer)).thenReturn(Mono.just(savedCustomer));

    var mono = customerService.createPersonCustomer(customer);
    StepVerifier.create(mono).expectError(BankValidationException.class).verify();
  }
}
