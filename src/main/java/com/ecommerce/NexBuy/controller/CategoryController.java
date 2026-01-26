package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.entity.ProductCategory;
import com.ecommerce.NexBuy.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing product categories
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Get all categories
     * @return List of all categories
     */
    @GetMapping
    public ResponseEntity<List<ProductCategory>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    /**
     * Get a category by ID
     * @param id The category ID
     * @return The category, if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get a category by slug
     * @param slug The category slug
     * @return The category, if found
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProductCategory> getCategoryBySlug(@PathVariable String slug) {
        return categoryService.getCategoryBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all top-level categories (categories with no parent)
     * @return List of top-level categories
     */
    @GetMapping("/top-level")
    public ResponseEntity<List<ProductCategory>> getTopLevelCategories() {
        return ResponseEntity.ok(categoryService.getTopLevelCategories());
    }

    /**
     * Get all active top-level categories
     * @return List of active top-level categories
     */
    @GetMapping("/top-level/active")
    public ResponseEntity<List<ProductCategory>> getActiveTopLevelCategories() {
        return ResponseEntity.ok(categoryService.getActiveTopLevelCategories());
    }

    /**
     * Get all subcategories of a given parent category
     * @param parentId The parent category ID
     * @return List of subcategories
     */
    @GetMapping("/subcategories/{parentId}")
    public ResponseEntity<List<ProductCategory>> getSubcategories(@PathVariable Long parentId) {
        return ResponseEntity.ok(categoryService.getSubcategories(parentId));
    }

    /**
     * Get all active subcategories of a given parent category
     * @param parentId The parent category ID
     * @return List of active subcategories
     */
    @GetMapping("/subcategories/{parentId}/active")
    public ResponseEntity<List<ProductCategory>> getActiveSubcategories(@PathVariable Long parentId) {
        return ResponseEntity.ok(categoryService.getActiveSubcategories(parentId));
    }

    /**
     * Get the complete category tree
     * @return List of top-level categories with their subcategories loaded
     */
    @GetMapping("/tree")
    public ResponseEntity<List<ProductCategory>> getCategoryTree() {
        return ResponseEntity.ok(categoryService.getCategoryTree());
    }

    /**
     * Create a new category
     * @param category The category to create
     * @return The created category
     */
    @PostMapping
    public ResponseEntity<ProductCategory> createCategory(@RequestBody ProductCategory category) {
        ProductCategory createdCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    /**
     * Update an existing category
     * @param id The category ID
     * @param category The updated category data
     * @return The updated category
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductCategory> updateCategory(@PathVariable Long id, @RequestBody ProductCategory category) {
        try {
            ProductCategory updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(updatedCategory);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete a category
     * @param id The category ID
     * @return No content if successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update the display order of categories
     * @param categoryIds List of category IDs in the desired order
     * @return No content if successful
     */
    @PutMapping("/order")
    public ResponseEntity<Void> updateCategoryOrder(@RequestBody List<Long> categoryIds) {
        try {
            categoryService.updateCategoryOrder(categoryIds);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Generate a slug from a category name
     * @param request Map containing the name
     * @return The generated slug
     */
    @PostMapping("/generate-slug")
    public ResponseEntity<Map<String, String>> generateSlug(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String slug = categoryService.generateSlug(name);
        return ResponseEntity.ok(Map.of("slug", slug));
    }
}
