package com.mytheresa.promotions.domain.ports;

import com.mytheresa.promotions.domain.model.Category;
import com.mytheresa.promotions.domain.model.CategoryDiscount;
import com.mytheresa.promotions.domain.model.Discount;
import com.mytheresa.promotions.domain.model.DiscountedProduct;
import com.mytheresa.promotions.domain.model.Product;
import com.mytheresa.promotions.domain.model.ProductDiscount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    private ProductsRepository productsRepository;
    private DiscountsRepository discountsRepository;
    private ProductService productService;

    @BeforeEach
    public void setup() {
        this.productsRepository = mock(ProductsRepository.class);
        this.discountsRepository = mock(DiscountsRepository.class);
        this.productService = new ProductService(productsRepository, discountsRepository);
    }

    @Test
    public void givenEmptyRepository_whenGetProductsWithDiscount_thenReturnEmptyList() {
        when(productsRepository.getProductsByCategoryAndPriceLessThan(null, null)).thenReturn(Collections.emptyList());
        when(discountsRepository.getDiscountsByCategory(null)).thenReturn(Collections.emptyList());

        Collection<DiscountedProduct> products = productService.getProductsWithDiscount(null, null);

        assertTrue(products.isEmpty());
    }

    @Test
    public void givenProductsButNoDiscounts_whenGetProductsWithDiscountWithoutParameters_thenReturnProductsWithoutDiscount() {
        Product boot = new Product("sku1", "name", Category.BOOTS, 80000);
        DiscountedProduct discountedProduct = DiscountedProduct.of(boot, 80000, null);

        when(productsRepository.getProductsByCategoryAndPriceLessThan(null, null)).thenReturn(Collections.singletonList(boot));
        when(discountsRepository.getDiscountsByCategory(null)).thenReturn(Collections.emptyList());
        when(discountsRepository.getDiscountByProduct("sku1")).thenReturn(Collections.emptyList());

        Collection<DiscountedProduct> products = productService.getProductsWithDiscount(null, null);

        assertEquals(1, products.size());
        assertEquals(discountedProduct, products.stream().findFirst().get());
    }

    @Test
    public void givenProductsAndDiscounts_whenGetProductsWithDiscountWithNoParameters_thenReturnProductsWithDiscountApplied() {
        Product boot = new Product("sku", "name", Category.BOOTS, 80000);
        Product sneakers = new Product("sku2", "name", Category.SNEAKERS, 75000);
        Product sandal = new Product("sku3", "name", Category.SANDALS, 70000);

        Discount skuDiscount = new ProductDiscount(10, "sku");
        Discount bootDiscount = new CategoryDiscount(40, Category.BOOTS);
        Discount sneakerDiscount = new CategoryDiscount(20, Category.SNEAKERS);
        Discount sandalDiscount = new CategoryDiscount(10, Category.SANDALS);

        DiscountedProduct discountedBoot = DiscountedProduct.of(boot, 48000, "40%");
        DiscountedProduct discountedSneaker = DiscountedProduct.of(sneakers, 60000, "20%");
        DiscountedProduct discountedSandal = DiscountedProduct.of(sandal, 63000, "10%");

        when(productsRepository.getProductsByCategoryAndPriceLessThan(null, null)).thenReturn(Arrays.asList(boot, sneakers, sandal));
        when(discountsRepository.getDiscountsByCategory(null)).thenReturn(Arrays.asList(bootDiscount, sneakerDiscount, sandalDiscount));
        when(discountsRepository.getDiscountByProduct("sku")).thenReturn(Collections.singletonList(skuDiscount));

        Collection<DiscountedProduct> products = productService.getProductsWithDiscount(null, null);

        assertEquals(3, products.size());
        assertTrue(products.contains(discountedBoot));
        assertTrue(products.contains(discountedSneaker));
        assertTrue(products.contains(discountedSandal));
    }

    @Test
    public void givenProductsAndDiscounts_whenGetProductsWithDiscountFromCategory_thenReturnProductsMatchWithDiscountApplied() {
        Product boot = new Product("sku", "name", Category.BOOTS, 80000);
        Product boot2 = new Product("sku2", "Expensive boots", Category.BOOTS, 100000);

        Discount skuDiscount = new ProductDiscount(40, "sku2");
        Discount bootDiscount = new CategoryDiscount(10, Category.BOOTS);

        DiscountedProduct discountedBoot = DiscountedProduct.of(boot, 72000, "10%");
        DiscountedProduct discountedBoot2 = DiscountedProduct.of(boot2, 60000, "40%");

        when(productsRepository.getProductsByCategoryAndPriceLessThan(Category.BOOTS, null)).thenReturn(Arrays.asList(boot, boot2));
        when(discountsRepository.getDiscountsByCategory(Category.BOOTS)).thenReturn(Collections.singletonList(bootDiscount));
        when(discountsRepository.getDiscountByProduct("sku")).thenReturn(Collections.emptyList());
        when(discountsRepository.getDiscountByProduct("sku2")).thenReturn(Collections.singletonList(skuDiscount));

        Collection<DiscountedProduct> products = productService.getProductsWithDiscount(Category.BOOTS, null);

        assertEquals(2, products.size());
        assertTrue(products.contains(discountedBoot));
        assertTrue(products.contains(discountedBoot2));
    }

    @Test
    public void givenProductsAndDiscounts_whenGetProductsWithDiscountAndPrice_thenReturnProductsMatchWithDiscountApplied() {
        Product sandal = new Product("sku", "name", Category.SANDALS, 45000);
        Product sandal2 = new Product("sku2", "Expensive sandals", Category.SANDALS, 200000);

        Discount skuDiscount = new ProductDiscount(40, "sku2");
        Discount bootDiscount = new CategoryDiscount(10, Category.SANDALS);

        DiscountedProduct discountedSandal = DiscountedProduct.of(sandal, 40500, "10%");
        DiscountedProduct discountedSandal2 = DiscountedProduct.of(sandal2, 120000, "40%");

        when(productsRepository.getProductsByCategoryAndPriceLessThan(Category.SANDALS, 300000)).thenReturn(Arrays.asList(sandal, sandal2));
        when(discountsRepository.getDiscountsByCategory(Category.SANDALS)).thenReturn(Collections.singletonList(bootDiscount));
        when(discountsRepository.getDiscountByProduct("sku")).thenReturn(Collections.emptyList());
        when(discountsRepository.getDiscountByProduct("sku2")).thenReturn(Collections.singletonList(skuDiscount));

        Collection<DiscountedProduct> products = productService.getProductsWithDiscount(Category.SANDALS, 300000);

        assertEquals(2, products.size());
        assertTrue(products.contains(discountedSandal));
        assertTrue(products.contains(discountedSandal2));
    }

}
