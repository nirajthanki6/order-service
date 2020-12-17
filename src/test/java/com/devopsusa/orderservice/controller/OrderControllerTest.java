package com.devopsusa.orderservice.controller;

import com.devopsusa.orderservice.common.Payment;
import com.devopsusa.orderservice.common.TransactionRequest;
import com.devopsusa.orderservice.common.TransactionResponse;
import com.devopsusa.orderservice.domain.Order;
import com.devopsusa.orderservice.repository.OrderRepository;
import com.devopsusa.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootTest(classes = {RestTemplate.class,OrderService.class})
public class OrderControllerTest {

    @MockBean
    private OrderRepository orderRepository;

    private RestTemplate template = new RestTemplate();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldPlaceFruitOrderTest() throws URISyntaxException {
        final String baseURL = "http://localhost:8083/api/orders/fruit";
        URI uri = new URI(baseURL);
        Order order = new Order(104,"Banana",10,4.99);
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type","application/json");
        TransactionRequest transactionRequest = new TransactionRequest(order,payment);
        HttpEntity<TransactionRequest> httpEntity = new HttpEntity<TransactionRequest>(transactionRequest,httpHeaders);
        ResponseEntity<TransactionResponse> responseResponseEntity = this.template.postForEntity(uri,httpEntity,TransactionResponse.class);

        assertEquals(201,responseResponseEntity.getStatusCodeValue());
    }
}