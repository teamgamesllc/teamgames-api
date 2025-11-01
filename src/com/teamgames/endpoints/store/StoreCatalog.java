package com.teamgames.endpoints.store;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.teamgames.https.JsonPost;
import com.teamgames.lib.gson.Gson;
import com.teamgames.util.Thread;

/**
 * Fetches the v2 product catalog for a TeamGames store and caches the last response.
 * <p>
 * <strong>Thread-safety:</strong> Instances are safe for concurrent use as methods are synchronized.
 * Cache data is shared per instance; create separate instances per API key if required.
 * </p>
 *
 * @since 1.1.0
 */
public class StoreCatalog {

    private static final String ENDPOINT_URL = "api/v2/client/global/products";
    public static final long DEFAULT_CACHE_TTL_MS = 60_000; // 1 minute

    private static final Gson GSON = new Gson();

    private String apiKey;
    private long cacheTtlMs = DEFAULT_CACHE_TTL_MS;

    private transient CatalogResponse cachedResponse;
    private transient long cacheTimestamp;
    private transient String cachedKeySignature;

    public StoreCatalog setApiKey(String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API key must not be null or blank.");
        }
        this.apiKey = apiKey;
        return this;
    }

    /**
     * Override the cache TTL (in milliseconds). Defaults to 60 seconds.
     */
    public StoreCatalog setCacheTtlMs(long cacheTtlMs) {
        if (cacheTtlMs < 0) {
            throw new IllegalArgumentException("Cache TTL must be non-negative.");
        }
        this.cacheTtlMs = cacheTtlMs;
        return this;
    }

    /**
     * Retrieves the current catalog for the API key's game server, using a cached
     * response when it is still fresh.
     *
     * @return CatalogResponse containing products and status information.
     * @throws Exception if the request fails.
     */
    public synchronized CatalogResponse fetch() throws Exception {
        return fetchInternal(null);
    }

    public synchronized CatalogResponse fetchByCategory(int categoryId) throws Exception {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("categoryId must be greater than zero.");
        }
        return fetchInternal(Integer.valueOf(categoryId));
    }

    /**
     * Asynchronous variant of {@link #fetch()} that runs on the shared store executor.
     */
    public CompletableFuture<CatalogResponse> fetchAsync() {
        return fetchAsync(Thread.executor);
    }

    public CompletableFuture<CatalogResponse> fetchAsync(Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return fetch();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }, executor);
    }

    public CompletableFuture<CatalogResponse> fetchByCategoryAsync(int categoryId) {
        return fetchByCategoryAsync(categoryId, Thread.executor);
    }

    public CompletableFuture<CatalogResponse> fetchByCategoryAsync(int categoryId, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return fetchByCategory(categoryId);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }, executor);
    }

    private CatalogResponse fetchInternal(Integer categoryId) throws Exception {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("API key must be set before fetching the catalog.");
        }

        String categoryKey = categoryId == null ? "ALL" : ("CATEGORY-" + categoryId);
        String keySignature = Base64.getEncoder().encodeToString(apiKey.getBytes(StandardCharsets.UTF_8)) + '|' + categoryKey;
        long now = System.currentTimeMillis();

        if (cachedResponse != null
                && keySignature.equals(cachedKeySignature)
                && (cacheTtlMs == 0 || now - cacheTimestamp < cacheTtlMs)) {
            return cachedResponse;
        }

        String payload = categoryId == null ? "{}" : GSON.toJson(new CatalogRequest(categoryId));
        String response = JsonPost.send(payload, ENDPOINT_URL, apiKey);
        CatalogResponse parsed = GSON.fromJson(response, CatalogResponse.class);

        cachedResponse = parsed;
        cacheTimestamp = now;
        cachedKeySignature = keySignature;

        return parsed;
    }

    private static final class CatalogRequest {
        @SuppressWarnings("unused")
        final int categoryId;

        CatalogRequest(int categoryId) {
            this.categoryId = categoryId;
        }
    }

    public static class CatalogResponse {
        public final String message;
        public final String extendedMessage;
        public final Product[] products;

        public CatalogResponse(String message, String extendedMessage, Product[] products) {
            this.message = message;
            this.extendedMessage = extendedMessage;
            this.products = products;
        }
    }

    public static class Product {
        public final long id;
        public final String productId;
        public final String name;
        public final double price;
        public final int quantity;
        public final String description;
        public final String image;
        public final boolean disabled;
        public final Sale[] sales;

        public Product(long id, String productId, String name, double price, int quantity,
                       String description, String image, boolean disabled, Sale[] sales) {
            this.id = id;
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.description = description;
            this.image = image;
            this.disabled = disabled;
            this.sales = sales;
        }
    }

    public static class Sale {
        public final String saleType;
        public final double discountAmount;
        public final double discountPercentage;

        public Sale(String saleType, double discountAmount, double discountPercentage) {
            this.saleType = saleType;
            this.discountAmount = discountAmount;
            this.discountPercentage = discountPercentage;
        }
    }
}
