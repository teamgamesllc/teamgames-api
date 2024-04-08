package com.teamgames.tests.request;

import com.teamgames.util.RateLimiter;
import com.teamgames.util.RequestQueue;
import com.teamgames.util.Thread;
import com.teamgames.vote.RewardService;
import com.teamgames.vote.Vote;

import java.util.concurrent.CompletableFuture;

public class TestVoteCommand {

	public static void main(String mainArgs[]) {
		String playerCommand = "reward 123 1";
		String playerName = "REPLACE_WITH_PLAYER_NAME"; // Example: c.getUsername(), c.playerName(), c.getDisplayName()

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

			if (!RateLimiter.isRequestAllowed(playerName, ALLOWED_REQUESTS_PER_LIMIT, LIMIT_IN_MINUTES)) {
				System.out.println("Too many requests! Please try again in a few seconds.");
				return;
			}

			if (!RequestQueue.getRequestQueue().startRequest(playerName)) {
				System.out.println("Your previous request is still processing. Please wait.");
				return;
			}

			CompletableFuture.supplyAsync(() -> {
				try {
					return Vote.reward("secret_key", playerName, id, amount);
				} catch (Exception e) {
					System.out.println("API Services are currently offline. Please check back shortly.");
					e.printStackTrace();
					return null;
				}
			}, Thread.executor).thenAcceptAsync(rewards -> {
				String completedMessage;
				final int itemIds = rewards[0].reward_id;
				final int rewardAmount = rewards[0].reward_amount;
				RewardService rewardService = rewards[0].rewardService;
				int votePoints = 0;
				for (Vote reward : rewards) {
					if (reward == null) continue;
					if (reward.message == null) {
						rewardService.rewards.put(itemIds, rewardAmount);
						votePoints += reward.vote_points;
					}
				}
				completedMessage = "Thank you for voting! You now have " + votePoints + " vote points.";
				System.out.println(completedMessage);
			}).exceptionally(e -> {
				System.out.println("An unexpected error occurred: " + e.getMessage());
				e.printStackTrace();
				return null;
			}).whenComplete((result, throwable) -> RequestQueue.getRequestQueue().finishRequest(playerName));
		}
	}

}
