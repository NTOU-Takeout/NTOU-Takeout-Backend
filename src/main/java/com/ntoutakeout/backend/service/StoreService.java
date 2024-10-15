package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Menu;
import com.ntoutakeout.backend.entity.Review;
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

//    @PostConstruct
//    public void init() {
//        storeRepository.deleteAll();
//        menuRepository.deleteAll();
//        reviewRepository.deleteAll();
//        storeRepository.saveAll(generateTestData.initStores());
//        menuRepository.saveAll(generateTestData.initMenu());
//        reviewRepository.saveAll(generateTestData.initReview());
//    }

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

    public List<String> getStoresIdFilteredAndSorted(String keyword, String sortBy, String sortDir) {
        List<Store> stores;
        switch (sortBy) {
            case "name" -> stores = sortDir.equals("asc")
                    ? storeRepository.findByNameContainingOnlyIdOrderByNameAsc(keyword)
                    : storeRepository.findByNameContainingOnlyIdOrderByNameDesc(keyword);
            case "rating" -> stores = sortDir.equals("asc")
                    ? storeRepository.findByNameContainingOnlyIdOrderByRatingAsc(keyword)
                    : storeRepository.findByNameContainingOnlyIdOrderByRatingDesc(keyword);
            default -> stores = sortDir.equals("asc")
                    ? storeRepository.findByNameContainingOnlyIdOrderByNameAsc(keyword)
                    : storeRepository.findByNameContainingOnlyIdOrderByNameDesc(keyword);
        }
        return stores.stream().map(Store::getId).toList();
    }

    public List<Store> getStoreListByIds(List<String> ids) {
        return storeRepository.findAllById(ids);
    }

    public List<Review> getReviewById(String storeId){
        return reviewRepository.findByStoreId(storeId);
    }

    public List<Menu> getMenuById(String storeId){
        return  menuRepository.findByStoreId(storeId);
    }

}
