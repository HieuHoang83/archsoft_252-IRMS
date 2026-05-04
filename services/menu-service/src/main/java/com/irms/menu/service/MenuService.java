package com.irms.menu.service;

import com.irms.menu.domain.Category;
import com.irms.menu.domain.MenuItem;
import com.irms.menu.dto.MenuItemRequest;
import com.irms.menu.exception.ResourceNotFoundException;
import com.irms.menu.repository.CategoryRepository;
import com.irms.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class MenuService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public MenuItem createMenuItem(MenuItemRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        MenuItem menuItem = MenuItem.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(category)
                .preparationTime(request.getPreparationTime())
                .imageUrl(request.getImageUrl())
                .allergens(request.getAllergens() != null ? request.getAllergens() : new ArrayList<>())
                .isAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true)
                .build();

        return menuItemRepository.save(menuItem);
    }

    public MenuItem getMenuItemById(UUID id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
    }

    public List<MenuItem> getItemsByCategory(UUID categoryId) {
        return menuItemRepository.findByCategoryId(categoryId);
    }

    @Transactional
    public MenuItem updateMenuItem(UUID id, MenuItemRequest request) {
        MenuItem menuItem = getMenuItemById(id);
        
        if (request.getCategoryId() != null && !request.getCategoryId().equals(menuItem.getCategory().getId())) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
            menuItem.setCategory(category);
        }
        
        if (request.getName() != null) menuItem.setName(request.getName());
        if (request.getDescription() != null) menuItem.setDescription(request.getDescription());
        if (request.getPrice() != null) menuItem.setPrice(request.getPrice());
        if (request.getPreparationTime() != null) menuItem.setPreparationTime(request.getPreparationTime());
        if (request.getImageUrl() != null) menuItem.setImageUrl(request.getImageUrl());
        if (request.getIsAvailable() != null) menuItem.setIsAvailable(request.getIsAvailable());
        if (request.getAllergens() != null) {
            menuItem.getAllergens().clear();
            menuItem.getAllergens().addAll(request.getAllergens());
        }
        
        return menuItemRepository.save(menuItem);
    }

    @Transactional
    public void deleteMenuItem(UUID id) {
        MenuItem menuItem = getMenuItemById(id);
        // Soft delete
        menuItem.setIsAvailable(false);
        menuItemRepository.save(menuItem);
    }
}
