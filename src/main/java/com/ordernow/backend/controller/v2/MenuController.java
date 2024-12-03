package com.ordernow.backend.controller.v2;

import com.ordernow.backend.dto.ApiResponse;
import com.ordernow.backend.entity.Dish;
import com.ordernow.backend.entity.Menu;
import com.ordernow.backend.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController("MenuControllerV2")
@RequestMapping("/api/v2/menu")
@Slf4j
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResponse<Menu>> getMenuByStoreId(
            @PathVariable String menuId)
            throws NoSuchElementException {

        Menu menu = menuService.getMenuById(menuId);
        ApiResponse<Menu> apiResponse = ApiResponse.success(menu);
        log.info("Get menu successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/dishes")
    public ResponseEntity<ApiResponse<List<Dish>>> getDishesByCategory(
            @RequestParam(value = "category") String category) {

        List<Dish> dishes = menuService.getDishesByCategory(category);
        ApiResponse<List<Dish>> apiResponse = ApiResponse.success(dishes);
        log.info("Get dishes successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
