package org.skypro.skyshop.service;

import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BasketService {
    private final ProductBasket productBasket;
    private final StorageService storageService;

    public BasketService(ProductBasket productBasket, StorageService storageService) {
        this.productBasket = productBasket;
        this.storageService = storageService;
    }

    public void addProduct(UUID productId) {
        Product product = storageService.getProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        productBasket.addProduct(productId);
    }

    public UserBasket getUserBasket() {
        Map<UUID, Integer> basketProducts = productBasket.getProducts();

        List<BasketItem> items = basketProducts.entrySet().stream()
                .map(entry -> {
                    Product product = storageService.getProductById(entry.getKey())
                            .orElseThrow(() -> new IllegalStateException("Product in basket not found in storage"));
                    return new BasketItem(product, entry.getValue());
                })
                .collect(Collectors.toList());

        return new UserBasket(items);
    }
}
