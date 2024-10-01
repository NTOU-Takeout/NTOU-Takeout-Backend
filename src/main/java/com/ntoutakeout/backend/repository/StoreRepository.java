package com.ntoutakeout.backend.repository;

import com.ntoutakeout.backend.entity.Store;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class StoreRepository {
    Store a = new Store("AStore","St.AAA",4.4,30);
    Store b = new Store("BStore","St.BBB",4.2,60);
    Store c = new Store("CStore","St.CCC",4.6,70);
    Store d = new Store("DStore","St.DDD",2.9,20);
    Store e = new Store("EStore","St.EEE",1.0,14);
    private final ArrayList<Store> storeList;
    // temp data
    public StoreRepository() {
        storeList = new ArrayList<>();
        storeList.add(a);
        storeList.add(b);
        storeList.add(c);
    }
    public ArrayList<Store> getStoreList() {
        return storeList;
    }
}
