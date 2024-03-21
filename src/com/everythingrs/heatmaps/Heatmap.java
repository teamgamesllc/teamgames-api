package com.everythingrs.heatmaps;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.everythingrs.service.call.Post;
import com.everythingrs.lib.gson.Gson;

public class Heatmap {
	
	private static ConcurrentHashMap<String, Heatmap> map = new ConcurrentHashMap<String, Heatmap>();
	
	/**
	 * Getter for the map, which will then be passed to the API
	 * @return
	 */
	
	public static ConcurrentHashMap<String, Heatmap> getMap() {
		return map;
	}

	private String username;
	private int positionX;
	private int positionY;
	private int positionZ;
	private String message;
	
	/**
	 * @param username The player's username
	 * @param x The player's position on the x axis
	 * @param y The player's position on the y axis
	 * @param z The player's height
	 */
	
	public Heatmap(String username, int x, int y, int z) {
		this.username = username;
		this.positionX = x;
		this.positionY = y;
		this.positionZ = z;
	}
	
	/**
	 * Updates the heatmaps
	 */
	
	public static void update(String secret) {
		try {
			Map<String, Object> params = new LinkedHashMap<>();
			params.put("secret", secret);
			params.put("heatmap", new Gson().toJson(getMap()));
			Post.sendPostData(params, "api/heatmaps/update");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
