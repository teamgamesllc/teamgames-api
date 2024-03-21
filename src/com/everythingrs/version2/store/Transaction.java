package com.everythingrs.version2.store;

import com.everythingrs.lib.gson.*;
import com.everythingrs.version2.TeamGamesRequest;
import com.everythingrs.version2.request.GET;

/**
 * @author Nelson Sanchez
 */

public class Transaction extends TeamGamesRequest {

	/**
	 * These variables represent the JSON response that is sent from EverythingRS
	 */

	private String player_name;
	private int product_id;
	private int product_amount;
	private int amount_purchased;
	private String product_name;
	private double product_price;
	private String message;

	/**
	 * Fetches whether or not the player has already purchased an item through the
	 * EverythingRS.com API After the player it fetches the result, our backend will
	 * remove it.
	 * 
	 * @param secret
	 * @param playerName
	 * @throws Exception
	 */

	public final String networkValidate(String secret, String playerName) throws Exception {
		return new GET().fetch("https://ersdev.everythingrs.com/api/donate/process/" + playerName + "/" + secret);
	}

	/**
	 * Returns an array which contains all the players donated items. If the player
	 * has not donated or has already claimed their items, the array will be empty.
	 * 
	 * @param secret
	 * @param playerName
	 * @return
	 * @throws Exception
	 */

	public final Transaction[] transactionSync(String secret, String playerName) throws Exception {
		final String result = networkValidate(secret, playerName);
		Gson gson = new Gson();
		final Transaction[] donations = gson.fromJson(result, Transaction[].class);
		return donations;
	}

	/**
	 * Get the player's name
	 * 
	 * @return the player name
	 */

	public String getPlayerName() {
		return player_name;
	}

	/**
	 * Get the product id
	 * 
	 * @return the product id
	 */

	public int getProductId() {
		return product_id;
	}

	/**
	 * Get the product amount
	 * 
	 * @return the product amount
	 */

	public int getProductAmount() {
		return product_amount;
	}

	/**
	 * Get the amount purchased
	 * 
	 * @return the amount purchased
	 */

	public int getAmountPurchased() {
		return amount_purchased;
	}

	/**
	 * Get the product name
	 * 
	 * @return the product name
	 */

	public String getProductName() {
		return product_name;
	}

	/**
	 * Get the price of the product
	 * 
	 * @return the price of the product
	 */

	public double getProductPrice() {
		return product_price;
	}

	/**
	 * Get the message
	 * 
	 * @return the message
	 */

	public String getMessage() {
		return message;
	}
	
	public String getClassName() {
		return "Transaction";
	}

}
