package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "product-category", collectionResourceRel = "productCategory")
@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
