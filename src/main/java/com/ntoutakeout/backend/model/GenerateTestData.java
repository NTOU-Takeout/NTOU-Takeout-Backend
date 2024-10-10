package com.ntoutakeout.backend.model;

import com.ntoutakeout.backend.entity.Dish;
import com.ntoutakeout.backend.entity.Menu;
import com.ntoutakeout.backend.entity.Store;

import java.util.ArrayList;
import java.util.Random;

public class GenerateTestData {
    private ArrayList<Store> stores;
    private Random random;

    public GenerateTestData() {
        stores = new ArrayList<>();
        random = new Random();
    }

    public String getString() {
        int count = random.nextInt(10) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append((char) (random.nextInt(26) + 'a'));
        }
        return builder.toString();
    }

    public Dish GenerateDish() {
        int price = random.nextInt(100) + 1;
        String name = getString() + "Name";
        return new Dish(name, price);
    }

    public Store GenerateStore() {
        String name = getString() + "Name";
        String address = getString() + "Address";
        double rank = random.nextDouble(100);
        double averagePrice = random.nextDouble(200);
        Menu menu = new Menu();

        int count = random.nextInt(10) + 1;
        for (int i = 0; i < count; i++) {
            menu.addDish(GenerateDish());
        }

        return new Store(name, address, rank, averagePrice, menu);
    }

    public void init() {
        stores.clear();
        int count = random.nextInt(10) + 3;
        for (int i = 0; i < count; i++) {
            stores.add(GenerateStore());
        }
    }

    public ArrayList<Store> getStores() {
        return stores;
    }
}
