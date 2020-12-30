package com.devopsusa.orderservice.controllerTest;

import com.devopsusa.orderservice.common.Payment;
import com.devopsusa.orderservice.common.TransactionRequest;
import com.devopsusa.orderservice.common.TransactionResponse;
import com.devopsusa.orderservice.domain.Order;
import com.devopsusa.orderservice.repository.OrderRepository;
import com.devopsusa.orderservice.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;


    @Test
    void placeOrderTest() {

        Order order = new Order(105,"Orange",5,3.99);
        Payment payment = new Payment();
        TransactionResponse transactionResponse = new TransactionResponse();
        TransactionRequest transactionRequest = new TransactionRequest(order,payment);

        //doReturn(order).when(orderRepository).save(order);

        orderRepository.save(order);
        TransactionResponse returnTransactionResponse = orderService.saveOrder(transactionRequest);

        Assertions.assertEquals("payment processing successful and order placed",
                returnTransactionResponse.getMessage());
    }

//    @Test
//    void getAllOrderTest() {
//        List<Order> orders = orderRepository.findAll();
//        System.out.println("Orders = " + orders)
//
//        Assertions.assertEquals();
//
//    }



}
