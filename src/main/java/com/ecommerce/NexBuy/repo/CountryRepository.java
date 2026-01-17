package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "countries")
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}
