package com.mytheresa.promotions.infrastructure.repository;

import com.mytheresa.promotions.domain.model.Category;
import com.mytheresa.promotions.domain.model.Product;
import com.mytheresa.promotions.domain.ports.ProductsRepository;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemoryProductsRepositoryTest {

    // Basic tests to ensure in memory static repository works as expected

    private static final Product boot1 = new Product("000001", "BV Lean leather ankle boots", Category.BOOTS, 89000);
    private static final Product boot2 = new Product("000002", "BV Lean leather ankle boots", Category.BOOTS, 99000);
    private static final Product boot3 = new Product("000003", "Ashlington leather ankle boots", Category.BOOTS, 71000);
    private static final Product sandal = new Product("000004", "Naima embellished suede sandals", Category.SANDALS, 79500);
    private static final Product sneakers = new Product("000005", "Nathane leather sneakers", Category.SNEAKERS, 59000);

    private final ProductsRepository productsRepository = new MemoryProductsRepository();

    @Test
    public void given5Products_whenGetProductsWithoutParameters_thenReturnAllProducts() {
        Collection<Product> products = productsRepository.getProductsByCategoryAndPriceLessThan(null, null);
        assertEquals(5, products.size());
        assertTrue(products.contains(boot1));
        assertTrue(products.contains(boot2));
        assertTrue(products.contains(boot3));
        assertTrue(products.contains(sandal));
        assertTrue(products.contains(sneakers));
    }

    @Test
    public void given5Products_whenGetProductsFromCategory_thenReturnOnlyProductsFromCategory() {
        Collection<Product> foundBoots = productsRepository.getProductsByCategoryAndPriceLessThan(Category.BOOTS, null);
        assertEquals(3, foundBoots.size());
        assertTrue(foundBoots.contains(boot1));
        assertTrue(foundBoots.contains(boot2));
        assertTrue(foundBoots.contains(boot3));

        Collection<Product> foundSandals = productsRepository.getProductsByCategoryAndPriceLessThan(Category.SANDALS, null);
        assertEquals(1, foundSandals.size());
        assertTrue(foundSandals.contains(sandal));

        Collection<Product> foundSneakers = productsRepository.getProductsByCategoryAndPriceLessThan(Category.SNEAKERS, null);
        assertEquals(1, foundSneakers.size());
        assertTrue(foundSneakers.contains(sneakers));
    }

    @Test
    public void given5Products_whenGetProductsUnderThreshold_thenReturnOnlyOneProduct() {
        Collection<Product> cheapProducts = productsRepository.getProductsByCategoryAndPriceLessThan(null, 60000);
        assertEquals(1, cheapProducts.size());
        assertTrue(cheapProducts.contains(sneakers));
    }

    @Test
    public void given5Products_whenGetProductsFromCategoryAndUnderThreshold_thenReturnMatchingProduct() {
        Collection<Product> foundBoots = productsRepository.getProductsByCategoryAndPriceLessThan(Category.BOOTS, 89000);
        assertEquals(2, foundBoots.size());
        assertTrue(foundBoots.contains(boot1));
        assertTrue(foundBoots.contains(boot3));
    }
}
