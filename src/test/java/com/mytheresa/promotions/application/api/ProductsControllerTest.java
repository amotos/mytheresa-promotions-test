package com.mytheresa.promotions.application.api;

import com.mytheresa.promotions.domain.model.Category;
import com.mytheresa.promotions.domain.model.DiscountedProduct;
import com.mytheresa.promotions.domain.model.Product;
import com.mytheresa.promotions.domain.ports.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductsControllerTest {

    private static final DiscountedProduct boot1 = DiscountedProduct.of(new Product("000001", "BV Lean leather ankle boots", Category.BOOTS, 89000), null, null);
    private static final DiscountedProduct boot2 = DiscountedProduct.of(new Product("000002", "BV Lean leather ankle boots", Category.BOOTS, 99000), null, null);
    private static final DiscountedProduct sandals = DiscountedProduct.of(new Product("000004", "Naima embellished suede sandals", Category.SANDALS, 79500), null, null);
    private static final DiscountedProduct sneakers = DiscountedProduct.of(new Product("000005", "Nathane leather sneakers", Category.SNEAKERS, 59000), null, null);

    ProductsController productsController;
    ProductService productService;

    @BeforeEach
    public void setup() {
        this.productService = mock(ProductService.class);
        this.productsController = new ProductsController(productService);
    }

    @Test
    public void givenNoProductsExist_whenGetProductsWithoutParameters_thenReturnEmptyList() {
        when(productService.getProductsWithDiscount(isNull(), isNull())).thenReturn(Collections.emptyList());
        ResponseEntity<Collection<DiscountedProduct>> response = productsController.getProducts(null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Collection<DiscountedProduct> products = response.getBody();
        assertNotNull(products);
        assertEquals(0, products.size());
    }

    @Test
    public void givenNoProductsExist_whenGetProductsWithCategory_thenReturnEmptyList() {
        when(productService.getProductsWithDiscount(eq(Category.BOOTS), isNull())).thenReturn(Collections.emptyList());
        ResponseEntity<Collection<DiscountedProduct>> response = productsController.getProducts("Boots", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Collection<DiscountedProduct> products = response.getBody();
        assertNotNull(products);
        assertEquals(0, products.size());
    }

    @Test
    public void givenNoProductsExist_whenGetProductsWithPriceLessThan_thenReturnEmptyList() {
        when(productService.getProductsWithDiscount(isNull(), eq(10))).thenReturn(Collections.emptyList());
        ResponseEntity<Collection<DiscountedProduct>> response = productsController.getProducts(null, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Collection<DiscountedProduct> products = response.getBody();
        assertNotNull(products);
        assertEquals(0, products.size());
    }

    @Test
    public void givenProductsExist_whenGetProductsWithoutCategoryAndPriceLessThan_thenReturnAllProducts() {
        Collection<DiscountedProduct> storedProducts = Arrays.asList(boot1, boot2, sandals, sneakers);
        when(productService.getProductsWithDiscount(isNull(), isNull())).thenReturn(storedProducts);
        ResponseEntity<Collection<DiscountedProduct>> response = productsController.getProducts(null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Collection<DiscountedProduct> products = response.getBody();
        assertNotNull(products);
        assertEquals(storedProducts, products);
    }

    @Test
    public void givenProductsExist_whenGetProductsWithCategory_thenReturnCategoryProducts() {
        Collection<DiscountedProduct> storedProducts = Arrays.asList(boot1, boot2);
        when(productService.getProductsWithDiscount(eq(Category.BOOTS), isNull())).thenReturn(storedProducts);
        ResponseEntity<Collection<DiscountedProduct>> response = productsController.getProducts("Boots", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Collection<DiscountedProduct> products = response.getBody();
        assertNotNull(products);
        assertEquals(storedProducts, products);
    }

    @Test
    public void givenProductsExist_whenGetProductsWithPriceLessThan_thenReturnPriceLessThanProducts() {
        Collection<DiscountedProduct> storedProducts = Collections.singletonList(sneakers);
        when(productService.getProductsWithDiscount(isNull(), eq(60000))).thenReturn(storedProducts);
        ResponseEntity<Collection<DiscountedProduct>> response = productsController.getProducts(null, 60000);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Collection<DiscountedProduct> products = response.getBody();
        assertNotNull(products);
        assertEquals(storedProducts, products);
    }

    @Test
    public void givenProductsExist_whenGetProductsWithCategoryAndPriceLessThan_thenReturnCategoryAndPriceLessThanProducts() {
        Collection<DiscountedProduct> storedProducts = Collections.singletonList(sneakers);
        when(productService.getProductsWithDiscount(eq(Category.SANDALS), eq(60000))).thenReturn(storedProducts);
        ResponseEntity<Collection<DiscountedProduct>> response = productsController.getProducts("sandals", 60000);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Collection<DiscountedProduct> products = response.getBody();
        assertNotNull(products);
        assertEquals(storedProducts, products);
    }

    @Test
    public void givenProductsExist_whenGetProductsWithIncorrectCategory_thenReturnError() {
        ResponseEntity<Collection<DiscountedProduct>> response = productsController.getProducts("potato", null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void givenProductsExist_whenGetProductsWithIncorrectPriceLessThan_thenReturnError() {
        ResponseEntity<Collection<DiscountedProduct>> response = productsController.getProducts("Boots", -10);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}
