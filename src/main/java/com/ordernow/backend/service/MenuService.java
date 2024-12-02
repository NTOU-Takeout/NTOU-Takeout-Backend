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
}
