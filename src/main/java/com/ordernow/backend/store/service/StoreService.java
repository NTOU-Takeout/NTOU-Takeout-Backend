package com.ordernow.backend.store.service;

import com.ordernow.backend.menu.service.MenuService;
import com.ordernow.backend.store.model.dto.StoreUpdateRequest;
import com.ordernow.backend.store.model.entity.Store;
import com.ordernow.backend.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final MenuService menuService;

    @Autowired
    public StoreService(StoreRepository storeRepository, MenuService menuService) {
        this.storeRepository = storeRepository;
        this.menuService = menuService;
    }

    public List<String> getStoresIdFilteredAndSorted(String keyword, String sortBy, String sortDir) throws InvalidParameterException {
        List<Store> stores;
        switch (sortBy) {
            case "name" -> stores = sortDir.equals("asc")
                    ? storeRepository.findByNameContainingOnlyIdOrderByNameAsc(keyword)
                    : storeRepository.findByNameContainingOnlyIdOrderByNameDesc(keyword);
            case "rating" -> stores = sortDir.equals("asc")
                    ? storeRepository.findByNameContainingOnlyIdOrderByRatingAsc(keyword)
                    : storeRepository.findByNameContainingOnlyIdOrderByRatingDesc(keyword);
            case "averageSpend" -> stores = sortDir.equals("asc")
                    ? storeRepository.findByNameContainingOnlyIdOrderByAverageSpendAsc(keyword)
                    : storeRepository.findByNameContainingOnlyIdOrderByAverageSpendDesc(keyword);
            default -> {
                return null;
            }
        }
        return stores.stream().map(Store::getId).toList();
    }

    public Store getStoreById(String storeId)
            throws NoSuchElementException {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new NoSuchElementException("Store not found"));
    }

    public List<Store> getStoreByIds(List<String> ids) {
        return ids.stream()
                .map(storeRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public String createAndSaveStore(String phoneNumber) {
        Store store = Store.createDefaultStore();
        String menuId = menuService.createAndSaveMenu();
        store.setMenuId(menuId);
        store.setPhoneNumber(phoneNumber);
        return storeRepository.save(store).getId();
    }

    public void updateStore(String storeId, StoreUpdateRequest request)
            throws NoSuchElementException {

        Store currentStore = getStoreById(storeId);
        currentStore.setName(request.getName());
        currentStore.setPicture(request.getPicture());
        currentStore.setAddress(request.getAddress());
        currentStore.setDescription(request.getDescription());
        currentStore.setBusinessHours(request.getBusinessHours());
        storeRepository.save(currentStore);
    }

    public boolean changeBusinessStatus(String storeId)
            throws NoSuchElementException {
        Store store = getStoreById(storeId);
        store.setIsBusiness(!store.getIsBusiness());
        storeRepository.save(store);
        return store.getIsBusiness();
    }
}
