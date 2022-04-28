package com.bank.bootcamp.bankcustomers.webclient.dto;

import lombok.Data;

@Data
public class CreditBalanceDTO {

  private String creditId;
  private String cardNumber;
  private String type;
  private Double creditLimit;
  private Double used;
  private Double available;
}
