package com.mytheresa.promotions.domain.model;

import lombok.Data;
import lombok.NonNull;

@Data
public abstract class Discount {

    @NonNull private Integer discountPercentage;

    public Discount(Integer discountPercentage) {
        if(discountPercentage == null || discountPercentage < 0 || discountPercentage > 100)
            throw new IllegalArgumentException("Discount percentage must be a valid number between 0 and 100.");
        this.discountPercentage = discountPercentage;
    }

    public abstract boolean isApplicable(Product product);

    public final Integer getDiscountedPrice(Product product) {
        if(product == null || product.getPrice() == null)
            return null;

        if(!isApplicable(product))
            return product.getPrice();

        return ((100 - discountPercentage) * product.getPrice()) / 100;
    }

    public final String getDiscountText() {
        return discountPercentage + "%";
    }
}
