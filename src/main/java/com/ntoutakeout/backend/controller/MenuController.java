package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.entity.Menu;
import com.ntoutakeout.backend.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("{id}")
    public ResponseEntity<Menu> getMenuById(@PathVariable String id) {
        Menu menu = menuService.getMenuById(id);

        return menu == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(menu);
    }
}
