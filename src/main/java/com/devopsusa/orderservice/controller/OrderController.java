package com.devopsusa.orderservice.controller;

import com.devopsusa.orderservice.common.TransactionRequest;
import com.devopsusa.orderservice.common.TransactionResponse;
import com.devopsusa.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders/fruitOrder")
    public ResponseEntity<TransactionResponse> fruitOrder(@RequestBody TransactionRequest payload){
        TransactionResponse transactionResponse = orderService.saveOrder(payload);
        return new ResponseEntity<TransactionResponse>(transactionResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping("/orders/fruitOrder/{id}")
    public ResponseEntity<TransactionResponse> updateFruitOrder(@RequestBody TransactionRequest payload,
                                                                @PathVariable Integer id){
        TransactionResponse transactionResponse = orderService.updateOrder(id,payload);
        return new ResponseEntity<TransactionResponse>(transactionResponse, new HttpHeaders(), HttpStatus.OK);

    }
}
