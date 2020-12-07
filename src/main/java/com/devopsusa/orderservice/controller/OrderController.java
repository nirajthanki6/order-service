package com.devopsusa.orderservice.controller;

import com.devopsusa.orderservice.common.Payment;
import com.devopsusa.orderservice.common.TransactionRequest;
import com.devopsusa.orderservice.common.TransactionResponse;
import com.devopsusa.orderservice.domain.Order;
import com.devopsusa.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/fruitOrder")
    public ResponseEntity<TransactionResponse> fruitOrder(@RequestBody TransactionRequest payload){
        TransactionResponse transactionResponse = orderService.saveOrder(payload);
        return new ResponseEntity<TransactionResponse>(transactionResponse, new HttpHeaders(), HttpStatus.OK);
    }
}
