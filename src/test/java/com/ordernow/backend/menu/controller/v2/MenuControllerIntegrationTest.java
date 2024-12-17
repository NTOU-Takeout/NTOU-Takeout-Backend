//package com.ordernow.backend.menu.controller.v2;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ordernow.backend.auth.model.dto.LoginRequest;
//import com.ordernow.backend.user.model.entity.Role;
//import com.ordernow.backend.auth.repository.UserRepository;
//import com.ordernow.backend.menu.model.entity.*;
//import com.ordernow.backend.menu.repository.DishRepository;
//import com.ordernow.backend.menu.repository.MenuRepository;
//import com.ordernow.backend.menu.service.MenuService;
//import com.ordernow.backend.user.model.entity.User;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.data.util.Pair;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import lombok.extern.slf4j.Slf4j;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@Slf4j
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//public class MenuControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private MenuRepository menuRepository;
//
//    @Autowired
//    private DishRepository dishRepository;
//
//    @Autowired
//    private MenuService menuService;
//
//    @BeforeEach
//    void setUpEach() throws Exception {
//        userRepository.deleteAll();
//        menuRepository.deleteAll();
//        dishRepository.deleteAll();
//    }
//
//    @AfterEach
//    void tearDown() {
//        userRepository.deleteAll();
//        menuRepository.deleteAll();
//        dishRepository.deleteAll();
//    }
//
//    private String setupMerchant() throws Exception {
//
//        String testEmail = "merchant@example.com";
//        String testPassword = "password123";
//        String testName = "測試商家";
//        String testPhone = "0912345678";
//
//        User merchantUser = new User(testName, testEmail, testPassword, Role.MERCHANT);
//        merchantUser.setPhoneNumber(testPhone);
//
//        mockMvc.perform(post("/api/v2/auth/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(merchantUser)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(200))
//                .andExpect(jsonPath("$.message").value("Success"));
//
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail(testEmail);
//        loginRequest.setPassword(testPassword);
//
//        String responseBody = mockMvc.perform(post("/api/v2/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(200))
//                .andExpect(jsonPath("$.message").value("Success"))
//                .andExpect(jsonPath("$.data.id").exists())
//                .andExpect(jsonPath("$.data.name").value(testName))
//                .andExpect(jsonPath("$.data.email").value(testEmail))
//                .andExpect(jsonPath("$.data.avatarUrl").exists())
//                .andExpect(jsonPath("$.data.role").value("MERCHANT"))
//                .andExpect(jsonPath("$.data.token").exists())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        String token = objectMapper.readTree(responseBody)
//                .path("data")
//                .path("token")
//                .asText();
//
//        return "Bearer " + token;
//    }
//
//    private Dish createTestDish(String name, double price, String category, String description) {
//        Dish dish = new Dish();
//        dish.setName(name);
//        dish.setPrice(price);
//        dish.setCategory(category);
//        dish.setDescription(description);
//        dish.setPicture("https://example.com/test.jpg");
//        dish.setSalesVolume(0);
//
//        List<DishAttribute> attributes = new ArrayList<>();
//
//        DishAttribute tempAttribute = new DishAttribute();
//        tempAttribute.setName("溫度選擇");
//        tempAttribute.setDescription("可選擇飲品的溫度");
//        tempAttribute.setType("single");
//        tempAttribute.setAttributeOptions(Arrays.asList(
//            new AttributeOption("正常冰", 0.0, false),
//            new AttributeOption("少冰", 0.0, false)
//        ));
//        attributes.add(tempAttribute);
//
//        DishAttribute sweetAttribute = new DishAttribute();
//        sweetAttribute.setName("甜度選擇");
//        sweetAttribute.setDescription("根據個人口味調整甜度");
//        sweetAttribute.setType("single");
//        sweetAttribute.setAttributeOptions(Arrays.asList(
//            new AttributeOption("全糖", 0.0, false),
//            new AttributeOption("半糖", 0.0, false)
//        ));
//        attributes.add(sweetAttribute);
//
//        dish.setDishAttributes(attributes);
//        return dish;
//    }
//
//    private Menu createTestMenu() throws Exception {
//        String merchantToken = setupMerchant();
//
//        Dish dish1 = createTestDish(
//            "冰淇淋系列測試餐點1",
//            100.0,
//            "test冰淇淋系列",
//            "這是冰淇淋系列的第一個測試餐點"
//        );
//
//        Dish dish2 = createTestDish(
//            "冰淇淋系列測試餐點2",
//            120.0,
//            "test冰淇淋系列",
//            "這是冰淇淋系列的第二個測試餐點"
//        );
//
//        // 創建並添加第二個類別的餐點
//        Dish dish3 = createTestDish(
//            "冬季熱飲系列測試餐點1",
//            80.0,
//            "test冬季熱飲系列",
//            "這是冬季熱飲系列的第一個測試餐點"
//        );
//
//        Dish dish4 = createTestDish(
//            "冬季熱飲系列測試餐點2",
//            90.0,
//            "test冬季熱飲系列",
//            "這是冬季熱飲系列的第二個測試餐點"
//        );
//
//        mockMvc.perform(post("/api/v2/menu/" + testMenu.getId() + "/dish")
//                .header("Authorization", merchantToken)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dish1)))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(post("/api/v2/menu/" + testMenu.getId() + "/dish")
//                .header("Authorization", merchantToken)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dish2)))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(post("/api/v2/menu/" + testMenu.getId() + "/dish")
//                .header("Authorization", merchantToken)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dish3)))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(post("/api/v2/menu/" + testMenu.getId() + "/dish")
//                .header("Authorization", merchantToken)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dish4)))
//                .andExpect(status().isOk());
//
//        return menuRepository.findById(testMenu.getId()).orElseThrow();
//    }
//
//    @Test
//    void testGetMenu() throws Exception {
//        Menu testMenu = createTestMenu();
//
//        mockMvc.perform(get("/api/v2/menu/" + testMenu.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id").value(testMenu.getId()))
//                .andExpect(jsonPath("$.data.categories[0].first").value("test冰淇淋系列"))
//                .andExpect(jsonPath("$.data.categories[0].second[0]").value("testdish001"))
//                .andExpect(jsonPath("$.data.categories[1].first").value("test冬季熱飲系列"));
//    }
//
//    @Test
//    void testGetDishesByCategory() throws Exception {
//        Menu testMenu = createTestMenu();
//
//        mockMvc.perform(get("/api/v2/menu/dishes")
//                .param("category", "test冰淇淋系列"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").isArray());
//    }
//
//    @Test
//    void testAddDishToMenu() throws Exception {
//        Menu testMenu = createTestMenu();
//        String merchantToken = setupMerchant();
//
//        Dish dish = createTestDish(
//            "AddDishToMenu測試餐點",
//            100.0,
//            "test冰淇淋系列",
//            "這是AddDishToMenu的測試餐點描述"
//        );
//
//        mockMvc.perform(post("/api/v2/menu/" + testMenu.getId() + "/dish")
//                .header("Authorization", merchantToken)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dish)))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/api/v2/menu/dishes")
//                .param("category", "test冰淇淋系列"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data[0].name").value("AddDishToMenu測試餐點"))
//                .andExpect(jsonPath("$.data[0].price").value(100.0))
//                .andExpect(jsonPath("$.data[0].category").value("test冰淇淋系列"));
//    }
//
//    @Test
//    void testAddDishToMenuUnauthorized() throws Exception {
//        Menu testMenu = createTestMenu();
//
//        Dish dish = createTestDish(
//            "AddDishToMenuUnauthorized測試餐點",
//            100.0,
//            "test冰淇淋系列",
//            "這是AddDishToMenuUnauthorized的測試餐點描述"
//        );
//
//        mockMvc.perform(post("/api/v2/menu/" + testMenu.getId() + "/dish")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dish)))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void testUpdateDishInMenu() throws Exception {
//        Menu testMenu = createTestMenu();
//        String merchantToken = setupMerchant();
//
//        Dish dish = createTestDish(
//            "原始測試餐點",
//            100.0,
//            "test冰淇淋系列",
//            "這是原始測試餐點的描述"
//        );
//
//        mockMvc.perform(post("/api/v2/menu/" + testMenu.getId() + "/dish")
//                        .header("Authorization", merchantToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dish)))
//                        .andExpect(status().isOk());
//
//        List<Dish> dishes = menuService.getDishesByCategory("test冰淇淋系列");
//        String dishId = dishes.get(0).getId();
//        assertEquals("原始測試餐點", dishes.get(0).getName());
//
//        dish = createTestDish(
//            "更新後的測試餐點",
//            150.0,
//            "test冰淇淋系列",
//            "這是更新後的測試餐點描述"
//        );
//
//        mockMvc.perform(put("/api/v2/menu/" + testMenu.getId() + "/dish/" + dishId)
//                .header("Authorization", merchantToken)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dish)))
//                .andExpect(status().isOk());
//
//        List<Dish> updatedDishes = menuService.getDishesByCategory("test冰淇淋系列");
//        assertEquals(1, updatedDishes.size(), "There should be only one dish");
//        assertEquals("更新後的測試餐點", updatedDishes.get(0).getName(), "Dish name should be updated");
//        assertEquals(150.0, updatedDishes.get(0).getPrice(), "Dish price should be updated");
//        assertEquals("test冰淇淋系列", updatedDishes.get(0).getCategory(), "Dish category should remain unchanged");
//    }
//
//    @Test
//    void testUpdateDishInMenuUnauthorized() throws Exception {
//        Menu testMenu = createTestMenu();
//
//        Dish dish = createTestDish(
//            "未授權更新測試",
//            100.0,
//            "test冰淇淋系列",
//            "這是未授權更新測試的描述"
//        );
//
//        mockMvc.perform(put("/api/v2/menu/" + testMenu.getId() + "/dish/testdish001")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dish)))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void testDeleteDishFromMenu() throws Exception {
//        Menu testMenu = createTestMenu();
//        String merchantToken = setupMerchant();
//
//        Dish dish = createTestDish(
//            "待刪除測試餐點",
//            100.0,
//            "test冰淇淋系列",
//            "這是一個待刪除的測試餐點描述"
//        );
//
//        mockMvc.perform(post("/api/v2/menu/" + testMenu.getId() + "/dish")
//                .header("Authorization", merchantToken)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dish)))
//                .andExpect(status().isOk());
//
//        List<Dish> dishes = menuService.getDishesByCategory("test冰淇淋系列");
//        String dishId = dishes.get(0).getId();
//        assertEquals("待刪除測試餐點", dishes.get(0).getName());
//
//        mockMvc.perform(delete("/api/v2/menu/" + testMenu.getId() + "/dish/" + dishId)
//                .header("Authorization", merchantToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(200))
//                .andExpect(jsonPath("$.message").value("Success"));
//
//        dishes = menuService.getDishesByCategory("test冰淇淋系列");
//        assertTrue(dishes.isEmpty());
//
//        Menu updatedMenu = menuService.getMenuById(testMenu.getId());
//        boolean categoryExists = updatedMenu.getCategories().stream()
//                .anyMatch(category -> category.getFirst().equals("test冰淇淋系列") &&
//                         category.getSecond().contains(dishId));
//        assertFalse(categoryExists, "Dish ID should not exist in menu categories");
//    }
//
//    @Test
//    void testDeleteDishFromMenuUnauthorized() throws Exception {
//        Menu testMenu = createTestMenu();
//
//        mockMvc.perform(delete("/api/v2/menu/" + testMenu.getId() + "/dish/testdish001"))
//                .andExpect(status().isUnauthorized());
//    }
//
//}