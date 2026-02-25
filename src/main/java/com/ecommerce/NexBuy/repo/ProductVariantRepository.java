package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource(path = "product-variants")
@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    List<ProductVariant> findByProductIdAndIsActiveTrue(Long productId);

    List<ProductVariant> findByProductId(Long productId);

    List<ProductVariant> findByProductIdAndVariantType(Long productId, String variantType);
}
