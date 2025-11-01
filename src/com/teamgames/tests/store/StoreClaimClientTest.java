package com.teamgames.tests.store;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.teamgames.endpoints.store.StoreClaimClient;
import com.teamgames.endpoints.store.Transaction;
import com.teamgames.https.JsonPost;
import com.teamgames.https.Post;

/**
 * Manual smoke test for StoreClaimClient. Replace the fake executor and responses with
 * mocked HTTP clients when integrating into a proper unit test suite.
 */
public class StoreClaimClientTest {

    public static void main(String[] args) throws Exception {
        String apiKey = System.getenv().getOrDefault("TEAMGAMES_API_KEY", "test-key");
        String player = System.getenv().getOrDefault("TEAMGAMES_PLAYER_NAME", "Player123");

        Executor executor = Executors.newSingleThreadExecutor();
        StoreClaimClient client = new StoreClaimClient(apiKey, executor);

        // Synchronous call (would hit the real API in integration tests).
        try {
          // Force the next request on this thread to target the local base URL.
          JsonPost.setLocal(true);   // for JSON-based endpoints (catalog, checkout, v4 claim)
          Post.setLocal(true);       // for form-encoded legacy endpoints (v3 claim)

          Transaction.ClaimResponse response = client.newRequest()
                .playerName(player)
                .useV4Endpoint()
                .preview(true)
                .execute();
            handleResponse("Sync", response);
        } catch (Exception ex) {
            System.out.println("Sync call failed (expected in mock environment): " + ex.getMessage());
        }

        // Async call demonstration.
        JsonPost.setLocal(true);
        Post.setLocal(true);
        client.newRequest()
            .playerName(player)
            .useV4Endpoint()
            .preview(true)
            .executeAsync()
            .thenAccept(response -> {
            handleResponse("Async", response);
        }).exceptionally(ex -> {
            System.out.println("Async call failed (expected in mock environment): " + ex.getMessage());
            return null;
        }).join();
    }

    private static void handleResponse(String label, Transaction.ClaimResponse response) {
        if (response == null) {
            System.out.println(label + " call returned a null response.");
            return;
        }

        if (response.data == null || response.data.claims == null || response.data.claims.length == 0) {
            System.out.println(label + " call: " + response.code + " - " + response.message);
            return;
        }

        System.out.println(label + " call status: " + response.status + " (" + response.code + ")");
        System.out.println(label + " call returned " + response.data.claims.length + " deliverable item(s):");
        for (Transaction tx : response.data.claims) {
            System.out.printf("  • product %d x %d for player %s%n", tx.product_id, tx.product_amount, tx.player_name);
        }
        System.out.println("Grant items in-game and mark them as delivered.");
    }
}
