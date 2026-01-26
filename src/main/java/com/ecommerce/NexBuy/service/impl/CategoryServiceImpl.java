package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.entity.ProductCategory;
import com.ecommerce.NexBuy.repo.ProductCategoryRepository;
import com.ecommerce.NexBuy.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Implementation of the CategoryService interface
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public CategoryServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public List<ProductCategory> getAllCategories() {
        return productCategoryRepository.findAll();
    }

    @Override
    public Optional<ProductCategory> getCategoryById(Long id) {
        return productCategoryRepository.findById(id);
    }

    @Override
    public Optional<ProductCategory> getCategoryBySlug(String slug) {
        return productCategoryRepository.findBySlug(slug);
    }

    @Override
    public List<ProductCategory> getTopLevelCategories() {
        return productCategoryRepository.findByParentIsNullOrderByDisplayOrderAsc();
    }

    @Override
    public List<ProductCategory> getActiveTopLevelCategories() {
        return productCategoryRepository.findByParentIsNullAndIsActiveTrueOrderByDisplayOrderAsc();
    }

    @Override
    public List<ProductCategory> getSubcategories(Long parentId) {
        return productCategoryRepository.findByParentIdOrderByDisplayOrderAsc(parentId);
    }

    @Override
    public List<ProductCategory> getActiveSubcategories(Long parentId) {
        return productCategoryRepository.findByParentIdAndIsActiveTrueOrderByDisplayOrderAsc(parentId);
    }

    @Override
    public List<ProductCategory> getCategoryTree() {
        return productCategoryRepository.findCategoryTree();
    }

    @Override
    @Transactional
    public ProductCategory createCategory(ProductCategory category) {
        // Generate slug if not provided
        if (category.getSlug() == null || category.getSlug().isEmpty()) {
            category.setSlug(generateSlug(category.getCategoryName()));
        }
        
        // Set default values if not provided
        if (category.getIsActive() == null) {
            category.setIsActive(true);
        }
        
        if (category.getDisplayOrder() == null) {
            category.setDisplayOrder(0);
        }
        
        return productCategoryRepository.save(category);
    }

    @Override
    @Transactional
    public ProductCategory updateCategory(Long id, ProductCategory category) {
        ProductCategory existingCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        
        // Update fields
        existingCategory.setCategoryName(category.getCategoryName());
        
        // Update slug if provided, otherwise generate from name
        if (category.getSlug() != null && !category.getSlug().isEmpty()) {
            existingCategory.setSlug(category.getSlug());
        } else {
            existingCategory.setSlug(generateSlug(category.getCategoryName()));
        }
        
        existingCategory.setIconUrl(category.getIconUrl());
        existingCategory.setIsActive(category.getIsActive());
        existingCategory.setDisplayOrder(category.getDisplayOrder());
        
        // Update parent if provided
        if (category.getParent() != null) {
            // Prevent circular references
            if (category.getParent().getId().equals(id)) {
                throw new IllegalArgumentException("A category cannot be its own parent");
            }
            existingCategory.setParent(category.getParent());
        } else {
            existingCategory.setParent(null);
        }
        
        return productCategoryRepository.save(existingCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        ProductCategory category = productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        
        // Move children to parent's parent or make them top-level
        if (!category.getChildren().isEmpty()) {
            ProductCategory parent = category.getParent();
            for (ProductCategory child : category.getChildren()) {
                child.setParent(parent);
                productCategoryRepository.save(child);
            }
        }
        
        productCategoryRepository.deleteById(id);
    }

    @Override
    public String generateSlug(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        
        // Convert to lowercase
        String slug = name.toLowerCase();
        
        // Remove accents and special characters
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        slug = pattern.matcher(slug).replaceAll("");
        
        // Replace spaces and special chars with hyphens
        slug = slug.replaceAll("[^a-zA-Z0-9]", "-");
        
        // Remove consecutive hyphens
        slug = slug.replaceAll("-+", "-");
        
        // Remove leading and trailing hyphens
        slug = slug.replaceAll("^-|-$", "");
        
        return slug;
    }

    @Override
    @Transactional
    public void updateCategoryOrder(List<Long> categoryIds) {
        for (int i = 0; i < categoryIds.size(); i++) {
            Long categoryId = categoryIds.get(i);
            ProductCategory category = productCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));
            
            category.setDisplayOrder(i);
            productCategoryRepository.save(category);
        }
    }
}