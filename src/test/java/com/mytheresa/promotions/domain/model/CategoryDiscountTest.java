package com.mytheresa.promotions.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CategoryDiscountTest {

    @Test
    public void givenCategoryDiscount_whenIsApplicableSameCategory_thenReturnTrue() {
        Product boot = new Product("sku", "name", Category.BOOTS, 80000);
        Discount bootDiscount = new CategoryDiscount(30, Category.BOOTS);
        assertTrue(bootDiscount.isApplicable(boot));

        Product sneakers = new Product("sku", "name", Category.SNEAKERS, 75000);
        Discount sneakerDiscount = new CategoryDiscount(30, Category.SNEAKERS);
        assertTrue(sneakerDiscount.isApplicable(sneakers));

        Product sandal = new Product("sku", "name", Category.SANDALS, 70000);
        Discount sandalDiscount = new CategoryDiscount(30, Category.SANDALS);
        assertTrue(sandalDiscount.isApplicable(sandal));
    }

    @Test
    public void givenCategoryDiscount_whenIsApplicableDifferentCategory_thenReturnFalse() {
        Product boot = new Product("sku", "name", Category.BOOTS, 80000);
        Discount sneakerDiscount = new CategoryDiscount(30, Category.SNEAKERS);
        assertFalse(sneakerDiscount.isApplicable(boot));

        Product sandal = new Product("sku", "name", Category.SANDALS, 70000);
        assertFalse(sneakerDiscount.isApplicable(sandal));
    }

    @Test
    public void givenCategoryDiscount_whenIsApplicableNullProduct_thenReturnFalse() {
        Discount sneakerDiscount = new CategoryDiscount(30, Category.SNEAKERS);
        assertFalse(sneakerDiscount.isApplicable(null));
    }

}
