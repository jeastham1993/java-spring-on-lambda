package com.product.api.core;

import org.springframework.data.repository.CrudRepository;

public interface IProductRepository extends CrudRepository<Product, Long> {
}
