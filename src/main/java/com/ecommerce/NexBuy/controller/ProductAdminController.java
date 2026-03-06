package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.entity.Product;
import com.ecommerce.NexBuy.service.ProductAdminService;
import com.ecommerce.NexBuy.service.ProductAdminService.ProductCreateRequest;
import com.ecommerce.NexBuy.service.ProductAdminService.ProductUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin Products", description = "Product CRUD management (admin only)")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin
public class ProductAdminController {

    private final ProductAdminService productAdminService;

    public ProductAdminController(ProductAdminService productAdminService) {
        this.productAdminService = productAdminService;
    }

    @Operation(summary = "List all products", description = "Retrieve a paginated list of all products (admin only)")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return ResponseEntity.ok(productAdminService.getAllProducts(PageRequest.of(page, size, sort)));
    }

    @Operation(summary = "Get product", description = "Retrieve a product by ID (admin only)")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productAdminService.getProduct(id));
    }

    @Operation(summary = "Create product", description = "Create a new product (admin only)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody ProductCreateRequest request) {
        Product product = productAdminService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @Operation(summary = "Update product", description = "Update an existing product (admin only)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                  @RequestBody ProductUpdateRequest request) {
        Product product = productAdminService.updateProduct(id, request);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Delete product", description = "Delete a product by ID (admin only)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productAdminService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
