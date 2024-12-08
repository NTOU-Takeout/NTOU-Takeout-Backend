package com.ordernow.backend.menu.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordernow.backend.auth.model.dto.LoginRequest;
import com.ordernow.backend.auth.model.entity.Customer;
import com.ordernow.backend.auth.model.entity.Merchant;
import com.ordernow.backend.auth.model.entity.Role;
import com.ordernow.backend.auth.repository.UserRepository;
import com.ordernow.backend.common.dto.ApiResponse;
import com.ordernow.backend.menu.model.entity.*;
import com.ordernow.backend.menu.repository.DishRepository;
import com.ordernow.backend.menu.repository.MenuRepository;
import com.ordernow.backend.menu.service.MenuService;
import lombok.extern.log4j.Log4j;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.data.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MenuControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuService menuService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final String MERCHANT_EMAIL = "merchant@test.com";
    private final String MERCHANT_PASSWORD = "password123";
    private String merchantToken;
    private Menu testMenu;
    @Autowired
    private DishRepository dishRepository;

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
    void setUpEach() throws Exception {
        userRepository.deleteAll();
        menuRepository.deleteAll();
        dishRepository.deleteAll();

        Merchant merchant = new Merchant();
        merchant.setEmail(MERCHANT_EMAIL);
        merchant.setPassword(passwordEncoder.encode(MERCHANT_PASSWORD));
        merchant.setRole(Role.MERCHANT);
        userRepository.save(merchant);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(MERCHANT_EMAIL);
        loginRequest.setPassword(MERCHANT_PASSWORD);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        merchantToken = result.getResponse().getHeader("Authorization");

        testMenu = new Menu();
        testMenu.setId("testmenu001");
        List<Pair<String, List<String>>> categories = new ArrayList<>();

        categories.add(Pair.of(
            "test冰淇淋系列",
            Arrays.asList("testdish001", "testdish002")
        ));

        categories.add(Pair.of(
            "test冬季熱飲系列",
            Arrays.asList("testdish003", "testdish004")
        ));

        testMenu.setCategories(categories);
        testMenu = menuRepository.save(testMenu);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        menuRepository.deleteAll();
    }

    @Test
    void testGetMenu() throws Exception {
        mockMvc.perform(get("/api/v2/menu/" + testMenu.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(testMenu.getId()))
                .andExpect(jsonPath("$.data.categories[0].first").value("test冰淇淋系列"))
                .andExpect(jsonPath("$.data.categories[0].second[0]").value("testdish001"))
                .andExpect(jsonPath("$.data.categories[1].first").value("test冬季熱飲系列"));
    }

    @Test
    void testGetDishesByCategory() throws Exception {
        mockMvc.perform(get("/api/v2/menu/dishes")
                .param("category", "test冰淇淋系列"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testAddDishToMenu() throws Exception {
        Dish dish = new Dish();
        dish.setName("AddDishToMenu測試餐點");
        dish.setPrice(100.0);
        dish.setCategory("test冰淇淋系列");

        mockMvc.perform(post("/api/v2/menu/" + testMenu.getId() + "/dish")
                .header("Authorization", merchantToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dish)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v2/menu/dishes")
                .param("category", "test冰淇淋系列"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("AddDishToMenu測試餐點"))
                .andExpect(jsonPath("$.data[0].price").value(100.0))
                .andExpect(jsonPath("$.data[0].category").value("test冰淇淋系列"));
    }

    @Test
    void testAddDishToMenuUnauthorized() throws Exception {
        Dish dish = new Dish();
        dish.setName("AddDishToMenuUnauthorized測試餐點");
        dish.setPrice(100.0);
        dish.setCategory("test冰淇淋系列");

        mockMvc.perform(post("/api/v2/menu/" + testMenu.getId() + "/dish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dish)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateDishInMenu() throws Exception {
        Dish dish = new Dish();
        dish.setName("原始測試餐點");
        dish.setPrice(100.0);
        dish.setCategory("test冰淇淋系列");

        mockMvc.perform(post("/api/v2/menu/" + testMenu.getId() + "/dish")
                        .header("Authorization", merchantToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dish)))
                .andExpect(status().isOk());

        List<Dish> dishes = menuService.getDishesByCategory("test冰淇淋系列");
        String dishId = dishes.get(0).getId();
        assertEquals("原始測試餐點", dishes.get(0).getName());
        dish.setName("更新後的測試餐點");
        dish.setPrice(150.0);

        mockMvc.perform(patch("/api/v2/menu/" + testMenu.getId() + "/dish/" + dishId)
                .header("Authorization", merchantToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dish)))
                .andExpect(status().isOk());

        List<Dish> updatedDishes = menuService.getDishesByCategory("test冰淇淋系列");
        assertEquals(1, updatedDishes.size(), "There should be only one dish");
        assertEquals("更新後的測試餐點", updatedDishes.get(0).getName(), "Dish name should be updated");
        assertEquals(150.0, updatedDishes.get(0).getPrice(), "Dish price should be updated");
        assertEquals("test冰淇淋系列", updatedDishes.get(0).getCategory(), "Dish category should remain unchanged");
    }

    @Test
    void testUpdateDishInMenuUnauthorized() throws Exception {
        Dish dish = new Dish();
        dish.setName("未授權更新測試");
        dish.setPrice(100.0);

        mockMvc.perform(patch("/api/v2/menu/" + testMenu.getId() + "/dish/testdish001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dish)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteDishFromMenu() throws Exception {
        Dish dish = new Dish();
        dish.setName("待刪除測試餐點");
        dish.setPrice(100.0);
        dish.setCategory("test冰淇淋系列");

        mockMvc.perform(post("/api/v2/menu/" + testMenu.getId() + "/dish")
                .header("Authorization", merchantToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dish)))
                .andExpect(status().isOk());

        List<Dish> dishes = menuService.getDishesByCategory("test冰淇淋系列");
        String dishId = dishes.get(0).getId();
        assertEquals("待刪除測試餐點", dishes.get(0).getName());

        mockMvc.perform(delete("/api/v2/menu/" + testMenu.getId() + "/dish/" + dishId)
                .header("Authorization", merchantToken))
                .andExpect(status().isOk());

        dishes = menuService.getDishesByCategory("test冰淇淋系列");
        assertTrue(dishes.isEmpty());
        
        Menu updatedMenu = menuService.getMenuById(testMenu.getId());
        boolean categoryExists = updatedMenu.getCategories().stream()
                .anyMatch(category -> category.getFirst().equals("test冰淇淋系列") && 
                         category.getSecond().contains(dishId));
        assertFalse(categoryExists, "Dish ID should not exist in menu categories");
    }

    @Test
    void testDeleteDishFromMenuUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v2/menu/" + testMenu.getId() + "/dish/testdish001"))
                .andExpect(status().isUnauthorized());
    }

}