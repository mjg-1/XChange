package com.xeiam.xchange.itbit.v1.dto.account;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItBitWithdrawalRequest {

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("amount")
  protected BigDecimal amount;

  @JsonProperty("address")
  protected String address;

  public ItBitWithdrawalRequest(String currency, BigDecimal amount, String address) {

    super();
    this.currency = currency;
    this.amount = amount;
    this.address = address;
  }

  public String getCurrency() {

    return currency;
  }

  public BigDecimal getAmount() {

    return amount;
  }

  public String getAddress() {

    return address;
  }
}
