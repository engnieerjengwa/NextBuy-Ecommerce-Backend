package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> buildSearchSpec(
            String query,
            String brand,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean inStock,
            BigDecimal minRating,
            Boolean isNew) {

        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Only active products
            predicates.add(criteriaBuilder.isTrue(root.get("active")));

            // Text search on name and description
            if (query != null && !query.trim().isEmpty()) {
                String searchPattern = "%" + query.trim().toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")), searchPattern);
                Predicate descriptionPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")), searchPattern);
                Predicate brandPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("brand")), searchPattern);
                predicates.add(criteriaBuilder.or(namePredicate, descriptionPredicate, brandPredicate));
            }

            // Brand filter
            if (brand != null && !brand.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("brand"), brand));
            }

            // Category filter
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            // Price range filter
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("unitPrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("unitPrice"), maxPrice));
            }

            // In stock filter
            if (inStock != null && inStock) {
                predicates.add(criteriaBuilder.greaterThan(root.get("unitsInStock"), 0));
            }

            // Minimum rating filter
            if (minRating != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("averageRating"), minRating));
            }

            // New products filter
            if (isNew != null && isNew) {
                predicates.add(criteriaBuilder.isTrue(root.get("isNew")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
