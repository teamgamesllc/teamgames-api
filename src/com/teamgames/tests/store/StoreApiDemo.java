package com.teamgames.tests.store;

import java.util.ArrayList;
import java.util.List;

import com.teamgames.endpoints.store.StoreCatalog;
import com.teamgames.endpoints.store.StoreCatalog.CatalogResponse;
import com.teamgames.endpoints.store.StoreCatalog.Product;
import com.teamgames.endpoints.store.StoreCatalogClient;
import com.teamgames.endpoints.store.StoreCheckoutClient;
import com.teamgames.endpoints.store.StoreCheckout.CheckoutResponse;

/**
 * Convenience entry point for exercising the catalog + checkout flow.
 *
 * Usage:
 *   export TEAMGAMES_API_KEY=your_api_key
 *   # Optional: export TEAMGAMES_RUN_CHECKOUT=true to actually submit a checkout
 *   javac com/teamgames/tests/store/StoreApiDemo.java  (or build via your IDE)
 *   java com.teamgames.tests.store.StoreApiDemo
 */
public class StoreApiDemo {

    public static void main(String[] args) throws Exception {
        String apiKey = "Test";
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("TEAMGAMES_API_KEY environment variable is not set.");
        }

        boolean runCheckout = "true".equalsIgnoreCase(System.getenv("TEAMGAMES_RUN_CHECKOUT"));

        System.out.println("Fetching catalog…");
        StoreCatalogClient catalogClient = new StoreCatalogClient(apiKey);
        CatalogResponse catalog = catalogClient.fetch();

        if (!"SUCCESS".equalsIgnoreCase(catalog.message)) {
            System.out.println("Catalog response:");
            System.out.println("  message          = " + catalog.message);
            System.out.println("  extendedMessage  = " + catalog.extendedMessage);
        }

        if (catalog.products == null || catalog.products.length == 0) {
            System.out.println("No products returned for this API key.");
            return;
        }

        System.out.println("Found " + catalog.products.length + " product(s).");
        for (int i = 0; i < Math.min(5, catalog.products.length); i++) {
            Product product = catalog.products[i];
            System.out.println("  • " + product.id + " :: " + product.name + " :: $" + product.price);
        }

        StoreCheckoutClient checkoutClient = new StoreCheckoutClient(apiKey);
        StoreCheckoutClient.CheckoutRequest checkout = checkoutClient.newRequest()
            .username("Player123");

        // Prepare cart selections (up to 3 items or the catalog size if smaller).
        List<long[]> selections = new ArrayList<>();
        for (int i = 0; i < catalog.products.length && i < 3; i++) {
            long productId = catalog.products[i].id;
            int quantity = i + 1; // arbitrary example quantity
            selections.add(new long[]{productId, quantity});
        }

        for (long[] selection : selections) {
            checkout.addItem(selection[0], (int) selection[1]);
        }

        String cartJson = checkout.buildCartItemsJson();
        System.out.println("Prepared cartItems payload: " + cartJson);

        if (!runCheckout) {
            System.out.println("Skipping checkout submit (set TEAMGAMES_RUN_CHECKOUT=true to execute).");
            return;
        }

        System.out.println("Submitting checkout…");
        CheckoutResponse response = checkout.submit();
        System.out.println("Checkout status: " + response.statusCode + " / " + response.status);
        if (response.message != null) {
            System.out.println("Message: " + response.message);
        }
        if (response.redirect != null) {
            System.out.println("Redirect URL: " + response.redirect);
        }
        if (response.error != null) {
            System.out.println("Error detail: " + response.error);
        }
    }
}
