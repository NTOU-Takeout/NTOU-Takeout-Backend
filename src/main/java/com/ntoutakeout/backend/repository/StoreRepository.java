package com.ntoutakeout.backend.repository;

import com.ntoutakeout.backend.entity.Store;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends MongoRepository<Store, String> {
    List<Store> findByNameContainingOrderByNameAsc(String storeName);
    List<Store> findByNameContainingOrderByNameDesc(String storeName);
    List<Store> findByNameContainingOrderByRatingAsc(String storeName);
    List<Store> findByNameContainingOrderByRatingDesc(String storeName);
}
