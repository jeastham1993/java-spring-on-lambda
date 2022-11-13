package com.product.api;

import com.product.api.core.ProductDTO;
import com.product.api.core.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
public class PropertiesController {
    private final ApplicationProperties applicationProperties;

    public PropertiesController(ApplicationConfiguration configuration) {
        this.applicationProperties = configuration.getApplicationProperties();
    }

    @GetMapping("/properties")
    ResponseEntity<ArrayList<String>> listProducts() {
        var properties = new ArrayList<String>();
        properties.add(this.applicationProperties.getMyApplicationProperty());

        return ResponseEntity.ok(properties);
    }
}
