# teamgames-api

Usage:

Store Command:
```
		String playerCommand = "claim";
		String playerName = "REPLACE_WITH_PLAYER_NAME"; // Example: c.getUsername(), c.playerName(), c.getDisplayName()

		/**
		 * Processes the "claim" command for a player. This command allows a player to
		 * claim purchased items from the store. It is subject to rate limiting to
		 * prevent server overload and includes measures to handle concurrent requests
		 * and API service availability.
		 */
		if (playerCommand.equalsIgnoreCase("claim")) {
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

			// Asynchronously process the claim command to avoid blocking the main thread.
			CompletableFuture.supplyAsync(() -> {
				try {
					// Attempt to fetch transactions for the player from the store API.
					return com.teamgames.store.Transaction.getTransactions("secret_key", playerName);
				} catch (Exception e) {
					// Handle exceptions by notifying the player and logging the error for
					// investigation.
					System.out.println("API Services are currently offline. Please check back shortly.");
					e.printStackTrace();
					return null;
				}
			}, com.teamgames.util.Thread.executor).thenAcceptAsync(transactions -> {
				String completedMessage = null;
				boolean processTransaction = false;

				if (transactions == null) {
					completedMessage = "An error occurred while processing your request.";
				} else if (transactions.length == 0) {
					completedMessage = "You currently don't have any items waiting. You must make a purchase first!";
				} else if (transactions[0].message != null) {
					completedMessage = transactions[0].message;
				} else {
					completedMessage = "Thank you for supporting the server!";
					processTransaction = true;
				}

				// Thread safety is ensured here by using the synchronized block.
				// The synchronized block is placed after the network requests for efficiency.
				synchronized (c) {
					if (processTransaction) {
						// Add the purchased items to the player's inventory.
						for (com.teamgames.store.Transaction transaction : transactions) {
							// Items would be added to the player's inventory here based on the transaction
							// details.
							c.getItems().addItem(transaction.product_id, transaction.product_amount);
						}
					}

					if (completedMessage != null) {
						System.out.println(completedMessage);
					}
				}
			}).exceptionally(e -> {
				// Handle any exceptions that occurred during the process and log them for
				// debugging.
				System.out.println("An unexpected error occurred: " + e.getMessage());
				e.printStackTrace();
				return null;
			}).whenComplete((result, throwable) -> {
				// Mark the request as completed in the request queue, regardless of success or
				// failure.
				// This ensures proper tracking and management of player requests.
				com.teamgames.util.RequestQueue.getRequestQueue().finishRequest(playerName);
			});
		}
	}
```

Vote Command:

```
		String playerName = "REPLACE_WITH_PLAYER_NAME"; // Example: c.getUsername(), c.playerName(), c.getDisplayName()

		/**
		 * Processes the "reward" command for a player. This command allows a player to claim rewards, for example,
		 * for voting for the server. It includes rate limiting and handles concurrent requests and API service availability.
		 */
		if (playerCommand.startsWith("reward")) {
		    final long ALLOWED_REQUESTS_PER_LIMIT = 10;
		    final long LIMIT_IN_MINUTES = 1;
		    
		    String[] args = playerCommand.split(" ");
		    if (args.length < 2) {
		        System.out.println("Please use [::reward id], [::reward id amount], or [::reward id all].");
		        return;
		    }
		    final String id = args[1];
		    final String amount = args.length == 3 ? args[2] : "1";
		    
		    if (!com.teamgames.util.RateLimiter.isRequestAllowed(playerName, ALLOWED_REQUESTS_PER_LIMIT, LIMIT_IN_MINUTES)) {
		        System.out.println("Too many requests! Please try again in a few seconds.");
		        return;
		    }
		    
		    if (!com.teamgames.util.RequestQueue.getRequestQueue().startRequest(playerName)) {
		        System.out.println("Your previous request is still processing. Please wait.");
		        return;
		    }
		    
		    java.util.concurrent.CompletableFuture.supplyAsync(() -> {
		        try {
		            // Assuming the reward method returns an array of Vote objects similar to the transactions example.
		            return com.teamgames.vote.Vote.reward("secret_key", playerName, id, amount);
		        } catch (Exception e) {
		            System.out.println("API Services are currently offline. Please check back shortly.");
		            e.printStackTrace();
		            return null;
		        }
		    }, com.teamgames.util.Thread.executor).thenAcceptAsync(rewards -> {
		        String completedMessage = null;
		        boolean processReward = false;
		        
		        if (rewards == null) {
		            completedMessage = "An error occurred while processing your request.";
		        } else if (rewards.length == 0) {
		            completedMessage = "No rewards found for the given parameters.";
		        } else if (rewards[0].message != null) {
		            completedMessage = rewards[0].message;
		        } else {
		            completedMessage = "Thank you for voting! You now have " + rewards[0].vote_points + " vote points.";
		            processReward = true;
		        }
		        
		        synchronized (c) {
		            if (processReward) {
		                // Add the reward to the player's inventory here, based on reward details.
		                c.getItems().addItem(rewards[0].reward_id, rewards[0].give_amount);
		            }
		            
		            if (completedMessage != null) {
		                System.out.println(completedMessage);
		            }
		        }
		    }).exceptionally(e -> {
		        System.out.println("An unexpected error occurred: " + e.getMessage());
		        e.printStackTrace();
		        return null;
		    }).whenComplete((result, throwable) -> {
		        com.teamgames.util.RequestQueue.getRequestQueue().finishRequest(playerName);
		    });
		}
```

Hiscore API (Soon to be deprecated)
```
CompletableFuture.runAsync(() -> {
			try {
				com.teamgames.leaderboard.Leaderboard.update("secret_key", "Normal Mode", playerName, playerRights,
						playerXP, debugMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, Executors.newSingleThreadExecutor()).whenComplete((result, throwable) -> {
			if (throwable != null) {
				System.err.println("An error occurred during the leaderboard update: " + throwable.getMessage());
				throwable.printStackTrace();
			}
		});
		
```
