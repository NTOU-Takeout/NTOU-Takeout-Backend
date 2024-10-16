package com.ntoutakeout.backend.repository;

import com.ntoutakeout.backend.entity.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends MongoRepository<Menu, String> {
    List<Menu> findByStoreId(String storeId);
}
