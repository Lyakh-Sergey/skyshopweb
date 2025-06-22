package org.skypro.skyshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private ProductBasket productBasket;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private BasketService basketService;

    @Test
    void addProduct_shouldThrowException_whenProductNotFound() {
        // Подготовка
        UUID productId = UUID.randomUUID();
        when(storageService.getProductById(productId)).thenReturn(Optional.empty());

        // Действие и проверка
        assertThrows(NoSuchProductException.class,
                () -> basketService.addProduct(productId));

        verify(productBasket, never()).addProduct(any());
    }

    @Test
    void addProduct_shouldAddToBasket_whenProductExists() {
        // Подготовка
        UUID productId = UUID.randomUUID();
        Product product = new SimpleProduct(productId, "Apple", 100);
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));

        // Действие
        basketService.addProduct(productId);

        // Проверка
        verify(productBasket).addProduct(productId);
    }

    @Test
    void getUserBasket_shouldReturnEmptyBasket_whenNoProducts() {
        // Подготовка
        when(productBasket.getProducts()).thenReturn(Map.of());

        // Действие
        UserBasket result = basketService.getUserBasket();

        // Проверка
        assertTrue(result.getItems().isEmpty());
        assertEquals(0, result.getTotal());
    }

    @Test
    void getUserBasket_shouldReturnFilledBasket_whenProductsExist() {
        // Подготовка
        UUID productId = UUID.randomUUID();
        Product product = new SimpleProduct(productId, "Apple", 100);
        when(productBasket.getProducts()).thenReturn(Map.of(productId, 2));
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));

        // Действие
        UserBasket result = basketService.getUserBasket();

        // Проверка
        assertEquals(1, result.getItems().size());
        assertEquals(200, result.getTotal());
    }

    @Test
    void getUserBasket_shouldCalculateTotalCorrectly() {
        // Подготовка
        UUID product1 = UUID.randomUUID();
        UUID product2 = UUID.randomUUID();
        when(productBasket.getProducts())
                .thenReturn(Map.of(
                        product1, 2,
                        product2, 3
                ));
        when(storageService.getProductById(product1))
                .thenReturn(Optional.of(new SimpleProduct(product1, "A", 100)));
        when(storageService.getProductById(product2))
                .thenReturn(Optional.of(new SimpleProduct(product2, "B", 50)));

        // Действие
        UserBasket result = basketService.getUserBasket();

        // Проверка
        assertEquals(350, result.getTotal()); // 2*100 + 3*50 = 350
    }
}
