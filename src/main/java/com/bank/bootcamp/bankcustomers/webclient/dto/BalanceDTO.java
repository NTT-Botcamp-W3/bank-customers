package com.bank.bootcamp.bankcustomers.webclient.dto;

import lombok.Data;

@Data
public class BalanceDTO {
  private String creditId;
  private String cardNumber;
  private String type;
  private Double creditLimit;
  private Double used;
  private Double available;
  private String accountId;
  private Integer accountNumber;
  private Double amount;
  private Double maintenanceFee;
  private Integer monthlyMovementLimit;
  private Long monthlyMovementsAvailable;
}
