package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Dish;
import com.ntoutakeout.backend.entity.Menu;
import com.ntoutakeout.backend.repository.DishRepository;
import com.ntoutakeout.backend.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private DishRepository dishRepository;

    public Menu getMenuById(String id) {
        return menuRepository.findById(id).orElse(null);
    }

    public List<Dish> getDishesByIds(List<String> ids) {
        return ids.stream()
                .map(dishRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
