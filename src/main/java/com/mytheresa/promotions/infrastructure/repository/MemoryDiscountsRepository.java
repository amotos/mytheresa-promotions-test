package com.mytheresa.promotions.infrastructure.repository;

import com.mytheresa.promotions.domain.model.Category;
import com.mytheresa.promotions.domain.model.CategoryDiscount;
import com.mytheresa.promotions.domain.model.Discount;
import com.mytheresa.promotions.domain.model.ProductDiscount;
import com.mytheresa.promotions.domain.ports.DiscountsRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class MemoryDiscountsRepository implements DiscountsRepository {

    private final Map<Category, Set<Discount>> discountsByCategory = new HashMap<>();
    private final Map<String, Set<Discount>> discountsByProduct = new HashMap<>();

    public MemoryDiscountsRepository() {
        // Mock data for exercise
        discountsByCategory.put(Category.BOOTS, Set.of(new CategoryDiscount(30, Category.BOOTS)));

        discountsByProduct.put("000003", Set.of(new ProductDiscount(15, "000003")));
    }
    @Override
    public Collection<Discount> getDiscountsByCategory(Category category) {
        if (category != null)
            return discountsByCategory.getOrDefault(category, Collections.emptySet());
        return discountsByCategory.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<Discount> getDiscountByProduct(String sku) {
        return discountsByProduct.getOrDefault(sku, Collections.emptySet());
    }
}
