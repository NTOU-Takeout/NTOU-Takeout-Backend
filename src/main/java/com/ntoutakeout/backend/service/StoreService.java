package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    public List<Store> getStoresSortedByRating() {
        List<Store> storeList = storeRepository.getStoreList();
        storeList.sort(Comparator.comparingDouble(Store::getRank).reversed()); // Sort descending by rating
        return storeList;
    }

    public List<Store> getStoresSortedByTime() {
        List<Store> storeList = storeRepository.getStoreList();
        storeList.sort(Comparator.comparing(Store::getAddress)); // Sort by time in ascending order
        return storeList;
    }
}
