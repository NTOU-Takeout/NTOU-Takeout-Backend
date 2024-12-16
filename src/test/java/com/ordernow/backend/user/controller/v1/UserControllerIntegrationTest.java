package com.ordernow.backend.user.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordernow.backend.auth.model.dto.LoginRequest;
import com.ordernow.backend.user.model.dto.UserProfileRequest;
import com.ordernow.backend.user.model.entity.*;
import com.ordernow.backend.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    private String setupCustomer(String email, String password) throws Exception {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setRole(Role.CUSTOMER);
        customer.setName("測試顧客");
        userRepository.save(customer);

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
        merchant.setName("測試商家");
        merchant.setPhoneNumber("0912345678");
        userRepository.save(merchant);

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

    @Test
    void testUpdateCustomerProfile() throws Exception {
        String customerToken = setupCustomer("customer1@test.com", "password123");
        Customer customer = (Customer) userRepository.findByEmail("customer1@test.com");
        assertNotNull(customer);
        assertEquals("測試顧客", customer.getName());

        UserProfileRequest profileRequest = new UserProfileRequest();
        profileRequest.setName("更新後的顧客名稱");
        profileRequest.setPhoneNumber("0987654321");
        profileRequest.setAvatarUrl("https://example.com/new-avatar.jpg");
        profileRequest.setGender(Gender.FEMALE);

        mockMvc.perform(put("/api/v1/user")
                .header("Authorization", customerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));

        User updatedUser = userRepository.findById(customer.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals("更新後的顧客名稱", updatedUser.getName());
        assertEquals("0987654321", updatedUser.getPhoneNumber());
        assertEquals("https://example.com/new-avatar.jpg", updatedUser.getAvatarUrl());
        assertEquals(Gender.FEMALE, updatedUser.getGender());
    }

    @Test
    void testUpdateMerchantProfile() throws Exception {
        String merchantToken = setupMerchant("merchant1@test.com", "password123");
        Merchant merchant = (Merchant) userRepository.findByEmail("merchant1@test.com");
        assertNotNull(merchant);
        assertEquals("測試商家", merchant.getName());
        assertEquals("0912345678", merchant.getPhoneNumber());

        UserProfileRequest profileRequest = new UserProfileRequest();
        profileRequest.setName("更新後的商家名稱");
        profileRequest.setPhoneNumber("0923456789");
        profileRequest.setAvatarUrl("https://example.com/merchant-avatar.jpg");
        profileRequest.setGender(Gender.MALE);

        mockMvc.perform(put("/api/v1/user")
                .header("Authorization", merchantToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));

        User updatedUser = userRepository.findById(merchant.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals("更新後的商家名稱", updatedUser.getName());
        assertEquals("0923456789", updatedUser.getPhoneNumber());
        assertEquals("https://example.com/merchant-avatar.jpg", updatedUser.getAvatarUrl());
        assertEquals(Gender.MALE, updatedUser.getGender());
    }

    @Test
    void testUpdateProfileUnauthorized() throws Exception {
        UserProfileRequest profileRequest = new UserProfileRequest();
        profileRequest.setName("未授權的更新測試");
        profileRequest.setPhoneNumber("0912345678");

        mockMvc.perform(put("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateProfileWithInvalidRequest() throws Exception {
        String customerToken = setupCustomer("customer2@test.com", "password123");

        UserProfileRequest profileRequest = new UserProfileRequest();

        profileRequest.setName("");
        profileRequest.setPhoneNumber("0912345678");

        mockMvc.perform(put("/api/v1/user")
                .header("Authorization", customerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfileWithNonExistentUser() throws Exception {
        String customerToken = setupCustomer("customer3@test.com", "password123");
        Customer customer = (Customer) userRepository.findByEmail("customer3@test.com");
        userRepository.deleteById(customer.getId());

        UserProfileRequest profileRequest = new UserProfileRequest();
        profileRequest.setName("測試名稱");
        profileRequest.setPhoneNumber("0912345678");
        profileRequest.setAvatarUrl("https://example.com/avatar.jpg");
        profileRequest.setGender(Gender.MALE);

        mockMvc.perform(put("/api/v1/user")
                .header("Authorization", customerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found"));
    }
}