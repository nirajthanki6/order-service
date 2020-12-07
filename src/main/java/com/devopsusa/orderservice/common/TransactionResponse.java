package com.devopsusa.orderservice.common;

import com.devopsusa.orderservice.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionResponse {
    private Order order;
    private double amount;
    private String transactionId;
    private String message;

}
