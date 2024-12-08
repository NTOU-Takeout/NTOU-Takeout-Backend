//package com.ordernow.backend.service;
//
//import com.ordernow.backend.store.model.entity.Store;
//import com.ordernow.backend.store.repository.StoreRepository;
//import com.ordernow.backend.store.service.StoreService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class StoreServiceTest {
//
//    @Mock
//    private StoreRepository storeRepository;
//
//    @InjectMocks
//    private StoreService storeService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetStoresIdFilteredAndSortedByNameAsc() {
//        // Arrange
//        String keyword = "store";
//        List<Store> mockStores = List.of(
//                new Store("1", "store1", "pic1", "1234567890", "address1", 4.5, List.of(), "menu1", 20.0, "desc1", null),
//                new Store("2", "store2", "pic2", "0987654321", "address2", 4.0, List.of(), "menu2", 25.0, "desc2", null)
//        );
//        when(storeRepository.findByNameContainingOnlyIdOrderByNameAsc(keyword)).thenReturn(mockStores);
//
//        // Act
//        List<String> result = storeService.getStoresIdFilteredAndSorted(keyword, "name", "asc");
//
//        // Assert
//        assertEquals(List.of("1", "2"), result);
//        verify(storeRepository).findByNameContainingOnlyIdOrderByNameAsc(keyword);
//    }
//    @Test
//    void testGetStoresIdFilteredAndSortedByNameDesc() {
//        // Arrange
//        String keyword = "store";
//        List<Store> mockStores = List.of(
//                new Store("2", "store2", "pic2", "0987654321", "address2", 4.0, List.of(), "menu2", 25.0, "desc2", null),
//                new Store("1", "store1", "pic1", "1234567890", "address1", 4.5, List.of(), "menu1", 20.0, "desc1", null)
//        );
//        when(storeRepository.findByNameContainingOnlyIdOrderByNameDesc(keyword)).thenReturn(mockStores);
//
//        // Act
//        List<String> result = storeService.getStoresIdFilteredAndSorted(keyword, "name", "desc");
//
//        // Assert
//        assertEquals(List.of("2", "1"), result);
//        verify(storeRepository).findByNameContainingOnlyIdOrderByNameDesc(keyword);
//    }
//
//    @Test
//    void testGetStoresIdFilteredAndSortedByRatingAsc() {
//        String keyword = "store";
//        List<Store> mockStores = List.of(
//                new Store("2", "store2", "pic2", "0987654321", "address2", 4.0, List.of(), "menu2", 25.0, "desc2", null),
//                new Store("1", "store1", "pic1", "1234567890", "address1", 4.5, List.of(), "menu1", 20.0, "desc1", null)
//        );
//        when(storeRepository.findByNameContainingOnlyIdOrderByRatingAsc(keyword)).thenReturn(mockStores);
//
//        List<String> result = storeService.getStoresIdFilteredAndSorted(keyword, "rating", "asc");
//
//        assertEquals(List.of("2", "1"), result);
//        verify(storeRepository).findByNameContainingOnlyIdOrderByRatingAsc(keyword);
//    }
//    @Test
//    void testGetStoresIdFilteredAndSortedByRatingDesc() {
//        String keyword = "store";
//        List<Store> mockStores = List.of(
//                new Store("1", "store1", "pic1", "1234567890", "address1", 4.5, List.of(), "menu1", 20.0, "desc1", null),
//                new Store("2", "store2", "pic2", "0987654321", "address2", 4.0, List.of(), "menu2", 25.0, "desc2", null)
//        );
//        when(storeRepository.findByNameContainingOnlyIdOrderByRatingDesc(keyword)).thenReturn(mockStores);
//
//        List<String> result = storeService.getStoresIdFilteredAndSorted(keyword, "rating", "desc");
//
//        assertEquals(List.of("1", "2"), result);
//        verify(storeRepository).findByNameContainingOnlyIdOrderByRatingDesc(keyword);
//    }
//
//    @Test
//    void testGetStoresIdFilteredAndSortedByAverageSpendAsc() {
//        String keyword = "store";
//        List<Store> mockStores = List.of(
//                new Store("1", "store1", "pic1", "1234567890", "address1", 4.5, List.of(), "menu1", 20.0, "desc1", null),
//                new Store("2", "store2", "pic2", "0987654321", "address2", 4.0, List.of(), "menu2", 25.0, "desc2", null)
//        );
//        when(storeRepository.findByNameContainingOnlyIdOrderByAverageSpendAsc(keyword)).thenReturn(mockStores);
//
//        List<String> result = storeService.getStoresIdFilteredAndSorted(keyword, "averageSpend", "asc");
//
//        assertEquals(List.of("1", "2"), result);
//        verify(storeRepository).findByNameContainingOnlyIdOrderByAverageSpendAsc(keyword);
//    }
//    @Test
//    void testGetStoresIdFilteredAndSortedByAverageSpendDesc() {
//        String keyword = "store";
//        List<Store> mockStores = List.of(
//                new Store("2", "store2", "pic2", "0987654321", "address2", 4.0, List.of(), "menu2", 25.0, "desc2", null),
//                new Store("1", "store1", "pic1", "1234567890", "address1", 4.5, List.of(), "menu1", 20.0, "desc1", null)
//        );
//        when(storeRepository.findByNameContainingOnlyIdOrderByAverageSpendDesc(keyword)).thenReturn(mockStores);
//
//        List<String> result = storeService.getStoresIdFilteredAndSorted(keyword, "averageSpend", "desc");
//
//        assertEquals(List.of("2", "1"), result);
//        verify(storeRepository).findByNameContainingOnlyIdOrderByAverageSpendDesc(keyword);
//    }
//
//    @Test
//    void testGetStoreByIds() {
//        // Arrange
//        List<String> ids = List.of("1", "2");
//        Store store1 = new Store("1", "store1", "pic1", "1234567890", "address1", 4.5, List.of("review1"), "menu1", 20.0, "desc1", null);
//        Store store2 = new Store("2", "store2", "pic2", "0987654321", "address2", 4.0, List.of("review2"), "menu2", 25.0, "desc2", null);
//
//        when(storeRepository.findById("1")).thenReturn(Optional.of(store1));
//        when(storeRepository.findById("2")).thenReturn(Optional.of(store2));
//
//        // Act
//        List<Store> result = storeService.getStoreByIds(ids);
//
//        // Assert
//        assertEquals(List.of(store1, store2), result);
//        verify(storeRepository).findById("1");
//        verify(storeRepository).findById("2");
//    }
//
//}
