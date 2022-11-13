package com.product.api;

import com.product.api.core.ProductDTO;
import com.product.api.core.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ProductController {
    private final ProductService productService;
    private final ApplicationProperties applicationProperties;

    public ProductController(ProductService productService, ApplicationConfiguration configuration) {
        this.productService = productService;
        this.applicationProperties = configuration.getApplicationProperties();
    }

    @PostMapping("/product")
    ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO product) {
        var createdProduct = productService.CreateProduct(product);

        return ResponseEntity.ok(createdProduct);
    }

    @GetMapping("/product")
    ResponseEntity<Iterable<ProductDTO>> listProducts() {
        var productList = productService.ListProducts();

        return ResponseEntity.ok(productList);
    }

    @GetMapping("/product/{id}")
    ResponseEntity<ProductDTO> getProduct(@PathVariable long id) {
        var product = productService.GetProduct(id);

        return ResponseEntity.ok(product);
    }
}
