package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.model.GenerateTestData;
import com.ntoutakeout.backend.repository.MenuRepository;
import com.ntoutakeout.backend.repository.ReviewRepository;
import com.ntoutakeout.backend.repository.StoreRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    private final GenerateTestData generateTestData = new GenerateTestData();

    @PostConstruct
    public void init() {
        storeRepository.deleteAll();
        menuRepository.deleteAll();
        reviewRepository.deleteAll();
        storeRepository.saveAll(generateTestData.initStores());
        menuRepository.saveAll(generateTestData.initMenu());
        reviewRepository.saveAll(generateTestData.initReview());
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
