package com.teamgames.endpoints.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.teamgames.https.JsonPost;
import com.teamgames.lib.gson.Gson;
import com.teamgames.lib.gson.SerializedName;
import com.teamgames.util.Thread;

/**
 * Submits carts to the TeamGames checkout endpoint using the API key integration.
 * <p>
 * <strong>Thread-safety:</strong> Instances are <em>not</em> thread-safe. Create a fresh
 * instance per checkout. After {@link #submit()} or {@link #submitAsync()} completes,
 * the instance is considered single-use and must not be reused.
 * </p>
 *
 * @since 1.1.0
 */
public class StoreCheckout {

    private static final String ENDPOINT_URL = "api/v2/client/global/checkout/complete";

    private static final Gson GSON = new Gson();
    private final List<CartItem> cartItems = new ArrayList<>();
    private boolean submitted = false;

    private String apiKey;
    private String username;

    public StoreCheckout setApiKey(String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API key must not be null or blank.");
        }
        this.apiKey = apiKey;
        return this;
    }

    public StoreCheckout setUsername(String username) {
        ensureNotSubmitted();
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not be null or blank.");
        }
        this.username = username;
        return this;
    }

    public StoreCheckout addItem(long productId, int quantity) {
        ensureNotSubmitted();
        if (productId <= 0) {
            throw new IllegalArgumentException("Product id must be a positive number.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (quantity > 1_000_000) {
            throw new IllegalArgumentException("Quantity is too large.");
        }
        cartItems.add(new CartItem(productId, quantity));
        return this;
    }

    public StoreCheckout clearItems() {
        ensureNotSubmitted();
        cartItems.clear();
        return this;
    }

    /**
     * @return JSON string representation of the cart items array.
     */
    public String buildCartItemsJson() {
        ensureNotSubmitted();
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty. Add items before building JSON.");
        }
        List<CartItem> snapshot = new ArrayList<>(cartItems);
        return GSON.toJson(snapshot);
    }

    /**
     * Submits the cart to the checkout endpoint and returns the response payload.
     *
     * @return CheckoutResponse containing redirect information and status codes.
     * @throws Exception if the request fails or the payload is invalid.
     */
    public CheckoutResponse submit() throws Exception {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("API key must be set before submitting checkout.");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalStateException("Username must be set before submitting checkout.");
        }
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot submit checkout with an empty cart.");
        }
        ensureNotSubmitted();
        List<CartItem> snapshot = new ArrayList<>(cartItems);

        Map<String, Object> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("cartItems", GSON.toJson(snapshot));

        String requestBody = GSON.toJson(payload);
        String response = JsonPost.send(requestBody, ENDPOINT_URL, apiKey);
        submitted = true;
        cartItems.clear();
        return GSON.fromJson(response, CheckoutResponse.class);
    }

    /**
     * Asynchronous variant of {@link #submit()} that runs on the shared store executor.
     */
    public CompletableFuture<CheckoutResponse> submitAsync() {
        return submitAsync(Thread.executor);
    }

    public CompletableFuture<CheckoutResponse> submitAsync(java.util.concurrent.Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return submit();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }, executor);
    }

    private void ensureNotSubmitted() {
        if (submitted) {
            throw new IllegalStateException("StoreCheckout instances are single-use. Create a new instance for each cart.");
        }
    }

    private static class CartItem {
        final long id;
        final int quantity;

        CartItem(long id, int quantity) {
            this.id = id;
            this.quantity = quantity;
        }
    }

    public static class CheckoutResponse {
        public final String statusCode;
        public final String status;
        public final String message;
        public final String extendedMessage;
        public final String redirect;
        public final String transactionId;
        public final boolean isFree;

        public final CartValidationItem[] cartItems;
        public final ProductSnapshot[] productsAffected;
        public final String error;

        public CheckoutResponse(String statusCode, String status, String message, String extendedMessage,
                                String redirect, String transactionId, boolean isFree,
                                CartValidationItem[] cartItems, ProductSnapshot[] productsAffected,
                                String error) {
            this.statusCode = statusCode;
            this.status = status;
            this.message = message;
            this.extendedMessage = extendedMessage;
            this.redirect = redirect;
            this.transactionId = transactionId;
            this.isFree = isFree;
            this.cartItems = cartItems;
            this.productsAffected = productsAffected;
            this.error = error;
        }
    }

    public static class CartValidationItem {
        public final long product;
        public final int quantity;
        public final double payWhatYouWantPrice;
        public final long cart;
        public final long gameServer;
        public final long owner;

        public CartValidationItem(long product, int quantity, double payWhatYouWantPrice,
                                  long cart, long gameServer, long owner) {
            this.product = product;
            this.quantity = quantity;
            this.payWhatYouWantPrice = payWhatYouWantPrice;
            this.cart = cart;
            this.gameServer = gameServer;
            this.owner = owner;
        }
    }

    public static class ProductSnapshot {
        public final long id;
        public final String productId;
        public final String name;
        public final double price;
        public final int stock;
        public final boolean hasStock;
        public final boolean unlimitedStock;
        public final boolean disabled;
        @SerializedName("saleType")
        public final String saleType;
        public final double discountAmount;
        public final double discountPercentage;

        public ProductSnapshot(long id, String productId, String name, double price, int stock,
                               boolean hasStock, boolean unlimitedStock, boolean disabled,
                               String saleType, double discountAmount, double discountPercentage) {
            this.id = id;
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.stock = stock;
            this.hasStock = hasStock;
            this.unlimitedStock = unlimitedStock;
            this.disabled = disabled;
            this.saleType = saleType;
            this.discountAmount = discountAmount;
            this.discountPercentage = discountPercentage;
        }
    }
}
