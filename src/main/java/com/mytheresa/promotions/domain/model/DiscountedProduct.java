package com.mytheresa.promotions.domain.model;

import lombok.Data;

@Data
public class DiscountedProduct {

    private String sku;
    private String name;
    private String category;
    private Price price;

    private DiscountedProduct() {
        // Ensures no discounted product can be created without a base product
    }

    public static DiscountedProduct of(Product product, Integer finalPrice, String discountPercentage) {
        if (product == null)
            return null;

        DiscountedProduct discountedProduct = new DiscountedProduct();
        discountedProduct.sku = product.getSku();
        discountedProduct.name = product.getName();
        discountedProduct.category = product.getCategory().toString();
        discountedProduct.price = new Price(product.getPrice(), finalPrice, discountPercentage);
        return discountedProduct;
    }

    @Data
    public static class Price {
        private final String currency = "EUR";
        private Integer originalPrice;
        private Integer finalPrice;
        private String discountPercentage;

        public Price(Integer originalPrice, Integer finalPrice, String discountPercentage) {
            this.originalPrice = originalPrice;
            if(finalPrice == null)
                this.finalPrice = originalPrice;
            else
                this.finalPrice = finalPrice;
            this.discountPercentage = discountPercentage;
        }

    }
}
