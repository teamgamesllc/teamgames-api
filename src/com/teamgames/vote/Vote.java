package com.teamgames.vote;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.teamgames.lib.gson.Gson;
import com.teamgames.net.HTTPS;
import com.teamgames.net.Post;

/**
 * @author Nelson
 */

public class Vote {

	/**
	 * Handles the reward command on a new thread for better performance
	 */

	public static ExecutorService service = Executors.newFixedThreadPool(1);

	/**
	 * These variables represent the JSON response that is sent from EverythingRS
	 */

	public String total_votes;
	public int vote_points;
	public int votes_month;
	public String username;
	public int give_amount;
	public int reward_id;
	public String reward_name;
	public int reward_amount;
	public String message;

	/**
	 * Returns an array which contains a response from the server containing the
	 * vote rewards
	 * 
	 * @param secret
	 * @param playerName
	 * @return
	 * @throws Exception
	 */

	public static Vote[] reward(String secret, String playerName, String id, String amount) throws Exception {
		String response = Vote.validate(secret, playerName, id, amount);
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

	public static String validate(String secret, String playerName, String id, String amount) throws Exception {
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

	public static String validate(String secret, String playerName, int id) throws Exception {
		return HTTPS.connection("https://everythingrs.com/api/vote/process/" + playerName + "/" + secret + "/" + id);
	}
	
	public static String claimAuth(String secret, String username, String auth) throws Exception {
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("username", username);
		params.put("auth", auth);
		params.put("secret", secret);
		final String response = Post.sendPostData(params, "api/vote/claim-auth");
		System.out.println("response: " + response);
		Auth[] authResponse = new Gson().fromJson(response, Auth[].class);
		return authResponse[0].message;
	}

}
