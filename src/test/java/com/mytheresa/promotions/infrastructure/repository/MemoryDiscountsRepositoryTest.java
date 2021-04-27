package com.mytheresa.promotions.infrastructure.repository;

import com.mytheresa.promotions.domain.model.Category;
import com.mytheresa.promotions.domain.model.CategoryDiscount;
import com.mytheresa.promotions.domain.model.Discount;
import com.mytheresa.promotions.domain.model.ProductDiscount;
import com.mytheresa.promotions.domain.ports.DiscountsRepository;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemoryDiscountsRepositoryTest {

    // Basic tests to ensure in memory static repository works as expected

    private final DiscountsRepository discountsRepository = new MemoryDiscountsRepository();

    @Test
    public void givenOnlyOneCategoryDiscountExists_whenGetDiscountsWithoutCategory_thenReturnOneDiscount() {
        Collection<Discount> discounts = discountsRepository.getDiscountsByCategory(null);
        assertEquals(1, discounts.size());
        assertTrue(discounts.contains(new CategoryDiscount(30, Category.BOOTS)));
    }

    @Test
    public void givenOnlyOneCategoryDiscountExists_whenGetDiscountsByExistingCategory_thenReturnOneDiscount() {
        Collection<Discount> discounts = discountsRepository.getDiscountsByCategory(Category.BOOTS);
        assertEquals(1, discounts.size());
        assertTrue(discounts.contains(new CategoryDiscount(30, Category.BOOTS)));
    }

    @Test
    public void givenOnlyOneCategoryDiscountExists_whenGetDiscountsByNonExistingCategory_thenReturnOneDiscount() {
        Collection<Discount> discounts = discountsRepository.getDiscountsByCategory(Category.SANDALS);
        assertEquals(0, discounts.size());
    }

    @Test
    public void givenOnlyOneProductDiscountExists_whenGetDiscountByProductExists_thenReturnOneDiscount() {
        Collection<Discount> discounts = discountsRepository.getDiscountByProduct("000003");
        assertEquals(1, discounts.size());
        assertTrue(discounts.contains(new ProductDiscount(15, "000003")));
    }

    @Test
    public void givenOnlyOneProductDiscountExists_whenGetDiscountByProductNonExisting_thenReturnOneDiscount() {
        Collection<Discount> discounts = discountsRepository.getDiscountByProduct("potato");
        assertEquals(0, discounts.size());
    }
}
