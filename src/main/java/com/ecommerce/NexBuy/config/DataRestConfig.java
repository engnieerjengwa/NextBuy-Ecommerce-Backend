package com.ecommerce.NexBuy.config;

import com.ecommerce.NexBuy.entity.Country;
import com.ecommerce.NexBuy.entity.Product;
import com.ecommerce.NexBuy.entity.ProductCategory;
import com.ecommerce.NexBuy.entity.Province;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public DataRestConfig(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);

        // Configure CORS mapping
        cors.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(false)
                .maxAge(3600);

        // List of domain types to apply the HTTP method restrictions
        HttpMethod[] unsupportedHttpActions = {HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH};
        Class<?>[] domainTypes = {Product.class, ProductCategory.class, Country.class, Province.class};

        // Disable HTTP methods for specified domain types
        for (Class<?> domainType : domainTypes) {
            disableHttpMethods(config, domainType, unsupportedHttpActions);
        }

        // Expose entity IDs
        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        var entities = entityManager.getMetamodel().getEntities();
        Class[] entityClasses = entities.stream().map(e -> e.getJavaType()).toArray(Class[]::new);
        config.exposeIdsFor(entityClasses);
    }

    private void disableHttpMethods(RepositoryRestConfiguration config, Class<?> domainType, HttpMethod[] unsupportedHttpActions) {
        config.getExposureConfiguration()
                .forDomainType(domainType)
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedHttpActions))
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedHttpActions));
    }

}