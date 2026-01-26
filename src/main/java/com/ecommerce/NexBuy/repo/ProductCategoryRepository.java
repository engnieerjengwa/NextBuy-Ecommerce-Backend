package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "product-category", collectionResourceRel = "productCategory")
@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    /**
     * Find all top-level categories (categories with no parent)
     * @return List of top-level categories
     */
    List<ProductCategory> findByParentIsNullOrderByDisplayOrderAsc();

    /**
     * Find all subcategories of a given parent category
     * @param parent The parent category
     * @return List of subcategories
     */
    List<ProductCategory> findByParentOrderByDisplayOrderAsc(ProductCategory parent);

    /**
     * Find all subcategories of a given parent category ID
     * @param parentId The parent category ID
     * @return List of subcategories
     */
    List<ProductCategory> findByParentIdOrderByDisplayOrderAsc(Long parentId);

    /**
     * Find a category by its slug
     * @param slug The category slug
     * @return The category, if found
     */
    Optional<ProductCategory> findBySlug(String slug);

    /**
     * Find all active categories
     * @return List of active categories
     */
    List<ProductCategory> findByIsActiveTrueOrderByDisplayOrderAsc();

    /**
     * Find all active subcategories of a given parent category
     * @param parentId The parent category ID
     * @return List of active subcategories
     */
    List<ProductCategory> findByParentIdAndIsActiveTrueOrderByDisplayOrderAsc(Long parentId);

    /**
     * Find all active top-level categories
     * @return List of active top-level categories
     */
    List<ProductCategory> findByParentIsNullAndIsActiveTrueOrderByDisplayOrderAsc();

    /**
     * Get the complete category tree
     * @return List of all categories with their relationships
     */
    @Query("SELECT c FROM ProductCategory c LEFT JOIN FETCH c.children WHERE c.parent IS NULL ORDER BY c.displayOrder ASC")
    List<ProductCategory> findCategoryTree();
}
