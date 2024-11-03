package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.entity.Dish;
import com.ntoutakeout.backend.entity.Menu;
import com.ntoutakeout.backend.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@Slf4j
public class MenuController {
    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenuById(@PathVariable String id) {
        log.info("Fetch API: getMenuById Success");
        Menu menu = menuService.getMenuById(id);

        return menu == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(menu);
    }

    @PostMapping("/getDishesByIds")
    public ResponseEntity<List<Dish>> getDishesByIds(@RequestBody List<String> ids) {
        log.info("Fetch API: getDishesByIds Success");
        List<Dish> dishes = menuService.getDishesByIds(ids);
        return ResponseEntity.ok(dishes);
    }
}
