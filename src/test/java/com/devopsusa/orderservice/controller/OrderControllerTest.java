package com.devopsusa.orderservice.controller;

import com.devopsusa.orderservice.common.Payment;
import com.devopsusa.orderservice.common.TransactionRequest;
import com.devopsusa.orderservice.common.TransactionResponse;
import com.devopsusa.orderservice.domain.Order;
import com.devopsusa.orderservice.exception.RecordNotFoundException;
import com.devopsusa.orderservice.repository.OrderRepository;
import com.devopsusa.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private TransactionResponse transactionResponse;

    @MockBean
    private TransactionRequest transactionRequest;

    private static String str = "http://localhost:8083/api/orders";
    

    @Test
    @Disabled
    @DisplayName("POST /api/orders/placeorder")
    void shouldPlaceOrderTest() throws Exception {
        Order order = new Order(101,"Apple", 12,6.99);
        Payment payment = new Payment();
        transactionRequest = new TransactionRequest(order,payment);
        TransactionResponse transactionResponse = new TransactionResponse(order,6.99,"d96b1971-1c84-4aaf-9224-f62b6d8ae9a5","success","payment processing successful and order placed");
        when(orderService.saveOrder(transactionRequest)).thenReturn(transactionResponse);
//        doReturn(transactionResponse).when(orderService).saveOrder(transactionRequest);
        mockMvc.perform(post(str + "/placeorder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(transactionRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(101)))
                .andExpect(jsonPath("$.name", is("Apple")))
                .andExpect(jsonPath("$.qty", is(12)))
                .andExpect(jsonPath("$.price", is(6.99)))
                .andExpect(jsonPath("$.amount", is(6.99)))
                .andExpect(jsonPath("$.transactionId", is(anyString())))
                .andExpect(jsonPath("$.paymentStatus", is(anyString())))
                .andExpect(jsonPath("$.message", is(anyString())));
    }

    @Test
    @DisplayName("GET /api/orders/all")
    void shouldgetAllOrdersTest() throws Exception {
        Order order2 = new Order(101,"Apple", 12,6.99);
        Order order3 = new Order(102,"Banana",12,5.99);
        doReturn(Lists.newArrayList(order2,order3)).when(orderService).getallOrder();
        mockMvc.perform(get(str + "/all"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(101)))
                .andExpect(jsonPath("$[0].name", is("Apple")))
                .andExpect(jsonPath("$[0].qty", is(12)))
                .andExpect(jsonPath("$[0].price", is(6.99)))
                .andExpect(jsonPath("$[1].id", is(102)))
                .andExpect(jsonPath("$[1].name", is("Banana")))
                .andExpect(jsonPath("$[1].qty", is(12)))
                .andExpect(jsonPath("$[1].price", is(5.99)));
    }

    @Test
    @DisplayName("GET /api/orders/{id}")
    void shouldgetOrderById() throws Exception {
        Order orderbyid = new Order(103,"Orange",5,3.99);
        doReturn(orderbyid).when(orderService).getOrderById(103);
        mockMvc.perform(get(str + "/{id}", 103))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(103)))
                .andExpect(jsonPath("$.name", is("Orange")))
                .andExpect(jsonPath("$.qty", is(5)))
                .andExpect(jsonPath("$.price", is(3.99)));
    }

    @Test
    @Disabled
    @DisplayName("GET /api/orders/{id} Not Found!")
    void shouldgetFruitOrderByIdNotFound() throws RecordNotFoundException {

        doReturn(Optional.empty()).when(orderService).getOrderById(103);
        try {
            mockMvc.perform(get(str + "/{id}", 103))
                    // Validate the response code and content type
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Exception exception = assertThrows(NotFoundException.class, () -> {
//            orderService.getFruitById(103);
//        });
//        assertEquals("",exception.getMessage());
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}