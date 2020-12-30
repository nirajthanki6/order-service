package com.devopsusa.orderservice.controller;

import com.devopsusa.orderservice.common.TransactionRequest;
import com.devopsusa.orderservice.common.TransactionResponse;
import com.devopsusa.orderservice.domain.Order;
import com.devopsusa.orderservice.exception.RecordNotFoundException;
import com.devopsusa.orderservice.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@Controller
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders/fruit")
    @ApiOperation(value = "Place fruit order by /orders/fruit",
            notes = "Place an order to cart and payment processing",
            response = TransactionResponse.class)
    @ResponseBody
    public ResponseEntity<TransactionResponse> fruitOrder(@RequestBody TransactionRequest payload){
        TransactionResponse transactionResponse = orderService.saveOrder(payload);
        return new ResponseEntity<TransactionResponse>(transactionResponse, new HttpHeaders(), HttpStatus.CREATED);
    }

    @PatchMapping("/orders/fruit/{id}")
    @ApiOperation(value = "Update fruit order by /orders/fruit/{id}",
            notes = "Update an order to cart and payment processing",
            response = TransactionResponse.class)
    @ResponseBody
    public ResponseEntity<TransactionResponse> updateFruitOrder(@RequestBody TransactionRequest payload,
                                                                @PathVariable Integer id) throws RecordNotFoundException {
        TransactionResponse transactionResponse = orderService.updateOrder(id,payload);
        return new ResponseEntity<TransactionResponse>(transactionResponse, new HttpHeaders(), HttpStatus.OK);

    }

    @GetMapping("/orders/all")
    @ApiOperation(value = "Get all fruit  order by /orders",
            notes = "Get all fruit order",
            response = Order.class)
    @ResponseBody
    public ResponseEntity<List<Order>> getallFruitOrder() {
        List<Order> orders = orderService.getallFruitOrder();
        return new ResponseEntity<List<Order>>(orders, new HttpHeaders(),HttpStatus.OK);
    }

    @GetMapping("/orders/fruit/{id}")
    @ApiOperation(value = "Get fruit by order id by /orders/{id}",
            notes = "Get fruit order by id",
            response = Order.class)
    @ResponseBody
    public ResponseEntity<Order> getFruitById(@PathVariable("id") Integer id) throws RecordNotFoundException {
        Order order = orderService.getFruitById(id);
        return new ResponseEntity<Order>(order,new HttpHeaders(),HttpStatus.OK);
    }
}
