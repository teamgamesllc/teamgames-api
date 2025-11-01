package com.teamgames.https;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utility for sending JSON POST requests to the TeamGames API v2 endpoints.
 */
public class JsonPost {

    private static final ThreadLocal<Boolean> local = ThreadLocal.withInitial(() -> false);

    /**
     * Sends a JSON payload to the given endpoint using the supplied API key.
     *
     * @param jsonBody JSON payload to send.
     * @param location Relative endpoint path (e.g. {@code api/v2/client/global/products}).
     * @param apiKey   Raw API key from the TeamGames dashboard.
     * @return Response body as a string.
     * @throws Exception if the request fails.
     */
    public static String send(String jsonBody, String location, String apiKey) throws Exception {
        boolean useLocal = local.get();
        try {
            String domain = useLocal ? "http://172.25.142.86:1353/" : "https://api.teamgames.io/";
            String target = domain + location;
            URL url = new URL(target);
          System.out.println("The target is : " + target);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            configureConnection(conn, jsonBody, apiKey);

            return performPostRequest(conn, jsonBody);
        } finally {
            if (useLocal) {
                local.remove();
            }
        }
    }

    private static void configureConnection(HttpURLConnection conn, String jsonBody, String apiKey) throws Exception {
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Content-Length", String.valueOf(jsonBody.getBytes(StandardCharsets.UTF_8).length));
        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(false);
        HttpClientConfig.apply(conn);

        String encodedKey = Base64.getEncoder().encodeToString(apiKey.getBytes(StandardCharsets.UTF_8));
        conn.setRequestProperty("Authorization", encodedKey);
    }

    private static String performPostRequest(HttpURLConnection conn, String jsonBody) throws Exception {
        byte[] bodyBytes = jsonBody.getBytes(StandardCharsets.UTF_8);

        try (OutputStream out = conn.getOutputStream()) {
            out.write(bodyBytes);
            out.flush();
        }

        return readResponse(conn);
    }

    /**
     * Sets or clears the local testing mode.
     *
     * @param isLocal If true, local domain will be used.
     */
    public static void setLocal(boolean isLocal) {
        local.set(isLocal);
    }

    private static String readResponse(HttpURLConnection conn) throws IOException {
        final int status = conn.getResponseCode();
        InputStream stream = status >= HttpURLConnection.HTTP_BAD_REQUEST ? conn.getErrorStream() : conn.getInputStream();
        if (stream == null) {
            return "";
        }
        StringBuilder response = new StringBuilder();
        try (Reader in = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            char[] buffer = new char[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                response.append(buffer, 0, read);
            }
        }

        if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
            throw new IOException("HTTP " + status + " :: " + response);
        }

        return response.toString();
    }
}
