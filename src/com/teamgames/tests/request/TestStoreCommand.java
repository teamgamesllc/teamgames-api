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
            }).thenAcceptAsync(transactions -> {
                if (transactions == null) {
                    System.out.println("An error occurred while processing your request.");
                } else if (transactions.length == 0) {
                    System.out.println("You currently don't have any items waiting. You must make a purchase first!");
                } else {
                	com.teamgames.util.Thread.itemAdditionExecutor.submit(() -> {
                        for (com.teamgames.store.Transaction transaction : transactions) {
                            if (transaction.message != null) {
                                System.out.println(transaction.message);
                                return; // Assuming stop processing if any transaction has a message
                            }
                            // Safe execution in a single-thread context
                            // c.getItems().addItem(transaction.product_id, transaction.product_amount);
                        }
                        System.out.println("Thank you for supporting the server!");
                    });
                }
            }).whenComplete((result, throwable) -> {
                com.teamgames.util.RequestQueue.getRequestQueue().finishRequest(playerName);
            });
        }
	}

}
