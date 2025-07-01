package com.teamgames.endpoints.playersonline;

import com.teamgames.https.Post;
import com.teamgames.lib.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handles periodic updates to the EverythingRS server with the current number of online players.
 * @author Nelson
 * @contributor Valk
 */


public class PlayersOnline {

	private static final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	private static final AtomicInteger players = new AtomicInteger();
	private static final Gson gson = new Gson();

	private String message;

	/**
	 * Gets the scheduled executor service thread (use with caution).
	 *
	 * @return the scheduled executor
	 */
	public static ScheduledExecutorService getService() {
		return service;
	}

	/**
	 * Starts the players online service. This sends a message to the EverythingRS server
	 * at a fixed interval to update the player count.
	 *
	 * @param secret  Your account secret key from EverythingRS.
	 * @param minutes Interval in minutes between updates (min: 5 for free, 2 for premium).
	 * @param debug   If true, prints debug messages from the server.
	 */
	public static void init(String secret, int minutes, boolean debug) {
		if (minutes < 5 && !debug) {
			throw new IllegalArgumentException("Free accounts must use an interval of 5 minutes or more.");
		}

		service.scheduleAtFixedRate(
				() -> insert(secret, players.get(), debug),
				0,
				minutes,
				TimeUnit.MINUTES
		);
	}

	/**
	 * Sends an update with the current player count to the EverythingRS server.
	 *
	 * @param secret Your EverythingRS secret key
	 * @param count  The current number of players online
	 * @param debug  If true, prints the server response message
	 */
	public static void insert(String secret, int count, boolean debug) {
		try {
			Map<String, Object> params = new LinkedHashMap<>();
			params.put("secret", secret);
			params.put("count", count);

			String response = Post.sendPostData(params, "api/playersonline/update", secret);

			if (debug) {
				PlayersOnline parsed = gson.fromJson(response, PlayersOnline.class);
				System.out.println(parsed.message);
			}
		} catch (Exception e) {
			if (debug) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Atomically increments the player count.
	 *
	 * @param amount amount to add
	 */
	public static void increment(int amount) {
		players.addAndGet(amount);
	}

	/**
	 * Atomically decrements the player count.
	 *
	 * @param amount amount to subtract
	 */
	public static void decrement(int amount) {
		players.addAndGet(-amount);
	}

	/**
	 * Atomically sets the player count.
	 *
	 * @param amount the exact value to set
	 */
	public static void set(int amount) {
		players.set(amount);
	}

	/**
	 * Retrieves the current player count.
	 *
	 * @return current online player count
	 */
	public static int get() {
		return players.get();
	}
}
