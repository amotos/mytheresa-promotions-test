package com.mytheresa.promotions.domain.ports;

import com.mytheresa.promotions.domain.model.Category;
import com.mytheresa.promotions.domain.model.Product;

import java.util.Collection;

public interface ProductsRepository {

    Collection<Product> getProductsByCategoryAndPriceLessThan(Category category, Integer priceLessThan);
}
