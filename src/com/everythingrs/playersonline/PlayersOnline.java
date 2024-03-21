package com.everythingrs.playersonline;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.everythingrs.net.Post;
import com.teamgames.lib.gson.Gson;

/**
 * 
 * @author Nelson
 *
 */

public class PlayersOnline {
	
	public String message;

	public static ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
	private static AtomicInteger players = new AtomicInteger();
	
	/**
	 * Gets the scheduled executor service thread
	 * @return service
	 */
	
	public ScheduledExecutorService getService() {
		return service;
	}
	
	/**
	 * Starts the players online service, it will send a message to the EverythingRS server
	 * every 5 minutes to update the player count. 
	 * @param secret Your account secret key. This can be found in the account panel
	 * at https://everythingrs.com/account
	 * @param minutes The number of minutes until the next update is sent to our server.
	 * by default it will send a call every 5 minutes. If you wish for the players online to update
	 * even slow you can set the minutes to a higher number such as 60 which will make it call 
	 * once every hour. For free accounts you can not set the call to less than 5 minutes. 
	 * Premium members can update every 2 minutes. 
	 * @param debug Set this to false if you do not wish to receive any debug messages back from 
	 * our server.
	 */
	
	public static void init(String secret, int minutes, boolean debug) {
		service.scheduleAtFixedRate(new Runnable() {
			public void run() {
				insert(secret, players.get(), debug);
			}
		}, 0, minutes, TimeUnit.MINUTES);
	}
	
	/**
	 * Updates your players online counter
	 * @param secret the secret key of your account
	 * @param count the amount of players currently online
	 * @param debug sends back a debug message from the server
	 */
	
	public static void insert(String secret, int count, boolean debug) {
		try {
			Map<String, Object> params = new LinkedHashMap<>();
			params.put("secret", secret);
			params.put("count", count);
			final String req = Post.sendPostData(params, "api/playersonline/update");
			if (debug)
				System.out.println(new Gson().fromJson(req, PlayersOnline.class).message);
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}
	
	/**
	 * Increments the playercount by the value you enter
	 * @param amount
	 */
	
	public static void increment(int amount) {
		players.set(players.get() + amount);
	}
	
	/**
	 * Decrements the playercount by the value you enter
	 * @param amount
	 */
	
	public static void decrement(int amount) {
		players.set(players.get() - amount);
	}
	
	/**
	 * Sets the value of the playercount to whichever
	 * value you enter
	 * @param amount
	 */
	
	public static void set(int amount) {
		players.set(amount);
	}
	
	/**
	 * Gets the atomic integer for playercount
	 * @return
	 */
	
	public static int get() {
		return players.get();
	}

}
