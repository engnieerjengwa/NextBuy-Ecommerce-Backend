package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.Country;
import com.ecommerce.NexBuy.entity.Province;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CountryProvinceRepositoryTests {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Test
    void testCountryRepository() {
        // Test findAll
        List<Country> countries = countryRepository.findAll();
        assertFalse(countries.isEmpty(), "Countries should not be empty");
        
        // Test findById
        if (!countries.isEmpty()) {
            Long countryId = countries.get(0).getId();
            Optional<Country> country = countryRepository.findById(countryId);
            assertTrue(country.isPresent(), "Country should be found by ID");
        }
    }

    @Test
    void testProvinceRepository() {
        // Test findAll
        List<Province> provinces = provinceRepository.findAll();
        assertFalse(provinces.isEmpty(), "Provinces should not be empty");
        
        // Test findById
        if (!provinces.isEmpty()) {
            Long provinceId = provinces.get(0).getId();
            Optional<Province> province = provinceRepository.findById(provinceId);
            assertTrue(province.isPresent(), "Province should be found by ID");
        }
        
        // Test findByCountryId
        List<Country> countries = countryRepository.findAll();
        if (!countries.isEmpty()) {
            String countryCode = countries.get(0).getCode();
            List<Province> provincesByCountry = provinceRepository.findByCountryCode(countryCode);
            assertFalse(provincesByCountry.isEmpty(), "Provinces should be found for country");
        }
    }
}