package com.ntoutakeout.backend;

import com.ntoutakeout.backend.controller.StoreController;
import com.ntoutakeout.backend.entity.Menu;
import com.ntoutakeout.backend.entity.Review;
import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.service.StoreService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class NtouTakeoutBackendApplicationTests {


    @Mock
    private StoreService storeService;

    @InjectMocks
    private StoreController storeController;

    public void StoreControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStores() {
        List<Store> mockStores = Collections.singletonList(new Store());
        when(storeService.getStoresFilteredAndSorted("", "name", "asc")).thenReturn(mockStores);

        ResponseEntity<List<Store>> response = storeController.getStores("", "name", "asc");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockStores, response.getBody());
    }

    @Test
    public void testGetIdList() {
        List<String> mockIdList = Collections.singletonList("store1");
        when(storeService.getStoresIdFilteredAndSorted("", "name", "asc")).thenReturn(mockIdList);

        ResponseEntity<Map<String, List<String>>> response = storeController.getIdList("", "name", "asc");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockIdList, response.getBody().get("ids"));
    }

    @Test
    public void testGetStoresByIdList() {
        List<String> storeIds = Collections.singletonList("store1");
        List<Store> mockStores = Collections.singletonList(new Store());
        when(storeService.getStoreListByIds(storeIds)).thenReturn(mockStores);

        Map<String, List<String>> request = new HashMap<>();
        request.put("ids", storeIds);

        ResponseEntity<Map<String, List<Store>>> response = storeController.getStoresByIdList(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockStores, response.getBody().get("stores"));
    }

    @Test
    public void testGetReviewByStoreId() {
        List<Review> mockReviews = Collections.singletonList(new Review());
        when(storeService.getReviewById("store1")).thenReturn(mockReviews);

        ResponseEntity<Map<String, List<Review>>> response = storeController.getReviewByStoreId("store1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockReviews, response.getBody().get("reviews"));
    }

    @Test
    public void testGetMenuByStoreId() {
        List<Menu> mockMenu = Collections.singletonList(new Menu());
        when(storeService.getMenuById("store1")).thenReturn(mockMenu);

        ResponseEntity<Map<String, List<Menu>>> response = storeController.getMenuByStoreId("store1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockMenu, response.getBody().get("menu"));
    }

}
