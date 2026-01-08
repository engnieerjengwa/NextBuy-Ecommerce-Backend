package com.ecommerce.NexBuy.dao;

import com.ecommerce.NexBuy.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(path = "product-category", collectionResourceRel = "productCategory")
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
