package com.irms.menu.controller;

import com.irms.menu.domain.MenuItem;
import com.irms.menu.dto.MenuItemRequest;
import com.irms.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/items")
    public ResponseEntity<List<MenuItem>> getAllItems() {
        return ResponseEntity.ok(menuService.getAllMenuItems());
    }

    @PostMapping("/items")
    public ResponseEntity<MenuItem> createItem(@RequestBody MenuItemRequest request) {
        return new ResponseEntity<>(menuService.createMenuItem(request), HttpStatus.CREATED);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<MenuItem> getItemById(@PathVariable UUID id) {
        return ResponseEntity.ok(menuService.getMenuItemById(id));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<MenuItem> updateItem(@PathVariable UUID id, @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(menuService.updateMenuItem(id, request));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID id) {
        menuService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories/{categoryId}/items")
    public ResponseEntity<List<MenuItem>> getItemsByCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(menuService.getItemsByCategory(categoryId));
    }
}
