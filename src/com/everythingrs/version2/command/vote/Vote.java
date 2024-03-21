package com.everythingrs.version2.command.vote;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.everythingrs.lib.gson.Gson;
import com.everythingrs.version2.TeamGamesRequest;
import com.everythingrs.version2.request.GET;
import com.everythingrs.version2.request.Post;

/**
 * @author Genesis
 */

public class Vote extends TeamGamesRequest {

	/**
	 * These variables represent the JSON response that is sent from EverythingRS
	 */

	private String total_votes;
	private int vote_points;
	private int votes_month;
	private String username;
	private int give_amount;
	private int reward_id;
	private String reward_name;
	private int reward_amount;
	private String message;

	/**
	 * Returns an array which contains a response from the server containing the
	 * vote rewards
	 * 
	 * @param secret
	 * @param playerName
	 * @return
	 * @throws Exception
	 */

	public Vote[] reward(String secret, String playerName, String id, String amount) throws Exception {
		String response = validate(secret, playerName, id, amount);
		Gson gson = new Gson();
		Vote[] vote = gson.fromJson(response, Vote[].class);
		return vote;
	}

	/**
	 * Sends a request to the EverythingRS server
	 * 
	 * @param secret
	 * @param playerName
	 * @param id
	 * @param amount
	 * @return
	 */

	public String validate(String secret, String playerName, String id, String amount) throws Exception {
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("secret", secret);
		params.put("player", playerName);
		params.put("reward", id);
		params.put("amount", amount);
		return Post.sendPostData(params, "api/vote/validate");
	}

	/**
	 * Fetches whether or not the player has already voted on EverythingRS.com This
	 * code will soon be deprecated, we now use $_POST as it's better for
	 * scalability. The code below is only supported for members using older
	 * versions of our API and should no longer be used
	 * 
	 * @param secret
	 * @param playerName
	 * @throws Exception
	 */

	public String validate(String secret, String playerName, int id) throws Exception {
		return new GET().fetch("https://everythingrs.com/api/vote/process/" + playerName + "/" + secret + "/" + id);
	}

	public String claimAuth(String secret, String username, String auth) throws Exception {
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("username", username);
		params.put("auth", auth);
		params.put("secret", secret);
		final String response = Post.sendPostData(params, "api/vote/claim-auth");
		System.out.println("response: " + response);
		Auth[] authResponse = new Gson().fromJson(response, Auth[].class);
		return authResponse[0].message;
	}

	/**
	 * @return the total_votes
	 */
	public String getTotalVotes() {
		return total_votes;
	}

	/**
	 * @return the vote_points
	 */
	public int getVotePoints() {
		return vote_points;
	}

	/**
	 * @return the votes_month
	 */
	public int getVotesMonth() {
		return votes_month;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the give_amount
	 */
	public int getAmount() {
		return give_amount;
	}

	/**
	 * @return the reward_id
	 */
	public int getRewardId() {
		return reward_id;
	}

	/**
	 * @return the reward_name
	 */
	public String getRewardName() {
		return reward_name;
	}

	/**
	 * @return the reward_amount
	 */
	public int getRewardAmount() {
		return reward_amount;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	public String getClassName() {
		return "Vote";
	}

}
