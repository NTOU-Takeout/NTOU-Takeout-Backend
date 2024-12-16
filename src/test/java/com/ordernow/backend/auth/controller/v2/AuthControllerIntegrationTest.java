package com.ordernow.backend.auth.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordernow.backend.auth.model.dto.LoginRequest;
import com.ordernow.backend.auth.model.entity.Customer;
import com.ordernow.backend.auth.model.entity.Merchant;
import com.ordernow.backend.auth.model.entity.Role;
import com.ordernow.backend.auth.model.entity.User;
import com.ordernow.backend.auth.repository.UserRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Slf4j
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);


    @BeforeEach
    void setUpEach() {
        userRepository.deleteAll();
    }   

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testExistingUserLogin() throws Exception {
        String testEmail = "registered@example.com";
        String testPassword = "registeredpassword";
        String testName = "Test User";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

        User user = new User();
        user.setEmail(testEmail);
        user.setPassword(passwordEncoder.encode(testPassword));
        user.setRole(Role.CUSTOMER);
        user.setName(testName);
        
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(testEmail);
        loginRequest.setPassword(testPassword);

        mockMvc.perform(post("/api/v2/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.email").value(testEmail))
                .andExpect(jsonPath("$.data.role").value("CUSTOMER"))
                .andExpect(jsonPath("$.data.token").exists());
    }

    @Test
    void testRegisterAndLoginFlow() throws Exception {
        String testEmail = "test@example.com";
        String testPassword = "password123";
        String testName = "Test User";

        User user = new User();
        user.setName(testName);
        user.setEmail(testEmail);
        user.setPassword(testPassword);
        user.setRole(Role.CUSTOMER);

        mockMvc.perform(post("/api/v2/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(testEmail);
        loginRequest.setPassword(testPassword);

        mockMvc.perform(post("/api/v2/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.email").value(testEmail))
                .andExpect(jsonPath("$.data.role").value("CUSTOMER"))
                .andExpect(jsonPath("$.data.token").exists());
    }

    @Test
    void testRegisterWithExistingEmail() throws Exception {
        String testRegisteredEmail = "registered@example.com";
        String testRegisteredPassword = "registeredpassword";
        String testRegisteredName = "Test User";

        User user = new User();
        user.setEmail(testRegisteredEmail);
        user.setPassword(testRegisteredPassword);
        user.setRole(Role.CUSTOMER);
        user.setName(testRegisteredName);
        
        userRepository.save(user);
        
        mockMvc.perform(post("/api/v2/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    void testRegisterCustomerSuccessfully() throws Exception {
        String testEmail = "customer@example.com";
        String testPassword = "password123";
        String testName = "Test Customer";

        User user = new User();
        user.setName(testName);
        user.setEmail(testEmail);
        user.setPassword(testPassword);
        user.setRole(Role.CUSTOMER);

        mockMvc.perform(post("/api/v2/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));

        User savedUser = userRepository.findByEmail(testEmail);
        assertNotNull(savedUser);
        assertEquals(testName, savedUser.getName());
        assertEquals(testEmail, savedUser.getEmail());
        assertEquals(Role.CUSTOMER, savedUser.getRole());
        assertTrue(passwordEncoder.matches(testPassword, savedUser.getPassword()));
        assertInstanceOf(Customer.class, savedUser);
    }

    @Test
    void testRegisterMerchantSuccessfully() throws Exception {
        String testEmail = "merchant@example.com";
        String testPassword = "password123";
        String testName = "Test Merchant";

        User user = new User();
        user.setName(testName);
        user.setEmail(testEmail);
        user.setPassword(testPassword);
        user.setRole(Role.MERCHANT);

        mockMvc.perform(post("/api/v2/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));

        User savedUser = userRepository.findByEmail(testEmail);
        assertNotNull(savedUser);
        assertEquals(testName, savedUser.getName());
        assertEquals(testEmail, savedUser.getEmail());
        assertEquals(Role.MERCHANT, savedUser.getRole());
        assertTrue(passwordEncoder.matches(testPassword, savedUser.getPassword()));
        assertInstanceOf(Merchant.class, savedUser);
    }

    @Test
    void testRegisterWithTooLongName() throws Exception {
        String testEmail = "test@example.com";
        String testPassword = "password123";
        String testName = "This name is way too long and should exceed the twenty character limit";

        User user = new User();
        user.setName(testName);
        user.setEmail(testEmail);
        user.setPassword(testPassword);
        user.setRole(Role.CUSTOMER);

        mockMvc.perform(post("/api/v2/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Name is too long"));

        assertNull(userRepository.findByEmail(testEmail));
    }

    @Test
    void testRegisterWithInvalidRole() throws Exception {
        String testEmail = "test@example.com";
        String testPassword = "password123";
        String testName = "test Unknown Role";

        User user = new User();
        user.setName(testName);
        user.setEmail(testEmail);
        user.setPassword(testPassword);
        user.setRole(null);

        mockMvc.perform(post("/api/v2/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid role"));

        assertNull(userRepository.findByEmail(testEmail));
    }

    @Test
    void testLoginWithNonExistingEmail() throws Exception {
        String nonExistingEmail = "nonexisting@example.com";
        String testPassword = "password123";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(nonExistingEmail);
        loginRequest.setPassword(testPassword);

        mockMvc.perform(post("/api/v2/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLoginWithWrongPassword() throws Exception {
        String testEmail = "test@example.com";
        String correctPassword = "correctpassword";
        String wrongPassword = "wrongpassword";
        String testName = "Test User";

        User user = new User();
        user.setEmail(testEmail);
        user.setPassword(passwordEncoder.encode(correctPassword));
        user.setRole(Role.CUSTOMER);
        user.setName(testName);
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(testEmail);
        loginRequest.setPassword(wrongPassword);

        mockMvc.perform(post("/api/v2/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}