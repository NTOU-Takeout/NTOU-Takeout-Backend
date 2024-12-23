package com.ordernow.backend.store.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordernow.backend.auth.model.dto.LoginRequest;
import com.ordernow.backend.store.model.dto.StoreUpdateRequest;
import com.ordernow.backend.store.model.entity.Store;
import com.ordernow.backend.user.model.entity.Role;
import com.ordernow.backend.user.model.entity.User;
import com.ordernow.backend.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StoreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private Pair<LocalTime, LocalTime>[][] businessHours;

    public StoreControllerIntegrationTest() {
        this.businessHours = new Pair[7][2];
        initializeDefaultBusinessHours();
    }

    private void initializeDefaultBusinessHours() {
        for (int i = 0; i < 7; i++) {
            businessHours[i][0] = Pair.of(LocalTime.of(9, 0), LocalTime.of(12, 0));
            businessHours[i][1] = Pair.of(LocalTime.of(17, 0), LocalTime.of(20, 0));
        }
    }

    private void modifiedBusinessHours() {
        for (int i = 0; i < 7; i++) {
            businessHours[i][0] = Pair.of(LocalTime.of(7, 0), LocalTime.of(11, 0));
            businessHours[i][1] = Pair.of(LocalTime.of(15, 0), LocalTime.of(21, 0));
        }
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    private record MerchantSetupResult(String token, Store store) {}

    private MerchantSetupResult setupMerchant() throws Exception {
        String testEmail = "merchant@example.com";
        String testPassword = "password123";
        String testName = "測試商家";
        String testPhone = "0912345678";

        User merchantUser = new User(testName, testEmail, testPassword, Role.MERCHANT);
        merchantUser.setPhoneNumber(testPhone);

        mockMvc.perform(post("/api/v2/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(merchantUser)))
                .andExpect(status().isOk());
        
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(testEmail);
        loginRequest.setPassword(testPassword);

        String responseBody = mockMvc.perform(post("/api/v2/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = "Bearer " + objectMapper.readTree(responseBody)
                .path("data")
                .path("token")
                .asText();

        String storeId = objectMapper.readTree(responseBody)
        .path("data")
        .path("storeId")
        .asText();
        
        String storeDetailResponse = mockMvc.perform(get("/api/v2/stores/" + storeId)
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
                
        Store store = objectMapper.readValue(
            objectMapper.readTree(storeDetailResponse)
                .path("data")
                .toString(), 
            Store.class
        );

        return new MerchantSetupResult(token, store);
    }

    @Test
    void testSearchStores() throws Exception {
        mockMvc.perform(get("/api/v2/stores/search")
                .param("keyword", "測試")
                .param("sortBy", "rating")
                .param("sortDir", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testSearchStoresWithInvalidSortBy() throws Exception {
        mockMvc.perform(get("/api/v2/stores/search")
                .param("keyword", "測試")
                .param("sortBy", "invalid")
                .param("sortDir", "desc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSearchStoresWithInvalidSortDir() throws Exception {
        mockMvc.perform(get("/api/v2/stores/search")
                .param("keyword", "測試")
                .param("sortBy", "rating")
                .param("sortDir", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetStoresByIds() throws Exception {
        mockMvc.perform(post("/api/v2/stores/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Arrays.asList("store1", "store2"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetStoresByIdsWithEmptyList() throws Exception {
        mockMvc.perform(post("/api/v2/stores/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Collections.emptyList())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateStore() throws Exception {
        MerchantSetupResult merchantResult = setupMerchant();
        String merchantToken = merchantResult.token();
        Store store = merchantResult.store();

        StoreUpdateRequest updateRequest = new StoreUpdateRequest(
            "Updated Store Name",
            "https://example.com/avatar.jpg",
            "Updated Address",
            "Updated Store Description",
            businessHours
        );

        mockMvc.perform(put("/api/v2/stores/" + store.getId())
                .header("Authorization", merchantToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));

        String responseBody = mockMvc.perform(get("/api/v2/stores/" + store.getId())
                .header("Authorization", merchantToken))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
                
        Store updatedStore = objectMapper.readValue(
            objectMapper.readTree(responseBody)
                .path("data")
                .toString(), 
            Store.class
        );

        assertNotNull(updatedStore);
        assertEquals("Updated Store Name", updatedStore.getName());
        assertEquals("Updated Store Description", updatedStore.getDescription());
        assertEquals("Updated Address", updatedStore.getAddress());
        assertEquals("https://example.com/avatar.jpg", updatedStore.getPicture());
        assertArrayEquals(businessHours, updatedStore.getBusinessHours());
    }

    @Test
    void testUpdateStoreUnauthorized() throws Exception {
        MerchantSetupResult merchantResult = setupMerchant();
        Store store = merchantResult.store();

        StoreUpdateRequest updateRequest = new StoreUpdateRequest(
            "未授權的更新測試",
            "未授權的更新測試",
            "未授權的更新測試",
            "未授權的更新測試",
            businessHours
        );

        mockMvc.perform(put("/api/v2/stores/" + store.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testChangeBusinessStatus() throws Exception {
        MerchantSetupResult merchantResult = setupMerchant();
        String merchantToken = merchantResult.token();
        Store store = merchantResult.store();

        String initialResponseBody = mockMvc.perform(get("/api/v2/stores/" + store.getId())
                .header("Authorization", merchantToken))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
                
        Store initialStore = objectMapper.readValue(
            objectMapper.readTree(initialResponseBody)
                .path("data")
                .toString(), 
            Store.class
        );
        boolean initialStatus = initialStore.getIsBusiness();

        mockMvc.perform(patch("/api/v2/stores/" + store.getId() + "/isBusiness")
                .header("Authorization", merchantToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").value(!initialStatus));

        String updatedResponseBody = mockMvc.perform(get("/api/v2/stores/" + store.getId())
                .header("Authorization", merchantToken))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
                
        Store updatedStore = objectMapper.readValue(
            objectMapper.readTree(updatedResponseBody)
                .path("data")
                .toString(), 
            Store.class
        );

        assertNotNull(updatedStore);
        assertEquals(!initialStatus, updatedStore.getIsBusiness());
    }

    @Test
    void testChangeBusinessStatusUnauthorized() throws Exception {
        MerchantSetupResult merchantResult = setupMerchant();
        Store store = merchantResult.store();

        mockMvc.perform(patch("/api/v2/stores/" + store.getId() + "/isBusiness"))
                .andExpect(status().isUnauthorized());
    }
}