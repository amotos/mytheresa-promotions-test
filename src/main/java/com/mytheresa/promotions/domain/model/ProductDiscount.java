package com.mytheresa.promotions.domain.model;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProductDiscount extends Discount {

    @NonNull String sku;

    public ProductDiscount(Integer discountPercentage, String sku) {
        super(discountPercentage);
        this.sku = sku;
    }

    @Override
    public boolean isApplicable(Product product) {
        return product != null && sku.equals(product.getSku());
    }
}
