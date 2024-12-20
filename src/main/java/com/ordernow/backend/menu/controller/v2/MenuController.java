package com.ordernow.backend.menu.controller.v2;

import com.ordernow.backend.common.dto.ApiResponse;
import com.ordernow.backend.common.exception.RequestValidationException;
import com.ordernow.backend.common.validation.RequestValidator;
import com.ordernow.backend.menu.model.entity.Dish;
import com.ordernow.backend.menu.model.entity.Menu;
import com.ordernow.backend.menu.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<ApiResponse<Menu>> getMenuById(
            @PathVariable String menuId)
            throws NoSuchElementException {

        Menu menu = menuService.getMenuById(menuId);
        ApiResponse<Menu> apiResponse = ApiResponse.success(menu);
        log.info("Get menu successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{menuId}/dishes")
    public ResponseEntity<ApiResponse<List<Dish>>> getDishesByCategory(
            @PathVariable String menuId,
            @RequestParam(value = "category") String category)
            throws NoSuchElementException {

        List<Dish> dishes = menuService.getCategoryDishesByMenuId(menuId, category);
        ApiResponse<List<Dish>> apiResponse = ApiResponse.success(dishes);
        log.info("Get dishes successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{menuId}/dish/create")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<ApiResponse<String>> addDishToMenu(
            @PathVariable String menuId) {

        String dishId = menuService.createDishInMenu(menuId);
        ApiResponse<String> apiResponse = ApiResponse.success(dishId);
        log.info("Add dish to menu successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/{menuId}/dish/{dishId}")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<ApiResponse<Void>> updateDishInMenu(
            @PathVariable String menuId,
            @PathVariable String dishId,
            @RequestBody Dish dish)
            throws RequestValidationException {

        RequestValidator.validateRequest(dish);
        menuService.updateDishInMenu(menuId, dishId, dish);
        ApiResponse<Void> apiResponse = ApiResponse.success(null);
        log.info("Update dish in menu successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{menuId}/dish/{dishId}")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<ApiResponse<Void>> deleteDishFromMenu(
            @PathVariable String menuId,
            @PathVariable String dishId) {
        
        menuService.deleteDishFromMenu(menuId,dishId);
        ApiResponse<Void> apiResponse = ApiResponse.success(null);
        log.info("Delete dish from menu successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
