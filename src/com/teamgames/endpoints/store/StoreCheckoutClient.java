package com.teamgames.endpoints.store;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.teamgames.util.Thread;

/**
 * Thread-safe facade for creating single-use checkout requests.
 *
 * @since 1.2.0
 */
public final class StoreCheckoutClient {

    private final String apiKey;
    private final Executor defaultExecutor;

    public StoreCheckoutClient(String apiKey) {
        this(apiKey, Thread.executor);
    }

    public StoreCheckoutClient(String apiKey, Executor defaultExecutor) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API key must not be null or blank.");
        }
        if (defaultExecutor == null) {
            throw new IllegalArgumentException("Executor must not be null.");
        }
        this.apiKey = apiKey;
        this.defaultExecutor = defaultExecutor;
    }

    public CheckoutRequest newRequest() {
        return new CheckoutRequest();
    }

    public final class CheckoutRequest {
        private final StoreCheckout delegate = new StoreCheckout().setApiKey(apiKey);
        private boolean executed = false;

        private void ensureNotExecuted() {
            if (executed) {
                throw new IllegalStateException("CheckoutRequest instances are single-use.");
            }
        }

        public CheckoutRequest username(String username) {
            ensureNotExecuted();
            delegate.setUsername(username);
            return this;
        }

        public CheckoutRequest addItem(long productId, int quantity) {
            ensureNotExecuted();
            delegate.addItem(productId, quantity);
            return this;
        }

        public CheckoutRequest clearItems() {
            ensureNotExecuted();
            delegate.clearItems();
            return this;
        }

        public String buildCartItemsJson() {
            ensureNotExecuted();
            return delegate.buildCartItemsJson();
        }

        public StoreCheckout.CheckoutResponse submit() throws Exception {
            ensureNotExecuted();
            executed = true;
            return delegate.submit();
        }

        public CompletableFuture<StoreCheckout.CheckoutResponse> submitAsync() {
            ensureNotExecuted();
            executed = true;
            return delegate.submitAsync(defaultExecutor);
        }

        public CompletableFuture<StoreCheckout.CheckoutResponse> submitAsync(Executor executor) {
            ensureNotExecuted();
            executed = true;
            return delegate.submitAsync(executor);
        }
    }
}
