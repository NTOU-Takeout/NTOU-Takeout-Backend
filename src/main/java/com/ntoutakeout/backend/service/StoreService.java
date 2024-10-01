package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    public List<Store> getStoresSortedByRating() {
        List<Store> storeList = storeRepository.getStoreList();
        storeList.sort(Comparator.comparingDouble(Store::getRank).reversed());
        return storeList;
    }

    public List<Store> getStoresSortedByTime() {
        return storeRepository.getStoreList();
    }

    // Search stores by name (case-insensitive partial match)
    public List<Store> findStoresByName(String name) {
        return storeRepository.getStoreList().stream()
                .filter(store -> store.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}
