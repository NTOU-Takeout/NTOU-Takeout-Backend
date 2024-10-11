package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Menu;
import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.model.GenerateStoreData;
import com.ntoutakeout.backend.repository.StoreRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;
    private final GenerateStoreData generateStoreData = new GenerateStoreData();

    @PostConstruct
    public void init() {
        generateStoreData.init();
        storeRepository.deleteAll();
        storeRepository.saveAll(generateStoreData.getStores());
    }

    public List<Store> getStoresFilteredAndSorted(String keyword, String sortBy, String sortDir) {
        return switch (sortBy) {
            case "name" -> sortDir.equals("asc")
                    ? storeRepository.findByNameContainingOrderByNameAsc(keyword)
                    : storeRepository.findByNameContainingOrderByNameDesc(keyword);
            case "rating" -> sortDir.equals("asc")
                    ? storeRepository.findByNameContainingOrderByRatingAsc(keyword)
                    : storeRepository.findByNameContainingOrderByRatingDesc(keyword);
            default -> sortDir.equals("asc")
                    ? storeRepository.findByNameContainingOrderByNameAsc(keyword)
                    : storeRepository.findByNameContainingOrderByNameDesc(keyword);
        };
    }

//    public Optional<Menu> getMenuById(String id) {
//        Optional<Store> store = storeRepository.findById(id);
//        if (store.isPresent()) {
//            Store storeObj = store.get();
//            return Optional.ofNullable(storeObj.getMenu());
//        }
//        return Optional.empty();
//    }
}
