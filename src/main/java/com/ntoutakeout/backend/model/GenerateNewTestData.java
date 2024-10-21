package com.ntoutakeout.backend.model;

import com.ntoutakeout.backend.entity.*;
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
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class GenerateNewTestData {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private  MenuRepository menuRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private  DishRepository dishRepository;

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
        List<String> samplecoments = Arrays.asList(
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


        List<Review> reviews = new ArrayList<>();
        int count = random.nextInt(5) + 3;

        for (int i = 0; i < count; i++) {
            Review review = new Review();
            review.setAverageSpend((double)random.nextInt(100)+1);
            int randomIndex = random.nextInt(samplecoments.size());
            String randomComment = samplecoments.get(randomIndex);
            review.setComment(randomComment);
            review.setRating((double)random.nextInt(4)+1);
            reviews.add(review);
            reviewRepository.save(review);
        }
        return reviews;
    }

    public Pair<LocalTime, LocalTime>[][] generateBusinessHours() {
        Pair<LocalTime, LocalTime>[][] businessHours = new Pair[7][2];
        for (int i=0; i<7; i++) {
            businessHours[i][0] = Pair.of(LocalTime.of(9, 0), LocalTime.of(12, 0));
            businessHours[i][1] = Pair.of(LocalTime.of(17, 0), LocalTime.of(20, 0));
        }
        return businessHours;
    }

    public void generateNewStore() {

        try (Reader reader = new FileReader("src/main/resources/static/stores_data_10.json")) {

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
                store.setBusinessHours(generateBusinessHours());
                storeRepository.save(store);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

//    public Menu generateMenu() {
//        List<Menu> menus = new ArrayList<>(createTestMenus());
//        int randomIndex = random.nextInt(menus.size());
//        Menu randomMenus= menus.get(randomIndex);
//        return randomMenus;
//    }

//    public static List<Menu> createTestMenus() {
//        List<Menu> menus = new ArrayList<>();
//
//        // Create Menu 1: Pizza Menu
//        Menu pizzaMenu = new Menu();
//        pizzaMenu.setId("menu001");
//
//        //https://www.ubereats.com/tw/store/pizza-hut%E5%BF%85%E5%8B%9D%E5%AE%A2-%E5%85%89%E5%BE%A9%E9%A4%90%E5%BB%B3%E5%BA%97/KfgXtlSEQWSlvakN-oP6bw?diningMode=DELIVERY&pl=JTdCJTIyYWRkcmVzcyUyMiUzQSUyMiVFNSU5RiVCQSVFOSU5QSU4NiVFNSVCOCU4MiUyMiUyQyUyMnJlZmVyZW5jZSUyMiUzQSUyMkNoSUpsWFNEdVAxTlhUUVJhR1h5UmFwcjk1NCUyMiUyQyUyMnJlZmVyZW5jZVR5cGUlMjIlM0ElMjJnb29nbGVfcGxhY2VzJTIyJTJDJTIybGF0aXR1ZGUlMjIlM0EyNS4xMjc2MDMzJTJDJTIybG9uZ2l0dWRlJTIyJTNBMTIxLjczOTE4MzMlN0Q%3D
//        Dish dish1 = createDish("dish001", "menu001", "和風章魚燒小比薩 Japanese Takoyaki Small Pizza", "購買和風章魚燒送和風章魚燒小比薩1個。活動數量有限，售完為止。｜圖片僅供參考，餐點以實際供貨為主。 餐點內容無法提供客製化服務，另外為響應環保，不提供免洗餐具、吸管。", 510.00, "小比薩買1送1 Small Pizza Buy One Get One Free", 1334);
//        Dish dish2 = createDish("dish002", "menu001", "夏威夷小比薩Hawaiian Small Pizza", "購買夏威夷送夏威夷小比薩1個。活動數量有限，售完為止。｜圖片僅供參考，餐點以實際供貨為主。 餐點內容無法提供客製化服務，另外為響應環保，不提供免洗餐具、吸管。", 510.00, "小比薩買1送1 Small Pizza Buy One Get One Free", 116);
//        Dish dish3 = createDish("dish003", "menu001", "蔬食總匯大比薩", "原價 $690｜內含 : 青花菜、蘑菇、黃瓜、玉米、番茄丁、珍珠型莫札瑞拉起司、洋香菜葉、BBQ醬", 690.00, "限時優惠大比薩$299起", 344);
//        Dish dish4 = createDish("dish004", "menu001", "雙拼大比薩-四重濃起司+四小福", "原價 $620｜內含 : 燻雞絲、豬肉、義大利香腸、培根、菠菜、番茄、洋蔥、薯金幣、特濃切達起司醬、起司片、芝心起司、莫札瑞拉起司、BBQ醬。｜本產品非素食。本產品含有麩質、牛奶、大豆類及其製品，不適合對其過敏體質者食用。", 620.00, "限時優惠大比薩$299起", 75);
//
//        addCrust(dish1, true);
//        addCrust(dish2, true);
//        addCrust(dish3, true);
//        addCrust(dish4, true);
//        dishRepository.save(dish1);
//        dishRepository.save(dish2);
//        dishRepository.save(dish3);
//        dishRepository.save(dish4);
//
//        List<Pair<String, List<String>>> sales1get1Categories = new ArrayList<>();
//        List<Pair<String, List<String>>> sales299Categories = new ArrayList<>();
//        sales1get1Categories.add(Pair.of("小比薩買1送1 Small Pizza Buy One Get One Free", Arrays.asList(dish1.getId(), dish2.getId())));
//        sales299Categories.add(Pair.of("限時優惠大比薩$299起", Arrays.asList(dish3.getId(), dish4.getId())));
//        pizzaMenu.setCategories(sales1get1Categories);
//        pizzaMenu.setCategories(sales299Categories);
//        menuRepository.save(pizzaMenu);
//        menus.add(pizzaMenu);
//
//        // Create Menu 2: TaiwanFry Menu
//        Menu taiwanFry = new Menu();
//        taiwanFry.setId("menu002");
//
//        //https://www.ubereats.com/tw/store/%E8%83%A1%E6%A4%92%E9%A6%99%E5%A1%A9%E9%85%A5%E9%9B%9E-%E5%9F%BA%E9%9A%86%E7%B2%BE%E4%B8%80%E5%BA%97/pCoHlfdWSY-rNsFOBcs8Pg?diningMode=DELIVERY
//        Dish dish5 = createDish("dish005", "menu002", "甜不辣","", 45.00, "海鮮類", 1334);
//        Dish dish6 = createDish("dish006", "menu002", "深海魷魚腳","", 75.00, "海鮮類", 432);
//        Dish dish7 = createDish("dish005", "menu002", "四季豆","", 60.00, "蔬菜類", 42);
//        Dish dish8 = createDish("dish006", "menu002", "薯條","", 50.00, "蔬菜類", 75);
//        dishRepository.save(dish1);
//        dishRepository.save(dish2);
//        dishRepository.save(dish3);
//        dishRepository.save(dish4);
//
//
//        List<Pair<String, List<String>>> seafoodCategories = new ArrayList<>();
//        List<Pair<String, List<String>>> vegesCategories = new ArrayList<>();
//        seafoodCategories.add(Pair.of("海鮮類", Arrays.asList(dish5.getId(), dish6.getId())));
//        vegesCategories.add(Pair.of("蔬菜類", Arrays.asList(dish7.getId(), dish8.getId())));
//        taiwanFry.setCategories(seafoodCategories);
//        taiwanFry.setCategories(vegesCategories);
//        menuRepository.save(taiwanFry);
//        menus.add(taiwanFry);
//
//        // Create Menu 4: Beverage Menu
//        Menu beverageMenu = new Menu();
//        beverageMenu.setId("menu003");
//
//        // https://www.ubereats.com/tw/store/%E6%B8%85%E5%BF%83%E7%A6%8F%E5%85%A8-%E4%BB%81%E5%9B%9B%E5%BA%97/ObaJA87kVhGf5KNzB4UXFw?diningMode=DELIVERY
//        Dish iceCreamMilkTea = createDish("dish009", "menu003", "冰淇淋奶茶 Ice Cream Milk Tea",
//                "大杯。總糖量: 71, 總熱量: 750。咖啡因含量: 綠 (100mg 以下)。醲醇芳香的錫蘭奶紅, 加入香甜濃郁的香草冰淇淋, 絕對爽口, 極致冰涼。",
//                75.00, "冰淇淋系列", 423);
//        Dish iceCreamBlackTea = createDish("dish010", "menu003", "冰淇淋紅茶 Ice Cream Black Tea",
//                "大杯。總糖量: 86, 總熱量: 532。咖啡因含量: 綠 (100mg 以下)。清雅厚實的錫蘭紅茶, 加入香甜濃郁的香草冰淇淋, 凸顯茶味, 加倍冰涼。",
//                60.00, "冰淇淋系列", 732);
//        Dish longanTea = createDish("dish011", "menu003", "桂圓茶 Longan Tea",
//                "大杯。甜度固定。總糖量 65 總熱量 290。台灣冬季傳統飲品, 入口濃郁醇厚, 香甜滋潤, 呈現道地桂圓 (龍眼干) 口味, 袪寒暖身。",
//                60.00, "冬季熱飲系列", 67);
//        Dish gingerMilkTea = createDish("dish012", "menu003", "薑薑奶茶 Ginger Milk Tea",
//                "大杯。甜度固定。總糖量 63 總熱量 515。醇正濃厚, 潤澤滋養的薑母茶, 溶入香醇的嚴選奶精, 口感更加圓順馥郁。",
//                75.00, "冬季熱飲系列", 24);
//
//        // Add Ice level options
//        addIceOptions(iceCreamMilkTea);
//        // Add sugar level options
//        addSugarOptions(iceCreamMilkTea);
//        // Add tea base options
//        addTeaBaseOptions(iceCreamMilkTea);
//        // Add extra toppings
//        addToppingsOptions(iceCreamMilkTea);
//
//        addIceOptions(iceCreamBlackTea);
//        addSugarOptions(iceCreamBlackTea);
//        addTeaBaseOptions(iceCreamBlackTea);
//        addToppingsOptions(iceCreamBlackTea);
//
//        addIceOptions(longanTea);
//        addSugarOptions(longanTea);
//        addTeaBaseOptions(longanTea);
//        addToppingsOptions(longanTea);
//
//        addIceOptions(gingerMilkTea);
//        addSugarOptions(gingerMilkTea);
//        addTeaBaseOptions(gingerMilkTea);
//        addToppingsOptions(gingerMilkTea);
//
//        dishRepository.save(iceCreamMilkTea);
//        dishRepository.save(iceCreamBlackTea);
//        dishRepository.save(longanTea);
//        dishRepository.save(gingerMilkTea);
//
//
//        List<Pair<String, List<String>>> beverageCategories = new ArrayList<>();
//        beverageCategories.add(Pair.of("冰淇淋系列", Arrays.asList(iceCreamMilkTea.getId(),iceCreamBlackTea.getId())));
//        beverageMenu.setCategories(beverageCategories);
//        beverageCategories.add(Pair.of("冬季熱飲系列", Arrays.asList(longanTea.getId(),gingerMilkTea.getId())));
//        beverageMenu.setCategories(beverageCategories);
//        menuRepository.save(beverageMenu);
//        menus.add(beverageMenu);
//
//        return menus;
//    }
//
//    private static Dish createDish(String id, String menuId, String name, String description, double price, String category, int salesVolume) {
//        Dish dish = new Dish();
//        dish.setId(id);
//        dish.setName(name);
//        dish.setDescription(description);
//        dish.setPrice(price);
//        dish.setCategory(category);
//        dish.setSalesVolume(salesVolume);
//        return dish;
//    }
//
//    private static void addCrust(Dish dish, boolean isRequired) {
//        DishAttribute crust = new DishAttribute();
//        crust.setName("大比薩餅皮選擇 Choose Large Pizza Crust");
//        crust.setDescription("選擇 1");
//        crust.setType("single");
//        crust.setIsRequired(isRequired);
//
//        crust.getAttributeOptions().add(new AttributeOption("鬆厚餅皮【大】", 0.0, false));
//        crust.getAttributeOptions().add(new AttributeOption("薄脆餅皮【大】", 40.0, false));
//        crust.getAttributeOptions().add(new AttributeOption("芝心餅皮【大】", 115.0, false));
//
//        dish.getDishAttributes().add(crust);
//    }
//
//    private static void addSpiciness(Dish dish, boolean isRequired) {
//        DishAttribute spiciness = new DishAttribute();
//        spiciness.setName("Spiciness");
//        spiciness.setDescription("Choose your preferred spice level");
//        spiciness.setType("single");
//        spiciness.setIsRequired(isRequired);
//
//        spiciness.getAttributeOptions().add(new AttributeOption("Mild", 0.0, false));
//        spiciness.getAttributeOptions().add(new AttributeOption("Medium", 0.5, false));
//        spiciness.getAttributeOptions().add(new AttributeOption("Hot", 1.0, false));
//
//        dish.getDishAttributes().add(spiciness);
//    }
//
//    private static void addToppings(Dish dish, boolean isRequired) {
//        DishAttribute toppings = new DishAttribute();
//        toppings.setName("Toppings");
//        toppings.setDescription("Select your favorite toppings");
//        toppings.setType("multiple");
//        toppings.setIsRequired(isRequired);
//
//        toppings.getAttributeOptions().add(new AttributeOption("Cheese", 1.0, false));
//        toppings.getAttributeOptions().add(new AttributeOption("Olives", 0.5, false));
//        toppings.getAttributeOptions().add(new AttributeOption("Mushrooms", 0.75, false));
//
//        dish.getDishAttributes().add(toppings);
//    }
//
//    private static void addMexicanToppings(Dish dish) {
//        DishAttribute toppings = new DishAttribute();
//        toppings.setName("Toppings");
//        toppings.setDescription("Choose your toppings");
//        toppings.setType("multiple");
//        toppings.setIsRequired(false);
//
//        toppings.getAttributeOptions().add(new AttributeOption("Sour Cream", 0.5, false));
//        toppings.getAttributeOptions().add(new AttributeOption("Guacamole", 1.0, false));
//        toppings.getAttributeOptions().add(new AttributeOption("Pico de Gallo", 0.5, false));
//
//        dish.getDishAttributes().add(toppings);
//    }
//
//    private static void addSushiToppings(Dish dish) {
//        DishAttribute toppings = new DishAttribute();
//        toppings.setName("Toppings");
//        toppings.setDescription("Select your sushi toppings");
//        toppings.setType("multiple");
//        toppings.setIsRequired(false);
//
//        toppings.getAttributeOptions().add(new AttributeOption("Wasabi", 0.0, false));
//        toppings.getAttributeOptions().add(new AttributeOption("Soy Sauce", 0.0, false));
//        toppings.getAttributeOptions().add(new AttributeOption("Pickled Ginger", 0.0, false));
//
//        dish.getDishAttributes().add(toppings);
//    }
//
//    private static void addRamenSpiciness(Dish dish) {
//        DishAttribute spiciness = new DishAttribute();
//        spiciness.setName("Spiciness");
//        spiciness.setDescription("Choose your ramen spice level");
//        spiciness.setType("single");
//        spiciness.setIsRequired(true);
//
//        spiciness.getAttributeOptions().add(new AttributeOption("None", 0.0, false));
//        spiciness.getAttributeOptions().add(new AttributeOption("Mild", 0.0, false));
//        spiciness.getAttributeOptions().add(new AttributeOption("Spicy", 0.5, false));
//        spiciness.getAttributeOptions().add(new AttributeOption("Extra Spicy", 1.0, false));
//
//        dish.getDishAttributes().add(spiciness);
//    }
//
//    private static void addIceOptions(Dish dish) {
//        DishAttribute iceLevel = new DishAttribute();
//        iceLevel.setName("冰熱選擇");
//        iceLevel.setDescription("選擇冰量");
//        iceLevel.setType("single");
//        iceLevel.setIsRequired(true);
//
//        iceLevel.getAttributeOptions().add(new AttributeOption("正常冰 (100%)", 0.0, false));
//        iceLevel.getAttributeOptions().add(new AttributeOption("少冰 (70%)", 0.0, false));
//        iceLevel.getAttributeOptions().add(new AttributeOption("推薦微冰 (30%)", 0.0, false));
//        iceLevel.getAttributeOptions().add(new AttributeOption("去冰 (0%)", 0.0, false));
//
//        dish.getDishAttributes().add(iceLevel);
//    }
//
//    private static void addSugarOptions(Dish dish) {
//        DishAttribute sugarLevel = new DishAttribute();
//        sugarLevel.setName("糖量選擇");
//        sugarLevel.setDescription("選擇糖量");
//        sugarLevel.setType("single");
//        sugarLevel.setIsRequired(true);
//
//        sugarLevel.getAttributeOptions().add(new AttributeOption("少糖 (8 分糖)", 0.0, false));
//        sugarLevel.getAttributeOptions().add(new AttributeOption("半糖 (5 分糖)", 0.0, false));
//        sugarLevel.getAttributeOptions().add(new AttributeOption("微糖 (2 分糖)", 0.0, false));
//        sugarLevel.getAttributeOptions().add(new AttributeOption("無糖 (不另外加糖)", 0.0, false));
//
//        dish.getDishAttributes().add(sugarLevel);
//    }
//
//    private static void addTeaBaseOptions(Dish dish) {
//        DishAttribute teaBase = new DishAttribute();
//        teaBase.setName("茶湯更換選擇");
//        teaBase.setDescription("更換茶湯");
//        teaBase.setType("single");
//        teaBase.setIsRequired(false);
//
//        teaBase.getAttributeOptions().add(new AttributeOption("特級綠茶", 0.0, false));
//        teaBase.getAttributeOptions().add(new AttributeOption("極品菁茶", 0.0, false));
//        teaBase.getAttributeOptions().add(new AttributeOption("烏龍綠茶", 0.0, false));
//        teaBase.getAttributeOptions().add(new AttributeOption("特選普洱", 0.0, false));
//
//        dish.getDishAttributes().add(teaBase);
//    }
//
//    private static void addToppingsOptions(Dish dish) {
//        DishAttribute toppings = new DishAttribute();
//        toppings.setName("加料選擇");
//        toppings.setDescription("選擇加料");
//        toppings.setType("single");
//        toppings.setIsRequired(false);
//
//        toppings.getAttributeOptions().add(new AttributeOption("珍珠", 5.0, false));
//        toppings.getAttributeOptions().add(new AttributeOption("椰果 (僅限冷飲)", 5.0, false));
//        toppings.getAttributeOptions().add(new AttributeOption("粉圓 (小珍珠)", 5.0, false));
//        toppings.getAttributeOptions().add(new AttributeOption("QQ (珍珠及椰果) (僅限冷飲)", 5.0, false));
//        toppings.getAttributeOptions().add(new AttributeOption("冰淇淋 (僅限冷飲)", 15.0, false));
//        toppings.getAttributeOptions().add(new AttributeOption("咖啡凍", 15.0, false));
//        toppings.getAttributeOptions().add(new AttributeOption("蜜桃醬 (僅限冷飲)", 30.0, true));  // Marking as popular
//
//        dish.getDishAttributes().add(toppings);
//    }
    @PostConstruct
    public void init() {
        storeRepository.deleteAll();
        reviewRepository.deleteAll();
        menuRepository.deleteAll();
        dishRepository.deleteAll();
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
