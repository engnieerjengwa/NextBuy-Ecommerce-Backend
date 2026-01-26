package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.entity.ProductCategory;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing product categories
 */
public interface CategoryService {
    
    /**
     * Get all categories
     * @return List of all categories
     */
    List<ProductCategory> getAllCategories();
    
    /**
     * Get a category by ID
     * @param id The category ID
     * @return The category, if found
     */
    Optional<ProductCategory> getCategoryById(Long id);
    
    /**
     * Get a category by slug
     * @param slug The category slug
     * @return The category, if found
     */
    Optional<ProductCategory> getCategoryBySlug(String slug);
    
    /**
     * Get all top-level categories (categories with no parent)
     * @return List of top-level categories
     */
    List<ProductCategory> getTopLevelCategories();
    
    /**
     * Get all active top-level categories
     * @return List of active top-level categories
     */
    List<ProductCategory> getActiveTopLevelCategories();
    
    /**
     * Get all subcategories of a given parent category
     * @param parentId The parent category ID
     * @return List of subcategories
     */
    List<ProductCategory> getSubcategories(Long parentId);
    
    /**
     * Get all active subcategories of a given parent category
     * @param parentId The parent category ID
     * @return List of active subcategories
     */
    List<ProductCategory> getActiveSubcategories(Long parentId);
    
    /**
     * Get the complete category tree
     * @return List of top-level categories with their subcategories loaded
     */
    List<ProductCategory> getCategoryTree();
    
    /**
     * Create a new category
     * @param category The category to create
     * @return The created category
     */
    ProductCategory createCategory(ProductCategory category);
    
    /**
     * Update an existing category
     * @param id The category ID
     * @param category The updated category data
     * @return The updated category
     */
    ProductCategory updateCategory(Long id, ProductCategory category);
    
    /**
     * Delete a category
     * @param id The category ID
     */
    void deleteCategory(Long id);
    
    /**
     * Generate a slug from a category name
     * @param name The category name
     * @return A URL-friendly slug
     */
    String generateSlug(String name);
    
    /**
     * Update the display order of categories
     * @param categoryIds List of category IDs in the desired order
     */
    void updateCategoryOrder(List<Long> categoryIds);
}