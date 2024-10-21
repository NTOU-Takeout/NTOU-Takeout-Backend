package com.ntoutakeout.backend.model;

import com.ntoutakeout.backend.entity.Dish;
import com.ntoutakeout.backend.entity.Menu;
import com.ntoutakeout.backend.entity.Review;
import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.repository.DishRepository;
import com.ntoutakeout.backend.repository.MenuRepository;
import com.ntoutakeout.backend.repository.ReviewRepository;
import com.ntoutakeout.backend.repository.StoreRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import com.google.gson.Gson;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateNewTestData {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private DishRepository dishRepository;

    private final Random random = new Random();

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

    public List<Dish> generateDishes() {
        List<Dish> dishes = new ArrayList<>();
        int count = random.nextInt(5) + 3;
        for (int i = 0; i < count; i++) {
            Dish dish = new Dish();
            dish.setName((char)('A'+i) + "dish");
            dish.setPicture(generatePictureURL());
            dish.setPrice((double)random.nextInt(100)+1);
            dishRepository.save(dish);
            dishes.add(dish);
        }
        return dishes;
    }

    public Pair<String, List<String>> generateCategory(int i, List<Dish> dishes) {
        List<String> dishIds = new ArrayList<>();
        for (Dish dish : dishes) {
            dishIds.add(dish.getId());
        }
        return Pair.of((char) ('A' + i) + "category", dishIds);
    }

    public Menu generateMenu() {
        Menu menu = new Menu();
        int count = random.nextInt(5) + 2;
        List<Pair<String, List<String>>> categories = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            List<Dish> dishes = generateDishes();
            categories.add(generateCategory(i, dishes));
        }
        menu.setCategories(categories);
        menuRepository.save(menu);
        return menu;
    }

    public List<Review> generateReviews() {
        List<Review> reviews = new ArrayList<>();
        int count = random.nextInt(5) + 3;
        for (int i = 0; i < count; i++) {
            Review review = new Review();
            review.setAverageSpend((double)random.nextInt(100)+1);
            review.setComment((char)('A'+i) + "review");
            review.setRating((double)random.nextInt(4)+1);
            reviews.add(review);
            reviewRepository.save(review);
        }
        return reviews;
    }

    public void generateNewStore() {

        Gson gson = new Gson();
        try (Reader reader = new FileReader("src/main/resources/static/stores_data_3.json")) {

            // 解析 JSON 文件
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            // 遍歷每個餐廳的 JSON 對象
            for (JsonElement jsonElement : jsonArray) {
                JsonObject restaurant = jsonElement.getAsJsonObject();

                // 提取屬性
                String name = restaurant.get("name").getAsString();
                String description = restaurant.get("description").getAsString();
                String address = restaurant.get("address").getAsString();
                String picture = restaurant.get("picture").getAsString();

                // 輸出每個餐廳的信息
//                System.out.println("Name: " + name);
//                System.out.println("Description: " + description);
//                System.out.println("Address: " + address);
//                System.out.println("Picture: " + picture);
//                System.out.println("---------------");


                Store store = new Store();
                store.setName(name);
                store.setPicture(picture);
                store.setRating(random.nextInt(50)/10.0);
                store.setAverageSpend((double)random.nextInt(200));
                store.setDescription(description);
                store.setAddress(address);
                store.setPhoneNumber("1234567890");
                store.setMenuId(generateMenu().getId());
                List<String> ids = new ArrayList<>();
                for (Review review : generateReviews()) {
                    ids.add(review.getId());
                }
                store.setReviewIdList(ids);
                storeRepository.save(store);


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    @PostConstruct
    public void init() {
        generateNewStore();
    }
//    @PostConstruct
//    public void init() {
//        storeRepository.deleteAll();
//        menuRepository.deleteAll();
//        reviewRepository.deleteAll();
//        dishRepository.deleteAll();
//        int count = random.nextInt(10) + 3;
//        for (int i = 0; i < count; i++) {
//            generateStore(i);
//        }
//    }
}
