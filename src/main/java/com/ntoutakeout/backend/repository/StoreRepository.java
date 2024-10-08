package com.ntoutakeout.backend.repository;

import com.ntoutakeout.backend.entity.Dish;
import com.ntoutakeout.backend.entity.Store;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StoreRepository {
    // Creating fake stores
    Store a = new Store("testida","AStore","St.AAA",4.4,30);
    Store b = new Store("testidb","BStore","St.BBB",4.2,60);
    Store c = new Store("testidc","CStore","St.CCC",4.6,70);
    Store d = new Store("testidd","DStore","St.DDD",2.9,20);
    Store e = new Store("testide","EStore","St.EEE",1.0,14);
    Store f = new Store("testidf","ABreakfast","St.AAA",2.9,20);
    Store g = new Store("testidg","BBreakfast","St.BBB",1.0,14);

    private final ArrayList<Store> storeList;
    private final ArrayList<Dish> dishListA;
    private final ArrayList<Dish> dishListB;
    private final ArrayList<Dish> dishListC;
    private final ArrayList<Dish> dishListD;
    private final ArrayList<Dish> dishListE;
    private final ArrayList<Dish> dishListF;
    private final ArrayList<Dish> dishListG;

    // temp data for stores and dishes
    public StoreRepository() {
        // Initialize store list
        storeList = new ArrayList<>();
        storeList.add(a);
        storeList.add(b);
        storeList.add(c);
        storeList.add(d);
        storeList.add(e);
        storeList.add(f);
        storeList.add(g);

        // Initialize dish lists for each store
        dishListA = new ArrayList<>();
        dishListA.add(new Dish("Pasta", 1299));  // Prices in cents for better accuracy
        dishListA.add(new Dish("Pizza", 899));

        dishListB = new ArrayList<>();
        dishListB.add(new Dish("Salad", 699));
        dishListB.add(new Dish("Burger", 599));

        dishListC = new ArrayList<>();
        dishListC.add(new Dish("Sushi", 1099));
        dishListC.add(new Dish("Ramen", 799));

        dishListD = new ArrayList<>();
        dishListD.add(new Dish("Steak", 1599));
        dishListD.add(new Dish("Fries", 299));

        dishListE = new ArrayList<>();
        dishListE.add(new Dish("Noodles", 599));
        dishListE.add(new Dish("Dumplings", 399));

        dishListF = new ArrayList<>();
        dishListF.add(new Dish("Pancakes", 399));
        dishListF.add(new Dish("Coffee", 199));

        dishListG = new ArrayList<>();
        dishListG.add(new Dish("Waffles", 499));
        dishListG.add(new Dish("Orange Juice", 299));
    }

    public ArrayList<Store> getStoreList() {
        return storeList;
    }

    // Method to find dishes by store ID
    public List<Dish> findDishesByStoreId(String storeId) {
        switch (storeId) {
            case "testida":
                return dishListA;
            case "testidb":
                return dishListB;
            case "testidc":
                return dishListC;
            case "testidd":
                return dishListD;
            case "testide":
                return dishListE;
            case "testidf":
                return dishListF;
            case "testidg":
                return dishListG;
            default:
                return new ArrayList<>();
        }
    }
}
