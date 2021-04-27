package com.mytheresa.promotions.domain.model;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CategoryDiscount extends Discount {

    @NonNull Category category;

    public CategoryDiscount(Integer discountPercentage, Category category) {
        super(discountPercentage);
        this.category = category;
    }

    @Override
    public boolean isApplicable(Product product) {
        return product != null && category.equals(product.getCategory());
    }

}
