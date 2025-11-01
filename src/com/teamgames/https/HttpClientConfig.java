package com.teamgames.https;

import java.net.HttpURLConnection;

/**
 * Centralised HTTP client configuration. Applies connection pooling hints and timeouts to
 * every request issued by the SDK while allowing overrides via system properties.
 *
 * The defaults can be customised with the following system properties:
 * <ul>
 *   <li>{@code teamgames.http.connectTimeoutMs}</li>
 *   <li>{@code teamgames.http.readTimeoutMs}</li>
 *   <li>{@code teamgames.http.keepAlive} (true/false)</li>
 *   <li>{@code teamgames.http.maxConnections}</li>
 * </ul>
 */
public final class HttpClientConfig {

    private static final String CONNECT_TIMEOUT_KEY = "teamgames.http.connectTimeoutMs";
    private static final String READ_TIMEOUT_KEY = "teamgames.http.readTimeoutMs";
    private static final String KEEP_ALIVE_KEY = "teamgames.http.keepAlive";
    private static final String MAX_CONNECTIONS_KEY = "teamgames.http.maxConnections";

    private static final int DEFAULT_CONNECT_TIMEOUT_MS = 10_000;
    private static final int DEFAULT_READ_TIMEOUT_MS = 15_000;
    private static final boolean DEFAULT_KEEP_ALIVE = true;
    private static final String DEFAULT_MAX_CONNECTIONS = "50";

    static {
        // Enable keep-alive and raise the default connection pool size unless explicitly overridden.
        if (System.getProperty("http.keepAlive") == null) {
            System.setProperty("http.keepAlive", Boolean.toString(getKeepAliveDefault()));
        }
        if (System.getProperty("http.maxConnections") == null) {
            System.setProperty("http.maxConnections", getMaxConnectionsDefault());
        }
    }

    private HttpClientConfig() {
    }

    /**
     * Applies the configured connect/read timeouts and keep-alive header to the connection.
     */
    public static void apply(HttpURLConnection connection) {
        connection.setConnectTimeout(getConnectTimeout());
        connection.setReadTimeout(getReadTimeout());
        connection.setRequestProperty("Connection", getKeepAliveDefault() ? "Keep-Alive" : "close");
    }

    private static int getConnectTimeout() {
        return getIntProperty(CONNECT_TIMEOUT_KEY, DEFAULT_CONNECT_TIMEOUT_MS);
    }

    private static int getReadTimeout() {
        return getIntProperty(READ_TIMEOUT_KEY, DEFAULT_READ_TIMEOUT_MS);
    }

    private static boolean getKeepAliveDefault() {
        return Boolean.parseBoolean(System.getProperty(KEEP_ALIVE_KEY, Boolean.toString(DEFAULT_KEEP_ALIVE)));
    }

    private static String getMaxConnectionsDefault() {
        return System.getProperty(MAX_CONNECTIONS_KEY, DEFAULT_MAX_CONNECTIONS);
    }

    private static int getIntProperty(String key, int defaultValue) {
        String value = System.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
