package com.mytheresa.promotions.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private String sku;
    private String name;
    private Category category;
    private Integer price;
    private final String currency = "EUR";

}
