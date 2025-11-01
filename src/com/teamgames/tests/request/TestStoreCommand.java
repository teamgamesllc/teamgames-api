package com.teamgames.tests.request;

import com.teamgames.endpoints.store.Transaction;
import com.teamgames.endpoints.store.StoreClaimClient;

/**
 * @author Nelson A class that handles the "claim" command for players in a
 *         gaming server. This implementation includes rate limiting and request
 *         queuing to prevent abuse and ensure efficient processing of player
 *         commands.
 */
public class TestStoreCommand {

	public static void main(String[] args) {

		String apiKey = "api-key";
		String playerName = "Nelson"; // Example: c.getUsername(), c.playerName(), c.getDisplayName()

//		new java.lang.Thread() {
//			public void run() {
//				try {
//					Transaction[] transactions = new Transaction().setApiKey(apiKey).setPlayerName(playerName)
//							.getTransactions();
//					if (transactions.length == 0) {
//						System.out.println("No transactions found");
//						return;
//					}
//					if (transactions[0].message != null) {
//						System.out.println(transactions[0].message);
//						return;
//					}
//					for (Transaction transaction : transactions) {
//						System.out.println(transaction.product_id);
//						System.out.println(transaction.quantity_to_grant);
//					}
//					System.out.println("Thank you for donating!");
//				} catch (Exception e) {
//					System.out.println("Api Services are currently offline. Please check back shortly");
//					e.printStackTrace();
//				}
//			}
//		}.start();

		/**
		 * Processes the "claim" command for a player. This command allows a player to
		 * claim purchased items from the store. It is subject to rate limiting to
		 * prevent server overload and includes measures to handle concurrent requests
		 * and API service availability.
		 */
		final long ALLOWED_REQUESTS_PER_LIMIT = 10;
		final long LIMIT_IN_MINUTES = 1;

		// Check if the request is allowed under the current rate limiting rules.
		// This prevents a player from spamming the game server, ensuring the server
		// is not overloaded by excessive requests.
		if (!com.teamgames.util.RateLimiter.isRequestAllowed(playerName, ALLOWED_REQUESTS_PER_LIMIT,
				LIMIT_IN_MINUTES)) {
			System.out.println("Too many requests! Please try again in a few seconds.");
			return;
		}

		// Ensure that a new request is not processed for the user if the previous one
		// is still ongoing. This maintains a fair and orderly processing of requests.
		if (!com.teamgames.util.RequestQueue.getRequestQueue().startRequest(playerName)) {
			System.out.println("Your previous request is still processing. Please wait.");
			return;
		}

        StoreClaimClient transactionClient = new StoreClaimClient(apiKey);

        java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            try {
                return transactionClient.newRequest()
                        .playerName(playerName)
                        .useV4Endpoint()
                        .execute();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }, com.teamgames.util.Thread.executor).thenAcceptAsync(response -> {
 	    String completedMessage = null;
 	    boolean processTransaction = false;

	    if (response == null) {
 		completedMessage = "An error occurred while processing your request.";
	    } else if (response.data == null || response.data.claims == null || response.data.claims.length == 0) {
		completedMessage = response.message != null && !response.message.isEmpty()
			? response.message
			: "You currently don't have any items waiting. You must make a purchase first!";
	    } else if (!"SUCCESS".equals(response.status)) {
		completedMessage = response.message != null ? response.message : "Unable to fulfill the request.";
	    } else {
		completedMessage = "Thank you for supporting the server!";
		processTransaction = true;
 	    }

	    if (processTransaction) {
	        for (Transaction transaction : response.data.claims) {
	            System.out.println("Adding item to inventory: ID=" + transaction.product_id + ", Amount=" + transaction.quantity_to_grant);
	        }
 	    }

		    if (completedMessage != null) {
		        System.out.println(completedMessage);
		    }
		}).exceptionally(e -> {
		    System.out.println("An unexpected error occurred: " + e.getMessage());
		    e.printStackTrace();
		    return null;
		}).whenComplete((result, throwable) -> {
		    com.teamgames.util.RequestQueue.getRequestQueue().finishRequest(playerName);
		});
	}
}
