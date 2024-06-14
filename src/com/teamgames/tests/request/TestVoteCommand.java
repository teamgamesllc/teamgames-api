package com.teamgames.tests.request;

import com.teamgames.endpoints.vote.VoteEndpoint;
import com.teamgames.endpoints.vote.obj.ClaimReward;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestVoteCommand {
    public static void main(String[] args) {
        final VoteEndpoint endpoint = new VoteEndpoint().setApiKey("api-key").setPlayerName("Test").setRewardId("1").setAmount("all");

        ClaimReward[] rewards;
		try {
			rewards = endpoint.getReward();
	        if (rewards != null) {
	            for (ClaimReward reward : rewards) {
	            	System.out.println(reward.totalVotes);
	            	System.out.println(reward.votesMonth);
	            	System.out.println(reward.votePoints);
	            }
	        } else {
	            System.out.println("No rewards fetched.");
	        }
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
//            try {
//                return endpoint.getReward();
//            } catch (Exception e) {
//                System.err.println("Error fetching rewards: " + e.getMessage());
//                e.printStackTrace();
//                return null;
//            }
//        }).thenAccept(rewards -> {
//            if (rewards != null) {
//                for (ClaimReward reward : rewards) {
//                    System.out.println(reward.getMessage());
//                }
//            } else {
//                System.out.println("No rewards fetched.");
//            }
//        });
//
//        try {
//            future.get(); 
//        } catch (InterruptedException | ExecutionException e) {
//            System.err.println("Error waiting for future completion: " + e.getMessage());
//            e.printStackTrace();
//        }
    }
}
