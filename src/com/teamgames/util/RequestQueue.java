package com.teamgames.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RequestQueue {
	
	private static final RequestQueue requestQueue = new RequestQueue();
	
	public static RequestQueue getRequestQueue() {
		return requestQueue;
	}
    private final ConcurrentMap<String, Boolean> activeRequests = new ConcurrentHashMap<>();

    public boolean startRequest(String username) {
        return activeRequests.putIfAbsent(username, true) == null;
    }

    public void finishRequest(String username) {
        activeRequests.remove(username);
    }
}