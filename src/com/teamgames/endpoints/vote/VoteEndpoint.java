package com.teamgames.endpoints.vote;

import java.util.LinkedHashMap;
import java.util.Map;

import com.teamgames.https.Post;
import com.teamgames.lib.gson.Gson;
import com.teamgames.lib.gson.TypeToken;
import com.teamgames.endpoints.vote.obj.ClaimReward;

/**
 * @author Nelson Sanchez
 * Provides access to the Vote API for TeamGames, facilitating the claiming of vote rewards.
 * This endpoint is specifically used for interfacing with the TeamGames system to validate
 * and claim rewards for players based on their voting actions.
 */
public class VoteEndpoint {
	
	// TODO Authorization Headers for Endpoints.
    
    /**
     * Retrieves a {@link ClaimReward} object representing the reward for a player.
     * This method calls the TeamGames Vote API to validate the player's vote and claim the designated reward.
     * 
     * @param secret      The secret key for authentication with the TeamGames Vote API.
     * @param playerName  The name of the player claiming the reward.
     * @param id          The identifier of the specific reward to claim.
     * @param amount      The amount of the reward to be claimed, usually determined by vote points or similar metrics.
     * @return            A {@link ClaimReward} object populated with the reward details from the API response.
     * @throws Exception  If there is an error in the HTTP post request or during the parsing of the response.
     *                    This could be due to network issues, incorrect parameters, or server-side errors.
     */
    public static ClaimReward getReward(String secret, String playerName, String id, String amount) throws Exception {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("secret", secret);
        params.put("player", playerName);
        params.put("reward", id);
        params.put("amount", amount);
        return new Gson().fromJson(Post.sendPostData(params, "api/vote/validate"), new TypeToken<ClaimReward[]>().getType());
    }
}
