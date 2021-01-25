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
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeorder")
    @ApiOperation(value = "Place order by /placeorder",
            notes = "Place an order to cart and payment processing",
            response = TransactionResponse.class)
    @ResponseBody
    public ResponseEntity<TransactionResponse> Order(@RequestBody TransactionRequest payload){
        TransactionResponse transactionResponse = orderService.saveOrder(payload);
        return new ResponseEntity<TransactionResponse>(transactionResponse, new HttpHeaders(), HttpStatus.CREATED);
    }

    @PatchMapping("/updateorder/{id}")
    @ApiOperation(value = "Update order by /updateorder/{id}",
            notes = "Update an order to cart and payment processing",
            response = TransactionResponse.class)
    @ResponseBody
    public ResponseEntity<TransactionResponse> updateOrder(@RequestBody TransactionRequest payload,
                                                                @PathVariable Integer id) throws RecordNotFoundException {
        TransactionResponse transactionResponse = orderService.updateOrder(id,payload);
        return new ResponseEntity<TransactionResponse>(transactionResponse, new HttpHeaders(), HttpStatus.OK);

    }

    @GetMapping("/all")
    @ApiOperation(value = "Get all order by /all",
            notes = "Get all order",
            response = Order.class)
    @ResponseBody
    public ResponseEntity<List<Order>> getallOrder() {
        List<Order> orders = orderService.getallOrder();
        return new ResponseEntity<List<Order>>(orders, new HttpHeaders(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get order by id /{id}",
            notes = "Get order by id",
            response = Order.class)
    @ResponseBody
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Integer id) throws RecordNotFoundException {
        Order order = orderService.getOrderById(id);
        return new ResponseEntity<Order>(order,new HttpHeaders(),HttpStatus.OK);
    }
}
