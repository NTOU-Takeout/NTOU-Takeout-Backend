package com.ordernow.backend.auth.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordernow.backend.auth.model.dto.LoginRequest;
import com.ordernow.backend.auth.model.entity.Customer;
import com.ordernow.backend.auth.model.entity.Role;
import com.ordernow.backend.auth.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
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

    private final String TEST_REGISTERED_MAIL = "registered@example.com";
    private final String TEST_REGISTERED_PASSWORD = "registeredpassword";
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "password123";
    private final String TEST_NAME = "Test User";

    @BeforeAll
    static void setUp() {
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");
        String testingDbName = System.getenv("TESTING_DB_NAME");
        log.info(dbUser);
        log.info(dbPassword);
        log.info(testingDbName);
        if (dbUser == null || dbPassword == null || testingDbName == null) {
            throw new IllegalStateException(
                    "Required environment variables not set. Please ensure DB_USER, DB_PASSWORD and TESTING_DB_NAME are set"
            );
        }
    }

    @BeforeEach
    void setUpEach() {
        userRepository.deleteAll();

        Customer registeredCustomer = new Customer();
        registeredCustomer.setEmail(TEST_REGISTERED_MAIL);
        registeredCustomer.setPassword(passwordEncoder.encode(TEST_REGISTERED_PASSWORD));
        registeredCustomer.setRole(Role.CUSTOMER);
        registeredCustomer.setName(TEST_NAME);
        
        userRepository.save(registeredCustomer);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testExistingUserLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(TEST_REGISTERED_MAIL);
        loginRequest.setPassword(TEST_REGISTERED_PASSWORD);

        mockMvc.perform(post("/api/v2/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.email").value(TEST_REGISTERED_MAIL))
                .andExpect(jsonPath("$.data.role").value("CUSTOMER"))
                .andExpect(jsonPath("$.data.token").exists());

    }

    @Test
    void testRegisterAndLoginFlow() throws Exception {
        Customer customer = new Customer();
        customer.setEmail(TEST_EMAIL);
        customer.setPassword(TEST_PASSWORD);
        customer.setRole(Role.CUSTOMER);
        customer.setName(TEST_NAME);

        mockMvc.perform(post("/api/v2/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(TEST_EMAIL);
        loginRequest.setPassword(TEST_PASSWORD);

        mockMvc.perform(post("/api/v2/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.data.role").value("CUSTOMER"))
                .andExpect(jsonPath("$.data.token").exists());
    }

    @Test
    void testRegisterWithExistingEmail() throws Exception {
        Customer customer = new Customer();
        customer.setEmail(TEST_REGISTERED_MAIL);
        customer.setPassword(TEST_PASSWORD);
        customer.setRole(Role.CUSTOMER);
        customer.setName(TEST_NAME);
        
        mockMvc.perform(post("/api/v2/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }
}