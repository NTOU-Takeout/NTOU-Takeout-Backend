package com.ordernow.backend.order.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordernow.backend.auth.model.dto.LoginRequest;
import com.ordernow.backend.user.model.entity.Customer;
import com.ordernow.backend.user.model.entity.Merchant;
import com.ordernow.backend.user.model.entity.Role;
import com.ordernow.backend.auth.repository.UserRepository;
import com.ordernow.backend.order.model.entity.Order;
import com.ordernow.backend.order.model.entity.OrderedStatus;
import com.ordernow.backend.order.repository.OrderRepository;
import com.ordernow.backend.order.service.OrderService;
import com.ordernow.backend.user.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        orderRepository.deleteAll();
    }

    private String setupCustomer(String email, String password) throws Exception {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setRole(Role.CUSTOMER);
        customer.setName("Test Customer");
        userRepository.save(customer);

        User savedCustomer = userRepository.findByEmail(email);
        if (savedCustomer == null) {
            throw new RuntimeException("Customer was not saved properly");
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        String responseBody = mockMvc.perform(post("/api/v2/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readTree(responseBody)
                .path("data")
                .path("token")
                .asText();

        return "Bearer " + token;
    }

    private String setupMerchant(String email, String password) throws Exception {
        Merchant merchant = new Merchant();
        merchant.setEmail(email);
        merchant.setPassword(passwordEncoder.encode(password));
        merchant.setRole(Role.MERCHANT);
        merchant.setName("Test Merchant");
        merchant.setPhoneNumber("0912345678");
        userRepository.save(merchant);

        User savedMerchant = userRepository.findByEmail(email);
        if (savedMerchant == null) {
            throw new RuntimeException("Merchant was not saved properly");
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        String responseBody = mockMvc.perform(post("/api/v2/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readTree(responseBody)
                .path("data")
                .path("token")
                .asText();

        return "Bearer " + token;
    }

    private Order createTestOrder(String customerId, OrderedStatus status) {
        Order order = new Order();
        order.setId("testorder" + UUID.randomUUID().toString());
        order.setCustomerId(customerId);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Test
    void testCancelOrderByCustomer() throws Exception {
        String customerToken = setupCustomer("customer1@test.com", "password123");
        Customer customer = (Customer) userRepository.findByEmail("customer1@test.com");
        Order order = createTestOrder(customer.getId(), OrderedStatus.PENDING);

        mockMvc.perform(patch("/api/v1/order/" + order.getId() + "/cancel")
                .header("Authorization", customerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));

        Order canceledOrder = orderService.getOrderAndValid(order.getId());
        assertEquals(OrderedStatus.CANCELED, canceledOrder.getStatus());
    }

    @Test
    void testCancelOrderByMerchant() throws Exception {
        String merchantToken = setupMerchant("merchant1@test.com", "password123");
        Customer customer = new Customer();
        customer.setEmail("customer2@test.com");
        customer.setRole(Role.CUSTOMER);
        userRepository.save(customer);

        Order order = createTestOrder(customer.getId(), OrderedStatus.PENDING);

        mockMvc.perform(patch("/api/v1/order/" + order.getId() + "/cancel")
                .header("Authorization", merchantToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));

        Order canceledOrder = orderService.getOrderAndValid(order.getId());
        assertEquals(OrderedStatus.CANCELED, canceledOrder.getStatus());
    }

    @Test
    void testAcceptOrderByMerchant() throws Exception {
        String merchantToken = setupMerchant("merchant2@test.com", "password123");
        Customer customer = new Customer();
        customer.setEmail("customer3@test.com");
        customer.setRole(Role.CUSTOMER);
        userRepository.save(customer);

        Order order = createTestOrder(customer.getId(), OrderedStatus.PENDING);

        mockMvc.perform(patch("/api/v1/order/" + order.getId() + "/accept")
                .header("Authorization", merchantToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));

        Order acceptedOrder = orderService.getOrderAndValid(order.getId());
        assertEquals(OrderedStatus.PROCESSING, acceptedOrder.getStatus());
    }

    @Test
    void testAcceptOrderUnauthorized() throws Exception {
        String customerToken = setupCustomer("customer4@test.com", "password123");
        Customer customer = (Customer) userRepository.findByEmail("customer4@test.com");
        Order order = createTestOrder(customer.getId(), OrderedStatus.PENDING);

        mockMvc.perform(patch("/api/v1/order/" + order.getId() + "/accept")
                .header("Authorization", customerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCompleteOrderByMerchant() throws Exception {
        String merchantToken = setupMerchant("merchant3@test.com", "password123");
        Customer customer = new Customer();
        customer.setEmail("customer5@test.com");
        customer.setRole(Role.CUSTOMER);
        userRepository.save(customer);

        Order order = createTestOrder(customer.getId(), OrderedStatus.PROCESSING);

        mockMvc.perform(patch("/api/v1/order/" + order.getId() + "/complete")
                .header("Authorization", merchantToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));

        Order completedOrder = orderService.getOrderAndValid(order.getId());
        assertEquals(OrderedStatus.COMPLETED, completedOrder.getStatus());
    }

    @Test
    void testPickUpOrderByCustomer() throws Exception {
        String customerToken = setupCustomer("customer6@test.com", "password123");
        Customer customer = (Customer) userRepository.findByEmail("customer6@test.com");
        Order order = createTestOrder(customer.getId(), OrderedStatus.COMPLETED);

        mockMvc.perform(patch("/api/v1/order/" + order.getId() + "/pickup")
                .header("Authorization", customerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));

        Order pickedUpOrder = orderService.getOrderAndValid(order.getId());
        assertEquals(OrderedStatus.PICKED_UP, pickedUpOrder.getStatus());
    }

    @Test
    void testSearchOrdersByCustomer() throws Exception {
        String customerToken = setupCustomer("customer7@test.com", "password123");
        Customer customer = (Customer) userRepository.findByEmail("customer7@test.com");

        Order order1 = createTestOrder(customer.getId(), OrderedStatus.PENDING);
        Order order2 = createTestOrder(customer.getId(), OrderedStatus.PENDING);
        Order order3 = createTestOrder(customer.getId(), OrderedStatus.PROCESSING);

        List<Order> savedOrders = orderRepository.findAllByCustomerIdAndStatus(
            customer.getId(), 
            OrderedStatus.PENDING,
            PageRequest.of(0, 10)
        );
        assertEquals(2, savedOrders.size(), "Should have 2 PENDING orders saved");

        mockMvc.perform(get("/api/v1/order/search")
                .header("Authorization", customerToken)
                .param("status", "PENDING")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testSearchOrdersWithInvalidStatus() throws Exception {
        String customerToken = setupCustomer("customer8@test.com", "password123");

        mockMvc.perform(get("/api/v1/order/search")
                .header("Authorization", customerToken)
                .param("status", "INVALID_STATUS")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isBadRequest());
    }
}