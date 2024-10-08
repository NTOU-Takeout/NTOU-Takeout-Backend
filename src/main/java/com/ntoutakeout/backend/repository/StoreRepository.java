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
    Store f = new Store("ABreakfast","St.AAA",2.9,20);
    Store g = new Store("BBreakfast","St.BBB",1.0,14);
    private final ArrayList<Store> storeList;
    // temp data
    public StoreRepository() {
        storeList = new ArrayList<>();
        storeList.add(a);
        storeList.add(b);
        storeList.add(c);
        storeList.add(d);
        storeList.add(e);
        storeList.add(f);
        storeList.add(g);
    }
    public ArrayList<Store> getStoreList() {
        return storeList;
    }
}
