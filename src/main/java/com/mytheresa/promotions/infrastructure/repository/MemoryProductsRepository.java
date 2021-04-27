package com.mytheresa.promotions.infrastructure.repository;

import com.mytheresa.promotions.domain.model.Category;
import com.mytheresa.promotions.domain.model.Product;
import com.mytheresa.promotions.domain.ports.ProductsRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class MemoryProductsRepository implements ProductsRepository {

    private final Map<String, Product> products;

    public MemoryProductsRepository() {
        this.products = new HashMap<>();

        // Mocked data for test
        products.put("000001", new Product("000001", "BV Lean leather ankle boots", Category.BOOTS, 89000));
        products.put("000002", new Product("000002", "BV Lean leather ankle boots", Category.BOOTS, 99000));
        products.put("000003", new Product("000003", "Ashlington leather ankle boots", Category.BOOTS, 71000));
        products.put("000004", new Product("000004", "Naima embellished suede sandals", Category.SANDALS, 79500));
        products.put("000005", new Product("000005", "Nathane leather sneakers", Category.SNEAKERS, 59000));
    }

    @Override
    public Collection<Product> getProductsByCategoryAndPriceLessThan(Category category, Integer priceLessThan) {
        return products.values().stream()
                .filter(product -> category == null || product.getCategory() == category)
                .filter(product -> priceLessThan == null || product.getPrice() <= priceLessThan)
                .collect(Collectors.toList());
    }

}
