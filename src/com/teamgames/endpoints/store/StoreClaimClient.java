package com.teamgames.endpoints.store;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.teamgames.util.Thread;

/**
 * Thread-safe facade for creating transaction claim requests.
 *
 * @since 1.2.0
 */
public final class StoreClaimClient {

    private final String apiKey;
    private final Executor defaultExecutor;

    public StoreClaimClient(String apiKey) {
        this(apiKey, Thread.executor);
    }

    public StoreClaimClient(String apiKey, Executor defaultExecutor) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API key must not be null or blank.");
        }
        if (defaultExecutor == null) {
            throw new IllegalArgumentException("Executor must not be null.");
        }
        this.apiKey = apiKey;
        this.defaultExecutor = defaultExecutor;
    }

    public ClaimRequest newRequest() {
        return new ClaimRequest();
    }

    public final class ClaimRequest {
        private final Transaction delegate = new Transaction().setApiKey(apiKey);
        private boolean executed = false;
        private boolean preview;
        private boolean includeRawTransactions;

        private void ensureNotExecuted() {
            if (executed) {
                throw new IllegalStateException("ClaimRequest instances are single-use.");
            }
        }

        public ClaimRequest playerName(String playerName) {
            ensureNotExecuted();
            delegate.setPlayerName(playerName);
            return this;
        }

        public ClaimRequest useV4Endpoint() {
            ensureNotExecuted();
            delegate.useV4Endpoint();
            return this;
        }

        public ClaimRequest preview(boolean preview) {
            ensureNotExecuted();
            this.preview = preview;
            return this;
        }

        public ClaimRequest includeRawTransactions(boolean includeRawTransactions) {
            ensureNotExecuted();
            this.includeRawTransactions = includeRawTransactions;
            return this;
        }

        public Transaction.ClaimResponse execute() throws Exception {
            ensureNotExecuted();
            executed = true;
            delegate.setPreview(preview);
            delegate.setIncludeRawTransactions(includeRawTransactions);
            return delegate.execute();
        }

        public CompletableFuture<Transaction.ClaimResponse> executeAsync() {
            return executeAsync(defaultExecutor);
        }

        public CompletableFuture<Transaction.ClaimResponse> executeAsync(Executor executor) {
            ensureNotExecuted();
            executed = true;
            delegate.setPreview(preview);
            delegate.setIncludeRawTransactions(includeRawTransactions);
            return delegate.executeAsync(executor);
        }
    }
}
