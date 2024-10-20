package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.entity.Dish;
import com.ntoutakeout.backend.entity.Menu;
import com.ntoutakeout.backend.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenuById(@PathVariable String id) {
        Menu menu = menuService.getMenuById(id);

        return menu == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(menu);
    }

    @PostMapping("/getDishesByIds")
    public ResponseEntity<List<Dish>> getDishesByIds(@RequestBody List<String> ids) {
        List<Dish> dishes = menuService.getDishesByIds(ids);
        return ResponseEntity.ok(dishes);
    }
}
