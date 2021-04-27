package com.mytheresa.promotions.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductDiscountTest {

    @Test
    public void givenProductDiscount_whenIsApplicableProduct_thenReturnTrue() {
        Product boot = new Product("sku1", "name", Category.BOOTS, 80000);
        Discount bootDiscount = new ProductDiscount(30, "sku1");
        assertTrue(bootDiscount.isApplicable(boot));

        Product sneakers = new Product("sku2", "name", Category.SNEAKERS, 75000);
        Discount sneakerDiscount = new ProductDiscount(30, "sku2");
        assertTrue(sneakerDiscount.isApplicable(sneakers));

        Product sandal = new Product("sku3", "name", Category.SANDALS, 70000);
        Discount sandalDiscount = new ProductDiscount(30, "sku3");
        assertTrue(sandalDiscount.isApplicable(sandal));
    }

    @Test
    public void givenProductDiscount_whenNotApplicableProduct_thenReturnFalse() {
        Product boot = new Product("sku", "name", Category.BOOTS, 80000);
        Discount sneakerDiscount = new ProductDiscount(30, "Different-SKU");
        assertFalse(sneakerDiscount.isApplicable(boot));

        Product sandal = new Product("sku", "name", Category.SANDALS, 70000);
        assertFalse(sneakerDiscount.isApplicable(sandal));
    }

    @Test
    public void givenProductDiscount_whenIsApplicableNullProduct_thenReturnFalse() {
        Discount sneakerDiscount = new ProductDiscount(30, "sku");
        assertFalse(sneakerDiscount.isApplicable(null));
    }

}
