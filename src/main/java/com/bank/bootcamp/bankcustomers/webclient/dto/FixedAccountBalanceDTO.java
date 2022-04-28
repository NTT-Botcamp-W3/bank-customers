package com.bank.bootcamp.bankcustomers.webclient.dto;

import lombok.Data;

@Data
public class FixedAccountBalanceDTO {
  private String accountId;
  private String type;
  private Integer accountNumber;
  private Double amount;
  private Integer monthlyMovementLimit;
  private Long monthlyMovementsAvailable;
}
