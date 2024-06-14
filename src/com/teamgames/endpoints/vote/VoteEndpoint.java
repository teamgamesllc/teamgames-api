package com.teamgames.endpoints.vote;

import com.teamgames.endpoints.vote.obj.ClaimReward;
import com.teamgames.https.Post;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import com.teamgames.lib.gson.Gson;

/**
 * Provides access to the Vote API for TeamGames, facilitating the claiming of vote rewards.
 */
public class VoteEndpoint {
    private volatile String apiKey;
    private volatile String playerName;  // volatile to ensure visibility across threads
    private volatile String rewardId;
    private volatile String amount;

    public VoteEndpoint() {}

    public synchronized VoteEndpoint setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public synchronized VoteEndpoint setPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public synchronized VoteEndpoint setRewardId(String rewardId) {
        this.rewardId = rewardId;
        return this;
    }

    public synchronized VoteEndpoint setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Retrieves a {@link ClaimReward} object representing the reward for a player.
     * This method calls the TeamGames Vote API to validate the player's vote and claim the designated reward.
     *
     * @return A {@link ClaimReward} object populated with the reward details from the API response.
     * @throws Exception If there is an error in the HTTP post request or during the parsing of the response.
     *                   This could be due to network issues, incorrect parameters, or server-side errors.
     */
    public final ClaimReward[] getReward() throws Exception {
        final ConcurrentHashMap<String, Object> params = new ConcurrentHashMap<>();
        
        params.put("player", playerName);
        params.put("reward", rewardId);
        params.put("amount", amount);

        final Gson gson = new Gson();
        final String endpoint = "v3/vote/reward/claim";
        final String response = Post.sendPostData(params, endpoint, apiKey);
        System.out.println("The response is " + response);
        final ClaimReward[] reward = gson.fromJson(response, ClaimReward[].class);
        System.out.println(Arrays.toString(reward));
        return reward;
    }
}
