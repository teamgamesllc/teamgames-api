package com.teamgames.tests.request;

import java.util.concurrent.CompletableFuture;

public class TestStoreCommand {

	public static void main(String[] args) {
		String playerCommand = "claim";
		String playerName = "PLAYER_NAME";

		if (playerCommand.equalsIgnoreCase("claim")) {
			if (!com.teamgames.util.RequestQueue.getRequestQueue().startRequest(playerName)) {
				System.out.println("Your previous request is still processing. Please wait.");
				return;
			}
			CompletableFuture.supplyAsync(() -> {
				try {
					return com.teamgames.store.Transaction.getTransactions("secret_key", playerName);
				} catch (Exception e) {
					System.out.println("API Services are currently offline. Please check back shortly.");
					e.printStackTrace();
					return null;
				}
			}, com.teamgames.util.Thread.executor).thenAcceptAsync(transactions -> {
				if (transactions == null) {
					System.out.println("An error occurred while processing your request.");
				} else if (transactions.length == 0) {
					System.out.println("You currently don't have any items waiting. You must make a purchase first!");
				} else {
					if (transactions[0].message != null) {
						System.out.println(transactions[0].message);
						return;
					}
					//synchronized (c) { // Ensure thread safety.
						for (com.teamgames.store.Transaction transaction : transactions) {
							// c.getItems().addItem(transaction.product_id, transaction.product_amount);
						}
					System.out.println("Thank you for supporting the server!");
					//}
				}
			}).whenComplete((result, throwable) -> {
				com.teamgames.util.RequestQueue.getRequestQueue().finishRequest(playerName);
			});
		}
	}

}
