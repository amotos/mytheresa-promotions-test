package com.mytheresa.promotions.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountedProductTest {

    @Test
    public void givenCompleteProductWithDiscount_whenDiscountedProductOf_thenReturnNewDiscountedProduct() {
        Product boot = new Product("sku1", "name", Category.BOOTS, 80000);
        DiscountedProduct discountedProduct = DiscountedProduct.of(boot, 48000, "40%");
        assertEquals(boot.getSku(), discountedProduct.getSku());
        assertEquals(boot.getName(), discountedProduct.getName());
        assertEquals(boot.getCategory().toString(), discountedProduct.getCategory());
        assertEquals(boot.getPrice(), discountedProduct.getPrice().getOriginalPrice());
        assertEquals(48000, discountedProduct.getPrice().getFinalPrice());
        assertEquals("40%", discountedProduct.getPrice().getDiscountPercentage());
        assertEquals("EUR", discountedProduct.getPrice().getCurrency());
    }

    @Test
    public void givenCompleteProductWithoutDiscount_whenDiscountedProductOf_thenReturnNewProductWithoutDiscount() {
        Product boot = new Product("sku1", "name", Category.BOOTS, 80000);
        DiscountedProduct discountedProduct = DiscountedProduct.of(boot, null, null);
        assertEquals(boot.getSku(), discountedProduct.getSku());
        assertEquals(boot.getName(), discountedProduct.getName());
        assertEquals(boot.getCategory().toString(), discountedProduct.getCategory());
        assertEquals(boot.getPrice(), discountedProduct.getPrice().getOriginalPrice());
        assertEquals(boot.getPrice(), discountedProduct.getPrice().getFinalPrice());
        assertEquals(null, discountedProduct.getPrice().getDiscountPercentage());
        assertEquals("EUR", discountedProduct.getPrice().getCurrency());
    }

}
