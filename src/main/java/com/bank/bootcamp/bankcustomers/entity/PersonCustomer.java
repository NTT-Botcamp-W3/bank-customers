package com.bank.bootcamp.bankcustomers.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection = "PersonCustomers")
@Data
public class PersonCustomer {

  @Id
  private String id;
  private DocumentType documentType;
  private String documentNumber;
  private String name;
  private String lastName;
}
