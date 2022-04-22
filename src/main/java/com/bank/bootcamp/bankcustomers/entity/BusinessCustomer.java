package com.bank.bootcamp.bankcustomers.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection = "BusinessCustomers")
@Data
public class BusinessCustomer {

  @Id
  private String id;
  private String ruc;
  private String businessName;
}
