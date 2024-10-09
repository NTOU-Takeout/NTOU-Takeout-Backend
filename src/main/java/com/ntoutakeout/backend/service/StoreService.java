package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.model.DataList;
import com.ntoutakeout.backend.repository.StoreRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;
    private final DataList dataList = new DataList();

    @PostConstruct
    public void init() {
        dataList.init();
        storeRepository.deleteAll();
        storeRepository.saveAll(dataList.getStores());
    }

    public List<Store> getStoresFilteredAndSorted(String keyword, String sortBy, String sortDir) {
        return switch (sortBy) {
            case "name" -> sortDir.equals("asc")
                    ? storeRepository.findByNameContainingOrderByNameAsc(keyword)
                    : storeRepository.findByNameContainingOrderByNameDesc(keyword);
            case "rank" -> sortDir.equals("asc")
                    ? storeRepository.findByNameContainingOrderByRankAsc(keyword)
                    : storeRepository.findByNameContainingOrderByRankDesc(keyword);
            default -> sortDir.equals("asc")
                    ? storeRepository.findByNameContainingOrderByNameAsc(keyword)
                    : storeRepository.findByNameContainingOrderByNameDesc(keyword);
        };
    }
}
