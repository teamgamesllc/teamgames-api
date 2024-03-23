package com.teamgames.tests.request;

public class TestVoteCommand {
	
	public static void main(String mainArgs[]) {
		String playerCommand = "reward 123 1"; 
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
		        
//		        synchronized (c) {
		            if (processReward) {
		                // Add the reward to the player's inventory here, based on reward details.
//		                c.getItems().addItem(rewards[0].reward_id, rewards[0].give_amount);
		            }
		            
		            if (completedMessage != null) {
		                System.out.println(completedMessage);
		            }
//		        }
		    }).exceptionally(e -> {
		        System.out.println("An unexpected error occurred: " + e.getMessage());
		        e.printStackTrace();
		        return null;
		    }).whenComplete((result, throwable) -> {
		        com.teamgames.util.RequestQueue.getRequestQueue().finishRequest(playerName);
		    });
		}
	}

}
