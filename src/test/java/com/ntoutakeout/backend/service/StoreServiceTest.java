package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStoresIdFilteredAndSortedByNameAsc() {
        // Arrange
        String keyword = "store";
        List<Store> mockStores = List.of(
                new Store("1", "store1", "pic1", "1234567890", "address1", 4.5, List.of(), "menu1", 20.0, "desc1", null),
                new Store("2", "store2", "pic2", "0987654321", "address2", 4.0, List.of(), "menu2", 25.0, "desc2", null)
        );
        when(storeRepository.findByNameContainingOnlyIdOrderByNameAsc(keyword)).thenReturn(mockStores);

        // Act
        List<String> result = storeService.getStoresIdFilteredAndSorted(keyword, "name", "asc");

        // Assert
        assertEquals(List.of("1", "2"), result);
        verify(storeRepository).findByNameContainingOnlyIdOrderByNameAsc(keyword);
    }

    @Test
    void testGetStoreByIds() {
        // Arrange
        List<String> ids = List.of("1", "2");
        Store store1 = new Store("1", "store1", "pic1", "1234567890", "address1", 4.5, List.of("review1"), "menu1", 20.0, "desc1", null);
        Store store2 = new Store("2", "store2", "pic2", "0987654321", "address2", 4.0, List.of("review2"), "menu2", 25.0, "desc2", null);

        when(storeRepository.findById("1")).thenReturn(Optional.of(store1));
        when(storeRepository.findById("2")).thenReturn(Optional.of(store2));

        // Act
        List<Store> result = storeService.getStoreByIds(ids);

        // Assert
        assertEquals(List.of(store1, store2), result);
        verify(storeRepository).findById("1");
        verify(storeRepository).findById("2");
    }

}
