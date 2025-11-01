package com.teamgames.endpoints.store;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.teamgames.util.Thread;

/**
 * Thread-safe facade around {@link StoreCatalog} that exposes a simple client API.
 *
 * @since 1.2.0
 */
public final class StoreCatalogClient {

    private final StoreCatalog catalog;
    private final Executor defaultExecutor;

    public StoreCatalogClient(String apiKey) {
        this(apiKey, Thread.executor);
    }

    public StoreCatalogClient(String apiKey, Executor defaultExecutor) {
        if (defaultExecutor == null) {
            throw new IllegalArgumentException("Executor must not be null.");
        }
        this.catalog = new StoreCatalog().setApiKey(apiKey);
        this.defaultExecutor = defaultExecutor;
    }

    public StoreCatalogClient setCacheTtlMs(long cacheTtlMs) {
        catalog.setCacheTtlMs(cacheTtlMs);
        return this;
    }

    public StoreCatalog.CatalogResponse fetch() throws Exception {
        return catalog.fetch();
    }

    public StoreCatalog.CatalogResponse fetchByCategory(int categoryId) throws Exception {
        return catalog.fetchByCategory(categoryId);
    }

    public CompletableFuture<StoreCatalog.CatalogResponse> fetchAsync() {
        return catalog.fetchAsync(defaultExecutor);
    }

    public CompletableFuture<StoreCatalog.CatalogResponse> fetchAsync(Executor executor) {
        return catalog.fetchAsync(executor);
    }

    public CompletableFuture<StoreCatalog.CatalogResponse> fetchByCategoryAsync(int categoryId) {
        return catalog.fetchByCategoryAsync(categoryId, defaultExecutor);
    }

    public CompletableFuture<StoreCatalog.CatalogResponse> fetchByCategoryAsync(int categoryId, Executor executor) {
        return catalog.fetchByCategoryAsync(categoryId, executor);
    }
}
