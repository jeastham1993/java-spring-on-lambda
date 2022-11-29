package com.product.api;

import com.product.api.core.ProductDTO;
import com.product.api.core.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ProductController {
    private final ProductService productService;
    private final ApplicationProperties applicationProperties;

    private static final Logger LOG = LogManager.getLogger();

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
        LOG.info("Received request to retrieve products");

        var productList = productService.ListProducts();

        LOG.info("Product listing successful");

        return ResponseEntity.ok(productList);
    }

    @GetMapping("/product/{id}")
    ResponseEntity<ProductDTO> getProduct(@PathVariable long id) {
        var product = productService.GetProduct(id);

        return ResponseEntity.ok(product);
    }
}
