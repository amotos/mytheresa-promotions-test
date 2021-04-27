package com.mytheresa.promotions.domain.ports;

import com.mytheresa.promotions.domain.model.Category;
import com.mytheresa.promotions.domain.model.Discount;

import java.util.Collection;

public interface DiscountsRepository {

    Collection<Discount> getDiscountsByCategory(Category category);

    Collection<Discount> getDiscountByProduct(String sku);

}
