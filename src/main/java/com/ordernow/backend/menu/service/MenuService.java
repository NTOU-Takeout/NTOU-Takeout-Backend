package com.ordernow.backend.menu.service;

import com.ordernow.backend.menu.model.entity.Dish;
import com.ordernow.backend.menu.model.entity.Menu;
import com.ordernow.backend.menu.repository.DishRepository;
import com.ordernow.backend.menu.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;
import org.springframework.data.util.Pair;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository, DishRepository dishRepository) {
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
    }

    public Menu getMenuById(String id)
            throws NoSuchElementException {
        return menuRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Menu not found"));
    }

    public Dish getDishById(String id)
        throws NoSuchElementException {
        return dishRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Dish not found"));
    }

    public void addDishToCategory(Menu menu, Dish dish) {
        for (var category : menu.getCategories()) {
            if (category.getFirst().equals(dish.getCategory())) {
                category.getSecond().add(dish.getId());
                return;
            }
        }

        List<String> dishIds = new ArrayList<>();
        dishIds.add(dish.getId());
        menu.getCategories().add(Pair.of(dish.getCategory(), dishIds));
    }

    public List<Dish> getDishesByIds(List<String> ids) {
        return ids.stream()
                .map(dishRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<Dish> getDishesByCategory(String category) {
        return dishRepository.findAllByCategory(category);
    }

    public void changeDishesOrder(){
    }

    public String createDishInMenu(String menuId) {

        Dish dish = Dish.createDefaultDish();
        dishRepository.save(dish);

        Menu menu = getMenuById(menuId);
        addDishToCategory(menu, dish);
        menuRepository.save(menu);
        return dish.getId();
    }

    public void updateDishInMenu(String menuId, String dishId, Dish updatedDish)
            throws NoSuchElementException {
        Menu menu = getMenuById(menuId);
        Dish originalDish = getDishById(dishId);
        
        updatedDish.setId(originalDish.getId());
        
        if (!originalDish.getCategory().equals(updatedDish.getCategory())) {
            menu.getCategories().stream()
                    .filter(category -> category.getFirst().equals(originalDish.getCategory()))
                    .findFirst()
                    .ifPresent(category -> category.getSecond().remove(dishId));

            addDishToCategory(menu, updatedDish);
            menuRepository.save(menu);
        }
        
        dishRepository.save(updatedDish);
    }

    public void deleteDishFromMenu(String menuId, String dishId)
            throws NoSuchElementException {
        Menu menu = getMenuById(menuId);
        Dish dish = getDishById(dishId);

        menu.getCategories().stream()
                .filter(category -> category.getFirst().equals(dish.getCategory()))
                .findFirst()
                .ifPresent(category -> {
                    category.getSecond().remove(dishId);

                    if (category.getSecond().isEmpty()) {
                        menu.getCategories().remove(category);
                    }
                });

        menuRepository.save(menu);
        dishRepository.deleteById(dishId);
    }

    public String createAndSaveMenu() {
        Menu menu = Menu.createDefaultMenu();
        return menuRepository.save(menu).getId();
    }
}
