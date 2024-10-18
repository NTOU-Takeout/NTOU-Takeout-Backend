package com.ntoutakeout.backend.model;

import com.ntoutakeout.backend.entity.*;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateMenuTestData {
    public static List<Menu> createTestMenus() {
        List<Menu> menus = new ArrayList<>();

        // Create Menu 1: Italian Menu
        Menu italianMenu = new Menu();
        italianMenu.setId("menu001");
        italianMenu.setStoreId("store001");

        Dish pasta = createDish("dish001", "menu001", "Pasta", "Delicious Italian pasta", 14.99, "Main Course", 50);
        Dish pizza = createDish("dish002", "menu001", "Pizza", "Classic Margherita pizza", 12.99, "Main Course", 75);

        addSpiciness(pasta, true);
        addToppings(pizza, false);

        List<Pair<String, List<String>>> italianCategories = new ArrayList<>();
        italianCategories.add(Pair.of("Main Course", Arrays.asList(pasta.getId(), pizza.getId())));
        italianMenu.setCategories(italianCategories);

        menus.add(italianMenu);

        // Create Menu 2: Mexican Menu
        Menu mexicanMenu = new Menu();
        mexicanMenu.setId("menu002");
        mexicanMenu.setStoreId("store002");

        Dish burrito = createDish("dish003", "menu002", "Burrito", "Hearty bean and cheese burrito", 9.99, "Main Course", 60);
        Dish tacos = createDish("dish004", "menu002", "Tacos", "Authentic street tacos", 7.99, "Main Course", 80);

        addSpiciness(burrito, false);
        addMexicanToppings(tacos);

        List<Pair<String, List<String>>> mexicanCategories = new ArrayList<>();
        mexicanCategories.add(Pair.of("Main Course", Arrays.asList(burrito.getId(), tacos.getId())));
        mexicanMenu.setCategories(mexicanCategories);

        menus.add(mexicanMenu);

        // Create Menu 3: Asian Menu
        Menu asianMenu = new Menu();
        asianMenu.setId("menu003");
        asianMenu.setStoreId("store003");

        Dish sushi = createDish("dish005", "menu003", "Sushi", "Fresh assorted sushi platter", 18.99, "Main Course", 40);
        Dish ramen = createDish("dish006", "menu003", "Ramen", "Savory pork ramen", 13.99, "Main Course", 55);

        addSushiToppings(sushi);
        addRamenSpiciness(ramen);

        List<Pair<String, List<String>>> asianCategories = new ArrayList<>();
        asianCategories.add(Pair.of("Main Course", Arrays.asList(sushi.getId(), ramen.getId())));
        asianMenu.setCategories(asianCategories);

        menus.add(asianMenu);

        return menus;
    }

    public static void main(String[] args) {
        List<Menu> testMenus = createTestMenus();
        for (Menu menu : testMenus) {
            System.out.println(menu);
        }
    }

    private static Dish createDish(String id, String menuId, String name, String description, double price, String category, int salesVolume) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setMenuId(menuId);
        dish.setName(name);
        dish.setDescription(description);
        dish.setPrice(price);
        dish.setCategory(category);
        dish.setSalesVolume(salesVolume);
        return dish;
    }

    private static void addSpiciness(Dish dish, boolean includeHot) {
        DishAttribute spiciness = new DishAttribute();
        spiciness.setName("Spiciness");
        spiciness.setDescription("Choose your preferred spice level");
        spiciness.setType("single");
        spiciness.setIsRequired(true);

        spiciness.getAttributeOptions().add(new AttributeOption("Mild", 0.0, false));
        spiciness.getAttributeOptions().add(new AttributeOption("Medium", 0.5, false));
        if (includeHot) {
            spiciness.getAttributeOptions().add(new AttributeOption("Hot", 1.0, false));
        }

        dish.getDishAttributes().add(spiciness);
    }

    private static void addToppings(Dish dish, boolean isRequired) {
        DishAttribute toppings = new DishAttribute();
        toppings.setName("Toppings");
        toppings.setDescription("Select your favorite toppings");
        toppings.setType("multiple");
        toppings.setIsRequired(isRequired);

        toppings.getAttributeOptions().add(new AttributeOption("Cheese", 1.0, false));
        toppings.getAttributeOptions().add(new AttributeOption("Olives", 0.5, false));
        toppings.getAttributeOptions().add(new AttributeOption("Mushrooms", 0.75, false));

        dish.getDishAttributes().add(toppings);
    }

    private static void addMexicanToppings(Dish dish) {
        DishAttribute toppings = new DishAttribute();
        toppings.setName("Toppings");
        toppings.setDescription("Choose your toppings");
        toppings.setType("multiple");
        toppings.setIsRequired(false);

        toppings.getAttributeOptions().add(new AttributeOption("Sour Cream", 0.5, false));
        toppings.getAttributeOptions().add(new AttributeOption("Guacamole", 1.0, false));
        toppings.getAttributeOptions().add(new AttributeOption("Pico de Gallo", 0.5, false));

        dish.getDishAttributes().add(toppings);
    }

    private static void addSushiToppings(Dish dish) {
        DishAttribute toppings = new DishAttribute();
        toppings.setName("Toppings");
        toppings.setDescription("Select your sushi toppings");
        toppings.setType("multiple");
        toppings.setIsRequired(false);

        toppings.getAttributeOptions().add(new AttributeOption("Wasabi", 0.0, false));
        toppings.getAttributeOptions().add(new AttributeOption("Soy Sauce", 0.0, false));
        toppings.getAttributeOptions().add(new AttributeOption("Pickled Ginger", 0.0, false));

        dish.getDishAttributes().add(toppings);
    }

    private static void addRamenSpiciness(Dish dish) {
        DishAttribute spiciness = new DishAttribute();
        spiciness.setName("Spiciness");
        spiciness.setDescription("Choose your ramen spice level");
        spiciness.setType("single");
        spiciness.setIsRequired(true);

        spiciness.getAttributeOptions().add(new AttributeOption("None", 0.0, false));
        spiciness.getAttributeOptions().add(new AttributeOption("Mild", 0.0, false));
        spiciness.getAttributeOptions().add(new AttributeOption("Spicy", 0.5, false));
        spiciness.getAttributeOptions().add(new AttributeOption("Extra Spicy", 1.0, false));

        dish.getDishAttributes().add(spiciness);
    }
}