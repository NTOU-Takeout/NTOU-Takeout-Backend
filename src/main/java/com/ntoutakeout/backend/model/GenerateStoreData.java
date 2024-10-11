package com.ntoutakeout.backend.model;

import com.ntoutakeout.backend.entity.Dish;
import com.ntoutakeout.backend.entity.Store;

import java.util.ArrayList;
import java.util.Random;

public class GenerateStoreData {
    private ArrayList<Store> stores;
    private Random random;

    public GenerateStoreData() {
        stores = new ArrayList<>();
        random = new Random();
    }

    public String generateString() {
        int count = random.nextInt(10) + 3;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append((char) (random.nextInt(26) + 'a'));
        }
        return builder.toString();
    }

    public String generatePictureURL() {
        String seed = generateString();
        return "https://picsum.photos/seed/"+seed+"/200/300";
    }

    public Dish generateDish() {
        Dish dish = new Dish();
        dish.setName(generateString()+"Name");
        dish.setPicture(generatePictureURL());
        dish.setPrice(random.nextInt(100)+1);
        return dish;
    }

    public Store generateStore() {
        Store store = new Store();
        store.setName(generateString());
        store.setPicture(generatePictureURL());
        return store;
    }

    public void init() {
        stores.clear();
        int count = random.nextInt(5) + 3;
        for (int i = 0; i < count; i++) {
            stores.add(generateStore());
        }
    }

    public ArrayList<Store> getStores() {
        return stores;
    }
}
