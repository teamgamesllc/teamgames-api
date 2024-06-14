package com.teamgames.endpoints.marketplace;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.teamgames.https.Post;
import com.teamgames.lib.gson.Gson;

/**
 * Handles trade data
 * 
 * @author Nelson
 *
 */

public class Trade {

	public String username;
	public String tradeWith;

	public String hash;

	protected Stack<Item> stack = new Stack<Item>();

	private boolean state;

	public class TradeResponse {
		String message;
		boolean debug;
	}

	protected static ConcurrentHashMap<String, Stack<Item>> map = new ConcurrentHashMap<String, Stack<Item>>();
	protected static ExecutorService service = Executors.newCachedThreadPool();

	/**
	 * Updates the marketplace API with the current trades
	 * 
	 * @param secret
	 */

	public void update(String secret) {
		map.put(getUsername(), getStack());
		service.submit(new Runnable() {
			@Override
			public void run() {
				Stack<Item> firstTrade = map.get(getUsername());
				Stack<Item> secondTrade = map.get(getTradeWith());

				if (firstTrade != null && secondTrade != null) {
					System.out.println("Updating trade data...");
					map.remove(getUsername());
					map.remove(getTradeWith());
					Map<String, Object> params = new LinkedHashMap<>();
					params.put("secret", secret);
					params.put("username", getUsername());
					params.put("tradeWith", getTradeWith());
					params.put("trade", new Gson().toJson(firstTrade));
					params.put("secondTrade", new Gson().toJson(secondTrade));
					StringBuilder searchIndex = new StringBuilder();

					for (Item item : firstTrade) {
						searchIndex.append(item.getName() + " ");
					}

					for (Item item : secondTrade) {
						searchIndex.append(item.getName() + " ");
					}

					params.put("searchIndex", searchIndex.toString());
					try {
						Post.sendPostData(params, "api/marketplace/update", secret);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * Pushes a new item to the stack
	 * 
	 * @param item
	 */

	public void push(Item item) {
		stack.push(item);
	}

	/**
	 * Gets the stack
	 * 
	 * @return
	 */

	public Stack<Item> getStack() {
		return stack;
	}

	/**
	 * Returns the stack as a JSON object
	 * 
	 * @return
	 */

	public String getStackAsJson() {
		Gson gson = new Gson();
		return gson.toJson(stack);
	}

	/**
	 * Fetches the main username
	 * 
	 * @return
	 */

	public String getUsername() {
		return username;
	}

	/**
	 * Sets the main username
	 * 
	 * @param username
	 */

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Fetches the other player's name
	 * 
	 * @return
	 */

	public String getTradeWith() {
		return tradeWith;
	}

	/**
	 * Sets the other player's name
	 * 
	 * @param otherPlayer
	 */

	public void setTradeWith(String otherPlayer) {
		this.tradeWith = otherPlayer;
	}

	/**
	 * Gets a unique hash to identify the trade
	 * 
	 * @return
	 */

	public String getHash() {
		return hash;
	}

	/**
	 * Sets a unique hash to identify the trade
	 * 
	 * @return
	 */

	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * Fetches the state. State can be 0 or 1 Basically whether or not the user
	 * received (0) the item or has given (1) the item.
	 * 
	 * @return
	 */

	public boolean isState() {
		return state;
	}

	/**
	 * Sets the state
	 * 
	 * @param state
	 */

	public void setState(boolean state) {
		this.state = state;
	}

}
