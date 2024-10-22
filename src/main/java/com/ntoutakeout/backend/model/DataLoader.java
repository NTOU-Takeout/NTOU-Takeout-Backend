package com.ntoutakeout.backend.model;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
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
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

@Component
public class DataLoader {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private DishRepository dishRepository;

    private final Random random = new Random();
    private final Gson gson = new Gson();

    public void loadDishesFromData(int index) {
        String filePath = String.format("src/main/resources/category%d.json", index);
        try (Reader reader = new FileReader(filePath)) {
            Type dishListType = new TypeToken<List<Dish>>() {}.getType();

            List<Dish> dishes = gson.fromJson(reader, dishListType);
            dishRepository.saveAll(dishes);

            System.out.println(dishes);
            System.out.println("-----------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMenuFromData() {
        String filePath = "src/main/resources/menu.json";
        try (Reader reader = new FileReader(filePath)) {
            Type menuType = new TypeToken<Menu>() {}.getType();

            Menu menu = gson.fromJson(reader, menuType);
            System.out.println(menu);
            menuRepository.save(menu);

            System.out.println(menu);
            System.out.println("-----------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> generateReviews() {
        List<String> comments = Arrays.asList(
                "已購買，小孩很愛吃",
                "已購買，小孩不愛吃",
                "已購買，小孩不喜歡",
                "已購買，小孩很喜歡",
                "怎麼可以這麼好吃!",
                "已購買，下次不會買",
                "狗都不吃",
                "已回購，小孩很喜歡",
                "比我媽煮的還好吃",
                "不吃粗的好粗",
                "已Mygo 小孩還在go"
        );
        List<String> names = Arrays.asList(
                "John",
                "Emma",
                "Michael",
                "Olivia",
                "James",
                "Sophia",
                "David",
                "Isabella",
                "Daniel",
                "Emily"
        );

        List<String> reviewsId = new ArrayList<>();
        int count = random.nextInt(20) + 20;

        for (int i = 0; i < count; i++) {
            Review review = new Review();
            int commentId = random.nextInt(comments.size());
            String comment = comments.get(commentId);
            int nameId = random.nextInt(names.size());
            String name = names.get(nameId);
            review.setAverageSpend((double)random.nextInt(100)+20);
            review.setComment(comment);
            review.setUserName(name);
            review.setUserId(String.format("user%d", nameId));
            review.setRating((double)random.nextInt(4)+1);
            review.setDate(new Date());
            reviewRepository.save(review);
            reviewsId.add(review.getId());
        }
        return reviewsId;
    }

    public void loadStoreFromData() {
        String filePath = "src/main/resources/stores.json";
        try (Reader reader = new FileReader(filePath)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement jsonElement : jsonArray) {
                JsonObject restaurant = jsonElement.getAsJsonObject();

                String name = restaurant.get("name").getAsString();
                String description = restaurant.get("description").getAsString();
                String address = restaurant.get("address").getAsString();
                String picture = restaurant.get("picture").getAsString();

                Store store = new Store();
                store.setName(name);
                store.setPicture(picture);
                store.setRating(random.nextInt(50)/10.0);
                store.setAverageSpend((double)random.nextInt(100)+30);
                store.setDescription(description);
                store.setAddress(address);
                store.setPhoneNumber("1234567890");
                store.setMenuId("menu001");
                store.setReviewIdList(generateReviews());
                store.setBusinessHours();
                storeRepository.save(store);

                System.out.println(store);
                System.out.println("-----------------");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void init() {
//        reviewRepository.deleteAll();
//        storeRepository.deleteAll();
//        loadStoreFromData();

//        dishRepository.deleteAll();
//        menuRepository.deleteAll();
//        for (int i=1; i<=6; i++)
//            loadDishesFromData(i);
//        loadMenuFromData();
    }
}
