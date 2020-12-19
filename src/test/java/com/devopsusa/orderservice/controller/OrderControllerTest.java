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

    private static String str = "http://localhost:8083/api/orders";

    @MockBean
    private OrderRepository orderRepository;

    private RestTemplate template = new RestTemplate();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldPlaceFruitOrderTest() throws URISyntaxException {
        final String baseURL = str + "/fruit";
        URI uri = new URI(baseURL);
        Order order = new Order(103,"Orange",5,3.49);
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type","application/json");
        TransactionRequest transactionRequest = new TransactionRequest(order,payment);
        HttpEntity<TransactionRequest> httpEntity = new HttpEntity<TransactionRequest>(transactionRequest,httpHeaders);
        ResponseEntity<TransactionResponse> responseResponseEntity =
                this.template.postForEntity(uri,httpEntity,TransactionResponse.class);

        assertEquals(201,responseResponseEntity.getStatusCodeValue());
    }

    @Test
    public void shouldgetAllFruitOrderTest() throws URISyntaxException {
        final String baseURL = str + "/all";
        URI uri = new URI(baseURL);
        ResponseEntity<String> responseResponseEntity = this.template.getForEntity(uri,String.class);
        String orders = responseResponseEntity.getBody();
        System.out.println("The Orders Object = " + orders);

        assertEquals(200,responseResponseEntity.getStatusCodeValue());
    }

    @Test
    public void shouldgetFruitOrderByIdTest() throws URISyntaxException {
        final String baseURL = str + "/fruit/103";
        URI uri = new URI(baseURL);
        ResponseEntity<Order> responseResponseEntity = this.template.getForEntity(uri,Order.class);
        Order order = responseResponseEntity.getBody();
        System.out.println("The Orders Object with id 103 = " + order);

        assertEquals(200,responseResponseEntity.getStatusCodeValue());
    }

}