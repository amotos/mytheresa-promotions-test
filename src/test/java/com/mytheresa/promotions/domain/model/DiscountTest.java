package com.mytheresa.promotions.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiscountTest {

    @Test
    public void givenValidDiscountPercentage_whenCreatingDiscount_thenCreatesCorrectly() {
        Discount discount0 = new MockDiscount(0);
        assertEquals(0, discount0.getDiscountPercentage());

        Discount discount10 = new MockDiscount(10);
        assertEquals(10, discount10.getDiscountPercentage());

        Discount discount99 = new MockDiscount(99);
        assertEquals(99, discount99.getDiscountPercentage());

        Discount discount100 = new MockDiscount(100);
        assertEquals(100, discount100.getDiscountPercentage());
    }

    @Test
    public void givenInvalidDiscountPercentage_whenCreatingDiscount_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new MockDiscount(-1));
        assertThrows(IllegalArgumentException.class, () -> new MockDiscount(null));
        assertThrows(IllegalArgumentException.class, () -> new MockDiscount(101));
    }

    @Test
    public void givenDiscountApplied_whenGetDiscountedPrice_thenReturnDiscountedPrice() {
        Product boot = new Product("sku", "discounted", Category.BOOTS, 1000);

        Discount discount = new MockDiscount(0);
        assertEquals(1000, discount.getDiscountedPrice(boot));

        Discount discount10 = new MockDiscount(10);
        assertEquals(900, discount10.getDiscountedPrice(boot));

        Discount discount20 = new MockDiscount(20);
        assertEquals(800, discount20.getDiscountedPrice(boot));

        Discount discount50 = new MockDiscount(50);
        assertEquals(500, discount50.getDiscountedPrice(boot));

        Discount discount90 = new MockDiscount(90);
        assertEquals(100, discount90.getDiscountedPrice(boot));

        Discount discount99 = new MockDiscount(99);
        assertEquals(10, discount99.getDiscountedPrice(boot));

        Discount discount100 = new MockDiscount(100);
        assertEquals(0, discount100.getDiscountedPrice(boot));
    }

    @Test
    public void givenDiscountAppliedWithDecimals_whenGetDiscountedPrice_thenReturnDiscountedRoundedPrice() {
        Product boot = new Product("sku", "discounted", Category.BOOTS, 99999);

        Discount discount1 = new MockDiscount(1);
        assertEquals(98999, discount1.getDiscountedPrice(boot));

        Discount discount21 = new MockDiscount(21);
        assertEquals(78999, discount21.getDiscountedPrice(boot));
    }

    @Test
    public void givenDiscountDoesntApply_whenGetDiscountedPrice_thenReturnProductPrice() {
        Product boot = new Product("sku", "full price sale", Category.BOOTS, 1000);

        Discount discount20 = new MockDiscount(20);
        assertEquals(1000, discount20.getDiscountedPrice(boot));

        Discount discount90 = new MockDiscount(90);
        assertEquals(1000, discount90.getDiscountedPrice(boot));

        Discount discount100 = new MockDiscount(100);
        assertEquals(1000, discount100.getDiscountedPrice(boot));
    }

    @Test
    public void givenDiscountOnNullProduct_whenGetDiscountedPrice_thenReturnNull() {
        Discount discount20 = new MockDiscount(20);
        assertNull(discount20.getDiscountedPrice(null));
    }

    @Test
    public void givenDiscountOnNullPriceProduct_whenGetDiscountedPrice_thenReturnNull() {
        Product boot = new Product("sku", "full price sale", Category.BOOTS, null);
        Discount discount20 = new MockDiscount(20);

        assertNull(discount20.getDiscountedPrice(boot));
    }

    @Test
    public void givenDiscount_whenGetDiscountText_thenReturnDiscountString() {
        Discount discount0 = new MockDiscount(0);
        assertEquals("0%", discount0.getDiscountText());

        Discount discount1 = new MockDiscount(1);
        assertEquals("1%", discount1.getDiscountText());

        Discount discount10 = new MockDiscount(10);
        assertEquals("10%", discount10.getDiscountText());

        Discount discount99 = new MockDiscount(99);
        assertEquals("99%", discount99.getDiscountText());

        Discount discount100 = new MockDiscount(100);
        assertEquals("100%", discount100.getDiscountText());
    }

    private static class MockDiscount extends Discount {

        public MockDiscount(Integer discountPercentage) {
            super(discountPercentage);
        }

        @Override
        public boolean isApplicable(Product product) {
            return "discounted".equals(product.getName());
        }
    }
}
