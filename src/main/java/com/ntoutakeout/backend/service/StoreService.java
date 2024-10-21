package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    public List<String> getStoresIdFilteredAndSorted(String keyword, String sortBy, String sortDir) {
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

    public List<Store> getStoreByIds(List<String> ids) {
        return ids.stream()
                .map(storeRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
