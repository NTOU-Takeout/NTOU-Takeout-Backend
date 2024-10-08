package com.ntoutakeout.backend.repository;

import com.ntoutakeout.backend.entity.Dish;
import com.ntoutakeout.backend.entity.Dishes;
import com.ntoutakeout.backend.entity.Store;
import io.micrometer.common.KeyValues;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StoreRepository {
    Store a = new Store("testida","AStore","St.AAA",4.4,30)
    Store b = new Store("testidb","BStore","St.BBB",4.2,60);
    Store c = new Store("testida","CStore","St.CCC",4.6,70);
    Store d = new Store("testidc","DStore","St.DDD",2.9,20);
    Store e = new Store("testidd","EStore","St.EEE",1.0,14);
    Store f = new Store("testide","ABreakfast","St.AAA",2.9,20);
    Store g = new Store("testidf","BBreakfast","St.BBB",1.0,14);


    private final ArrayList<Store> storeList;
    private final ArrayList<Dish> dishList;
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
    public List<Dish> findDishesByStoreId(String storeId) {


        KeyValues dishList;
        return dishList.stream()
                .filter(dish -> dish.getStoreId().equals(storeId))
                .collect(Collectors.toList());
    }
}
