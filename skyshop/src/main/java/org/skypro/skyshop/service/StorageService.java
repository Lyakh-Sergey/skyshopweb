package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.*;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StorageService {
    private final Map<UUID, Product> products = new HashMap<>();
    private final Map<UUID, Article> articles = new HashMap<>();

    public StorageService() {
        initializeData();
    }

    private void initializeData() {
        addProduct(new SimpleProduct(UUID.randomUUID(), "Арбуз", 90));
        addProduct(new SimpleProduct(UUID.randomUUID(), "Яблоко", 80));
        addProduct(new DiscountedProduct(UUID.randomUUID(), "Дыня", 100, 10));
        addProduct(new SimpleProduct(UUID.randomUUID(), "Ананас", 145));
        addProduct(new FixPriceProduct(UUID.randomUUID(), "Груша"));
        addProduct(new SimpleProduct(UUID.randomUUID(), "Помидор", 70));
        addProduct(new DiscountedProduct(UUID.randomUUID(), "Огурец", 60, 10));
        addProduct(new FixPriceProduct(UUID.randomUUID(), "Слива"));
        addProduct(new DiscountedProduct(UUID.randomUUID(), "Хлеб", 35, 20));

        addArticle(new Article(UUID.randomUUID(), "Арбузы", "Польза арбузов"));
        addArticle(new Article(UUID.randomUUID(), "Хлеб", "Хлеб всему голова"));
        addArticle(new Article(UUID.randomUUID(), "Полезный сад", "Польза и вред слив"));
        addArticle(new Article(UUID.randomUUID(), "Итальянские деликатесы", "Помидоры и прочее"));
        addArticle(new Article(UUID.randomUUID(), "Яблоки", "Польза яблок"));
        addArticle(new Article(UUID.randomUUID(), "Дыня", "Дыня лучшая"));
        addArticle(new Article(UUID.randomUUID(), "Полезный огород", "Польза и вред огурца"));
        addArticle(new Article(UUID.randomUUID(), "Пуаре", "Грушевый сидр"));
        addArticle(new Article(UUID.randomUUID(), "Гаджеты и лайки", "Лучшие смартфоны 2025"));
    }

    public Collection<Product> getAllProducts() {
        return Collections.unmodifiableCollection(products.values());
    }

    public Collection<Article> getAllArticles() {
        return Collections.unmodifiableCollection(articles.values());
    }

    public Collection<Searchable> getAllSearchables() {
        List<Searchable> searchables = new ArrayList<>();
        searchables.addAll(products.values());
        searchables.addAll(articles.values());
        return searchables;
    }

    private void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    private void addArticle(Article article) {
        articles.put(article.getId(), article);
    }

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }
}
