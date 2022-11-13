package com.product.api.core;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProductService {
    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public ProductDTO CreateProduct(ProductDTO productDTO) {
        var product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());

        var savedProduct = this.productRepository.save(product);

        return savedProduct.asDto();
    }

    public ProductDTO GetProduct(long productId) {
        var retrievedProduct = this.productRepository.findById(productId);

        return retrievedProduct.map(Product::asDto).orElse(null);

    }

    public Iterable<ProductDTO> ListProducts() {
        var products = this.productRepository.findAll();

        var productDtoList = new ArrayList<ProductDTO>();

        var productIterator = products.iterator();

        while (productIterator.hasNext())
        {
            productDtoList.add(productIterator.next().asDto());
        }

        return productDtoList;
    }
}