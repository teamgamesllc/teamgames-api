package com.teamgames.endpoints.store;

import java.util.LinkedHashMap;
import java.util.Map;

import com.teamgames.https.Post;
import com.teamgames.lib.gson.Gson;

/**
 * @author Nelson
 */

public class Transaction {
	
	private final static String ENDPOINT_URL = "api/v3/store/transaction/update";

	/**
	 * These variables represent the JSON response that is sent from EverythingRS
	 */

	public String player_name;
	public int product_id;
	public int product_amount;
	public int amount_purchased;
	public String product_name;
	public double product_price;
	public String message;
	private String apiKey;
    private String playerName;
	
	public Transaction() {
		
	}

    // Method for setting game mode
    public Transaction setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    // Method for setting player name
    public Transaction setPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    /**
	 * Returns an array which contains all the players donated items. If the player
	 * has not made a purchase or has already claimed their items, the array will be empty.
	 * 
	 * @param secret
	 * @param playerName
	 * @return
	 * @throws Exception
	 */

	public Transaction[] getTransactions() throws Exception {
		Map<String, Object> params = new LinkedHashMap<>();
        params.put("playerName", playerName);
        Gson gson = new Gson();
        String endpoint = ENDPOINT_URL;
        String request = Post.sendPostData(params, endpoint, apiKey);
        Transaction[] transactions = gson.fromJson(request, Transaction[].class);
        return transactions;
//		return Get.connection("https://api.teamgames.io.everythingrs.com/api/donate/process/" + playerName + "/" + secret);
	}
}
