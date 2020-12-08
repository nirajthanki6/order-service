package com.devopsusa.orderservice.service;

import com.devopsusa.orderservice.common.Payment;
import com.devopsusa.orderservice.common.TransactionRequest;
import com.devopsusa.orderservice.common.TransactionResponse;
import com.devopsusa.orderservice.domain.Order;
import com.devopsusa.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class OrderService  {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestTemplate restTemplate;

    public TransactionResponse saveOrder(TransactionRequest request) {
        String message="";
        Order order = request.getOrder();
        Payment payment = request.getPayment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());
        Payment paymentResponse = restTemplate.postForObject("http://localhost:8082/payment/doPayment",payment,
                Payment.class);

        message = paymentResponse.getPaymentStatus().equals("success")? "payment processing successful and order placed"
                :"payment api failed,order added to cart";
        orderRepository.save(order);
        return new TransactionResponse(order,paymentResponse.getAmount(),paymentResponse.getTransactionId(),message);
    }


    public TransactionResponse updateOrder(Integer id, TransactionRequest payload) {

        String message="";
        Order order = payload.getOrder();
        Payment payment = payload.getPayment();
        order.setName(payload.getOrder().getName());
        order.setPrice(payload.getOrder().getPrice());
        order.setQty(payload.getOrder().getQty());
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());
        Payment paymentResponse = restTemplate.postForObject("http://localhost:8082/payment/doPayment",payment,
                Payment.class);
        orderRepository.save(order);
        message = paymentResponse.getPaymentStatus().equals("success")? "payment processing update successful and order placed"
                :"payment api failed,order added to cart";

        return new TransactionResponse(order,paymentResponse.getAmount(),paymentResponse.getTransactionId(),message);
    }
}
