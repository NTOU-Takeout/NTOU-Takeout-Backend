package com.ntoutakeout.backend.model;

import com.ntoutakeout.backend.entity.Dish;
import com.ntoutakeout.backend.entity.Menu;
import com.ntoutakeout.backend.entity.Review;
import com.ntoutakeout.backend.entity.Store;

import java.util.ArrayList;
import java.util.Random;

public class GenerateTestData {
    private final ArrayList<Store> stores;
    private final ArrayList<Menu> menus;
    private final ArrayList<Review> reviews;
    private final Random random;

    public GenerateTestData() {
        stores = new ArrayList<>();
        menus = new ArrayList<>();
        reviews = new ArrayList<>();
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

    public Dish generateDish(int i) {
        Dish dish = new Dish();
        dish.setName((char)('A'+i) + "dish");
        dish.setPicture(generatePictureURL());
        dish.setPrice(random.nextInt(100)+1);
        return dish;
    }

    public Menu generateMenu() {
        Menu menu = new Menu();
        int count = random.nextInt(5) + 3;
        ArrayList<Dish> dishes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            dishes.add(generateDish(i));
        }
        menu.setDishes(dishes);
        ArrayList<String> categories = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            categories.add((char)('a'+i) + "Category");
        }
        menu.setCategories(categories);
        return menu;
    }

    public Store generateStore(int i) {
        Store store = new Store();
        store.setName((char)('A'+i) + "store");
        store.setPicture(generatePictureURL());
        store.setRating(random.nextDouble(100));
        store.setAverageSpend(random.nextInt(200));
        return store;
    }

    public ArrayList<Store> initStores() {
        stores.clear();
        int count = 10;
        for (int i = 0; i < count; i++) {
            stores.add(generateStore(i));
        }
        return stores;
    }

    public ArrayList<Menu> initMenu() {
        menus.clear();
        for (Store store : stores) {
            Menu menu = generateMenu();
            menu.setStoreId(store.getId());
            menus.add(menu);
        }
        return menus;
    }

    public ArrayList<Review> initReview() {
        reviews.clear();
        for (Store store : stores) {
            int count = random.nextInt(5);
            for (int i = 0; i < count; i++) {
                Review review = new Review();
                review.setStoreId(store.getId());
                reviews.add(review);
            }
        }
        return reviews;
    }
}
