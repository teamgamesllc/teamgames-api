package com.teamgames.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RateLimiter {

    private static class RequestData {
        long startTime;
        long requestCount;
    }

    private static final ConcurrentHashMap<String, RequestData> clientRequestData = new ConcurrentHashMap<>();
    private static volatile long lastCleanup = 0L;
    private static final long CLEANUP_INTERVAL_MS = TimeUnit.MINUTES.toMillis(5);

    public static boolean isRequestAllowed(String clientId, long maxRequests, long minutes) {
        long currentTime = System.currentTimeMillis();
        long windowMs = TimeUnit.MINUTES.toMillis(minutes);

        maybeCleanup(currentTime, windowMs);

        RequestData data = clientRequestData.compute(clientId, (key, requestData) -> {
            if (requestData == null || currentTime - requestData.startTime > windowMs) {
                requestData = new RequestData();
                requestData.startTime = currentTime;
                requestData.requestCount = 0;
            }
            requestData.requestCount++;
            return requestData;
        });

        return data.requestCount <= maxRequests;
    }

    private static void maybeCleanup(long currentTime, long windowMs) {
        if (currentTime - lastCleanup < CLEANUP_INTERVAL_MS) {
            return;
        }

        synchronized (RateLimiter.class) {
            if (currentTime - lastCleanup < CLEANUP_INTERVAL_MS) {
                return;
            }

            clientRequestData.entrySet().removeIf(entry -> currentTime - entry.getValue().startTime > windowMs);
            lastCleanup = currentTime;
        }
    }
}
