package com.devopsusa.orderservice.service;

import com.devopsusa.orderservice.common.Payment;
import com.devopsusa.orderservice.common.TransactionRequest;
import com.devopsusa.orderservice.common.TransactionResponse;
import com.devopsusa.orderservice.domain.Order;
import com.devopsusa.orderservice.exception.RecordNotFoundException;
import com.devopsusa.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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
        Payment paymentResponse = restTemplate.postForObject("http://PAYMENT-SERVICE/payment/doPayment",payment,
                Payment.class);

        message = paymentResponse.getPaymentStatus().equals("success") ? "payment processing successful and order placed"
                :"payment api failed,order added to cart";
        orderRepository.save(order);
        payment.setOrderId(request.getOrder().getId());
        return new TransactionResponse(order,paymentResponse.getAmount(),paymentResponse.getTransactionId(),message);
    }


    public TransactionResponse updateOrder(Integer id, TransactionRequest payload) throws RecordNotFoundException {
        Optional<Order> order = orderRepository.findById(id);

        if(order.isPresent()) {
            String message="";
            orderRepository.save(payload.getOrder());
            Payment payment = payload.getPayment();
            Payment paymentResponse = restTemplate.postForObject("http://PAYMENT-SERVICE/api/payment/doPayment",payment,
                    Payment.class);
            message = paymentResponse.getPaymentStatus().equals("success") ? "payment processing successful and order placed"
                    :"payment api failed,order added to cart";
            payment.setPaymentStatus(paymentResponse.getPaymentStatus());
            payment.setTransactionId(paymentResponse.getTransactionId());
            payment.setAmount(payload.getOrder().getPrice());
            payment.setOrderId(payload.getOrder().getId());
            return new TransactionResponse(payload.getOrder(),payment.getAmount(),payment.getTransactionId(),message);
        } else {
            throw new RecordNotFoundException("No order record exist for given id");
        }
    }

    public List<Order> getallFruitOrder() {
        return orderRepository.findAll();
    }

    public Order getFruitById(Integer id) throws RecordNotFoundException {
        Optional<Order> order = orderRepository.findById(id);

        if(order.isPresent()) {
            return order.get();
        } else {
            throw new RecordNotFoundException("No order record exist for given id");
        }
    }
}
