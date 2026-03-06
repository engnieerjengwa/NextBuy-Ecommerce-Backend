package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.entity.ProductCategory;
import com.ecommerce.NexBuy.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing product categories
 */
@Tag(name = "Categories", description = "Product category hierarchy and management")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<List<ProductCategory>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get category by slug")
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProductCategory> getCategoryBySlug(@PathVariable String slug) {
        return categoryService.getCategoryBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get top-level categories")
    @GetMapping("/top-level")
    public ResponseEntity<List<ProductCategory>> getTopLevelCategories() {
        return ResponseEntity.ok(categoryService.getTopLevelCategories());
    }

    @Operation(summary = "Get active top-level categories")
    @GetMapping("/top-level/active")
    public ResponseEntity<List<ProductCategory>> getActiveTopLevelCategories() {
        return ResponseEntity.ok(categoryService.getActiveTopLevelCategories());
    }

    @Operation(summary = "Get subcategories")
    @GetMapping("/subcategories/{parentId}")
    public ResponseEntity<List<ProductCategory>> getSubcategories(@PathVariable Long parentId) {
        return ResponseEntity.ok(categoryService.getSubcategories(parentId));
    }

    @Operation(summary = "Get active subcategories")
    @GetMapping("/subcategories/{parentId}/active")
    public ResponseEntity<List<ProductCategory>> getActiveSubcategories(@PathVariable Long parentId) {
        return ResponseEntity.ok(categoryService.getActiveSubcategories(parentId));
    }

    @Operation(summary = "Get category tree", description = "Retrieve the full category hierarchy")
    @GetMapping("/tree")
    public ResponseEntity<List<ProductCategory>> getCategoryTree() {
        return ResponseEntity.ok(categoryService.getCategoryTree());
    }

    @Operation(summary = "Create category")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductCategory> createCategory(@RequestBody ProductCategory category) {
        ProductCategory createdCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @Operation(summary = "Update category")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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

    @Operation(summary = "Delete category")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update category display order")
    @PutMapping("/order")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateCategoryOrder(@RequestBody List<Long> categoryIds) {
        try {
            categoryService.updateCategoryOrder(categoryIds);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Generate slug from name")
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
