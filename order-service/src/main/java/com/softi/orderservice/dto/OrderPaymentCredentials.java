package com.softi.orderservice.dto;

import lombok.Data;

@Data
public class OrderPaymentCredentials {

    private String orderId;
    private String cardHolderName;
    private String cardNumber;
    private String cvv;

}
