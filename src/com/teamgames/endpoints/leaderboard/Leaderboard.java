package com.teamgames.endpoints.leaderboard;

import com.teamgames.https.Post;
import com.teamgames.lib.gson.Gson;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Leaderboard {
	
	private final static String ENDPOINT_URL = "v3/leaderboard/metrics/update";
	
	private String apiKey;
    private String gameMode;
    private String playerName;
    private List<PlayerMetric> playerMetrics;
    private boolean debugMessage;
    private String ipAddress;
    private String userRole;
    private Map<String, Object> metadata = new LinkedHashMap<>();
    
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    // Constructor
    public Leaderboard() {
    }

    // Method for setting game mode
    public Leaderboard setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    // Method for setting game mode
    public Leaderboard setGameMode(String gameMode) {
        this.gameMode = gameMode;
        return this;
    }

    // Method for setting player name
    public Leaderboard setPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    // Method for setting player metrics
    public Leaderboard setPlayerMetrics(List<PlayerMetric> playerMetrics) {
        this.playerMetrics = playerMetrics;
        return this;
    }

    // Method for setting debug message
    public Leaderboard setDebugMessage(boolean debugMessage) {
        this.debugMessage = debugMessage;
        return this;
    }

    // Method for setting IP address
    public Leaderboard setIPAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    // Method for setting IP address
    public Leaderboard setUserRole(String role) {
        this.userRole = role;
        return this;
    }
    
    // Method for setting Meta Data
    public Leaderboard addMetadata(String key, Object value) {
        metadata.put(key, value);
        return this;
    }
    
    /**
     * Asynchronously updates the leaderboard with new metric data for a player.
     */
    public void submitAsync() {
        executor.submit(() -> {
            try {
                submit();
            } catch (Exception e) {
                e.printStackTrace(); // Handle exception appropriately
            }
        });
    }

    /**
     * Update the Leaderboard with new metric data for a player.
     */
    
	public void submit() throws Exception {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("gameMode", gameMode);
        params.put("playerName", playerName);
        if (ipAddress != null) {
            params.put("ipAddress", ipAddress);
        }
        if (userRole != null) {
            params.put("userRole", userRole);
        }
        List<Map<String, Object>> metricsList = new ArrayList<>();
        for (PlayerMetric metric : playerMetrics) {
            Map<String, Object> metricMap = new LinkedHashMap<>();
            metricMap.put("name", metric.getName());
            metricMap.put("value", metric.getValue());
            metricMap.put("progress", metric.getProgress());
            metricsList.add(metricMap);
        }
        Gson gson = new Gson();
        String jsonMetrics = gson.toJson(metricsList);
        params.put("metrics", jsonMetrics);
        
        String endpoint = ENDPOINT_URL;
        
        String response = Post.sendPostData(params, endpoint, apiKey);
        
        if (debugMessage) {
            System.out.println("POST request sent. Response: " + response);
        }
    }
	
	
    public void shutdown() {
        executor.shutdown();
    }
	
}
