package com.ordernow.backend.data;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.ordernow.backend.menu.model.entity.Dish;
import com.ordernow.backend.menu.model.entity.Menu;
import com.ordernow.backend.review.model.entity.Review;
import com.ordernow.backend.store.model.entity.Store;
import com.ordernow.backend.menu.repository.DishRepository;
import com.ordernow.backend.menu.repository.MenuRepository;
import com.ordernow.backend.review.repository.ReviewRepository;
import com.ordernow.backend.store.repository.StoreRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Slf4j
public class DataLoader {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;
    private final DishRepository dishRepository;

    private final Random random;
    private final Gson gson;

    @Autowired
    public DataLoader(StoreRepository storeRepository,
                      MenuRepository  menuRepository,
                      ReviewRepository reviewRepository,
                      DishRepository dishRepository) {
        this.storeRepository = storeRepository;
        this.menuRepository = menuRepository;
        this.reviewRepository = reviewRepository;
        this.dishRepository = dishRepository;
        this.random = new Random();
        this.gson = new Gson();
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


    public void loadDishesFromData(int index) {
        String filePath = String.format("src/main/resources/category%d.json", index);
        try (Reader reader = new FileReader(filePath)) {
            Type dishListType = new TypeToken<List<Dish>>() {}.getType();

            List<Dish> dishes = gson.fromJson(reader, dishListType);
            dishRepository.saveAll(dishes);

            System.out.println(dishes);
            System.out.println("-----------------");

        } catch (Exception e) {
            log.error(e.getMessage());
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
            log.error(e.getMessage());
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
                "已Mygo 小孩還在go",
                "非常失望！點了一杯熱紅茶，結果送來的飲料居然是溫的，還有點偏苦。服務態度冷淡，感覺他們並不在意顧客的感受。不會再來。",
                "今天買了他們的草莓奶蓋，味道偏甜了，而且奶蓋的質地太稀。排隊時間有點長，結帳效率不高，可能不會再來。",
                "飲料還行，但並沒有特別讓人驚艷。氣泡飲系列的口感有點偏淡，沒有預期的酸爽。店員態度不錯，但是店內座位不多，有點擁擠。",
                "飲料種類很多，選擇豐富，尤其是水果茶系列非常清爽。唯一的缺點是價格稍微偏高，但品質確實不錯，偶爾犒賞一下自己還是可以接受的。",
                "這家店的飲料真的是一絕！特別是他們的抹茶奶蓋，味道濃郁，奶蓋綿密，甜度也剛剛好。服務態度非常好，每次去都有賓至如歸的感覺。推薦給抹茶愛好者！",
                "很愛他們的店，每次來都有新飲品推出。特別喜歡他們的玫瑰特調茶，口味非常獨特。服務員總是面帶笑容，讓人覺得很舒心。",
                "飲料的口感算中規中矩，沒什麼特別出彩的地方。不過店內環境還不錯，適合和朋友小坐聊天。"
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
            review.setDate(LocalDateTime.now());
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
                storeRepository.save(store);

                System.out.println(store);
                System.out.println("-----------------");
            }

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
