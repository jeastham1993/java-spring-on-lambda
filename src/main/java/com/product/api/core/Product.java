package com.product.api.core;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    public Product()
    {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private Double price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ProductDTO asDto()
    {
        ProductDTO dto = new ProductDTO();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setPrice(this.price);

        return dto;
    }
}
