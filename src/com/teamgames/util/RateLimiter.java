package com.teamgames.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RateLimiter {
//    private static final long MAX_REQUESTS = 10; // Max requests per time window
//    private static final long TIME_WINDOW_MS = TimeUnit.MINUTES.toMillis(1); // Time window in milliseconds
    
    private static class RequestData {
        long startTime;
        long requestCount;
    }
    
    private final static ConcurrentHashMap<String, RequestData> clientRequestData = new ConcurrentHashMap<>();

    public static boolean isRequestAllowed(String clientId, long MAX_REQUESTS, long MINUTES) {
        long currentTime = System.currentTimeMillis();
        RequestData data = clientRequestData.compute(clientId, (key, requestData) -> {
            if (requestData == null || currentTime - requestData.startTime > TimeUnit.MINUTES.toMillis(MINUTES)) {
                requestData = new RequestData();
                requestData.startTime = currentTime;
                requestData.requestCount = 0;
            }
            requestData.requestCount++;
            return requestData;
        });

        return data.requestCount <= MAX_REQUESTS;
    }
}
