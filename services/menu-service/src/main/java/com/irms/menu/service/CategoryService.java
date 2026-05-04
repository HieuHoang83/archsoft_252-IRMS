package com.irms.menu.service;

import com.irms.menu.domain.Category;
import com.irms.menu.exception.ResourceNotFoundException;
import com.irms.menu.repository.CategoryRepository;
import com.irms.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category already exists: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    public Category getCategoryById(java.util.UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Transactional
    public Category updateCategory(java.util.UUID id, Category request) {
        Category category = getCategoryById(id);
        
        if (!category.getName().equals(request.getName()) && categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category name already exists: " + request.getName());
        }
        
        category.setName(request.getName());
        if (request.getDisplayOrder() != null) {
            category.setDisplayOrder(request.getDisplayOrder());
        }
        
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(java.util.UUID id) {
        Category category = getCategoryById(id);
        
        if (!menuItemRepository.findByCategoryId(id).isEmpty()) {
            throw new RuntimeException("Cannot delete category. It contains active menu items.");
        }
        
        categoryRepository.delete(category);
    }
}
