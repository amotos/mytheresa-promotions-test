package com.mytheresa.promotions.application.api;

import com.mytheresa.promotions.domain.model.Category;
import com.mytheresa.promotions.domain.model.DiscountedProduct;
import com.mytheresa.promotions.domain.ports.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;


@RestController
@RequestMapping(ProductsController.PATH)
public class ProductsController {

    private static final Logger logger = LoggerFactory.getLogger(ProductsController.class);

    public static final String PATH = "api/";
    public static final String PRODUCTS_ENDPOINTS = "products";
    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(PRODUCTS_ENDPOINTS)
    public ResponseEntity<Collection<DiscountedProduct>> getProducts(@RequestParam(required = false) String category, @RequestParam(required = false) Integer priceLessThan) {
        Category modelCategory = null;

        try {
            if (category != null) modelCategory = Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException ex) {
            logger.warn("Invalid Category received: " + category);
            return ResponseEntity.badRequest().build();
        }

        if (priceLessThan != null && priceLessThan < 0) {
            logger.warn("Invalid price filter received: " + priceLessThan);
            return ResponseEntity.badRequest().build();
        }

        Collection<DiscountedProduct> products = productService.getProductsWithDiscount(modelCategory, priceLessThan);
        return ResponseEntity.ok(products);
    }

}
