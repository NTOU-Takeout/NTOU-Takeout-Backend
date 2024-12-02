package com.ordernow.backend.service;

import com.ordernow.backend.entity.Dish;
import com.ordernow.backend.entity.Menu;
import com.ordernow.backend.repository.DishRepository;
import com.ordernow.backend.repository.MenuRepository;
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

    public void addDishToMenu(String menuId, Dish dish) 
            throws NoSuchElementException {
        Menu menu = getMenuById(menuId);
        dish = dishRepository.save(dish);
        
        boolean categoryFound = false;
        for (var category : menu.getCategories()) {
            if (category.getFirst().equals(dish.getCategory())) {
                category.getSecond().add(dish.getId());
                categoryFound = true;
                break;
            }
        }
        
        if (!categoryFound) {
            List<String> dishIds = new ArrayList<>();
            dishIds.add(dish.getId());
            menu.getCategories().add(Pair.of(dish.getCategory(), dishIds));
        }
        
        menuRepository.save(menu);
    }

    public void updateDishInMenu(String menuId, Dish updatedDish) 
            throws NoSuchElementException {
        Menu menu = getMenuById(menuId);
        
        Dish originalDish = dishRepository.findById(updatedDish.getId())
                .orElseThrow(() -> new NoSuchElementException("Dish not found"));
        
        if (!originalDish.getCategory().equals(updatedDish.getCategory())) {
            menu.getCategories().stream()
                    .filter(category -> category.getFirst().equals(originalDish.getCategory()))
                    .findFirst()
                    .ifPresent(category -> category.getSecond().remove(updatedDish.getId()));
            
            boolean categoryFound = false;
            for (var category : menu.getCategories()) {
                if (category.getFirst().equals(updatedDish.getCategory())) {
                    category.getSecond().add(updatedDish.getId());
                    categoryFound = true;
                    break;
                }
            }
            
            if (!categoryFound) {
                throw new IllegalArgumentException("New category not found in menu");
            }
            
            menuRepository.save(menu);
        }
        
        dishRepository.save(updatedDish);
    }

    public void deleteDishFromMenu(String menuId, String dishId) 
            throws NoSuchElementException {
        Menu menu = getMenuById(menuId);
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new NoSuchElementException("Dish not found"));

        menu.getCategories().stream()
                .filter(category -> category.getFirst().equals(dish.getCategory()))
                .findFirst()
                .ifPresent(category -> {
                    category.getSecond().remove(dishId);
                });

        menuRepository.save(menu);
        dishRepository.deleteById(dishId);
    }
}
