package com.ntoutakeout.backend.repository;

import com.ntoutakeout.backend.entity.Store;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class StoreRepository {
    private final ArrayList<Store> storeList;

    public StoreRepository() {
        storeList = new ArrayList<>();
    }

    public ArrayList<Store> getStoreList() {
        return storeList;
    }
}
