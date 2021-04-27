package com.mytheresa.promotions.domain.ports;

import com.mytheresa.promotions.domain.model.Category;
import com.mytheresa.promotions.domain.model.Discount;
import com.mytheresa.promotions.domain.model.DiscountedProduct;
import com.mytheresa.promotions.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class ProductService {

    ProductsRepository productsRepository;
    DiscountsRepository discountsRepository;

    public ProductService(ProductsRepository productsRepository, DiscountsRepository discountsRepository) {
        this.productsRepository = productsRepository;
        this.discountsRepository = discountsRepository;
    }

    public Collection<DiscountedProduct> getProductsWithDiscount(Category category, Integer priceLessThan) {
        Collection<Product> products = productsRepository.getProductsByCategoryAndPriceLessThan(category, priceLessThan);
        Collection<Discount> categoryDiscounts = discountsRepository.getDiscountsByCategory(category);

        Collection<DiscountedProduct> discountedProducts = new ArrayList<>();

        for (Product product : products) {
            Integer minPrice = product.getPrice();
            String maxDiscount = null;

            // Process category wide discounts
            for (Discount discount : categoryDiscounts) {
                if (discount.isApplicable(product) && discount.getDiscountedPrice(product) < minPrice) {
                    minPrice = discount.getDiscountedPrice(product);
                    maxDiscount = discount.getDiscountText();
                }
            }

            // Search any specific sku discount
            for (Discount discount : discountsRepository.getDiscountByProduct(product.getSku())) {
                if (discount.isApplicable(product) && discount.getDiscountedPrice(product) < minPrice) {
                    minPrice = discount.getDiscountedPrice(product);
                    maxDiscount = discount.getDiscountText();
                }
            }

            // Transform the data to the final DiscountedProduct
            discountedProducts.add(DiscountedProduct.of(product, minPrice, maxDiscount));
        }

        return discountedProducts;
    }
}
