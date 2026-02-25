package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource(path = "products")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Page<Product> findByCategoryId(@Param("id") Long id, Pageable pageable);

    Page<Product> findByNameContaining(@Param("name") String name, Pageable pageable);

    @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.brand IS NOT NULL ORDER BY p.brand")
    List<String> findDistinctBrands();

    @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.category.id = :categoryId AND p.brand IS NOT NULL ORDER BY p.brand")
    List<String> findDistinctBrandsByCategoryId(@Param("categoryId") Long categoryId);

    Page<Product> findByIsNewTrue(Pageable pageable);

    Page<Product> findByAverageRatingGreaterThanEqual(java.math.BigDecimal rating, Pageable pageable);
}
