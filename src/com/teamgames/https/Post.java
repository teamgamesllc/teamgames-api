package com.teamgames.https;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for sending HTTP POST requests.
 */
public class Post {

    private static final ThreadLocal<Boolean> local = ThreadLocal.withInitial(() -> false);

    /**
     * Sends a POST request to the specified server and fetches the response.
     *
     * @param params   The POST parameters to send.
     * @param location The URL location or website directory to send the parameters to.
     * @param apiKey   The API key for authentication.
     * @return The response from the server as a String.
     * @throws Exception if an error occurs during the POST request.
     */
    public static String sendPostData(Map<String, Object> params, String location, String apiKey) throws Exception {
        String domain = local.get() ? "http://172.25.142.86:1353/" : "https://api.teamgames.io/";
        String target = domain + location;
        URL url = new URL(target);
        System.out.println("The target is : " + target);


        byte[] postDataBytes = constructPostData(params);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        configureConnection(conn, postDataBytes, apiKey);

        return performPostRequest(conn, postDataBytes);
    }

    /**
     * Constructs the POST data from a map of parameters.
     *
     * @param params The parameters to encode.
     * @return The encoded POST data as a byte array.
     * @throws Exception if an encoding error occurs.
     */
    private static byte[] constructPostData(Map<String, Object> params) throws Exception {
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        return postData.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Configures the HttpURLConnection with necessary headers and settings.
     *
     * @param conn          The connection to configure.
     * @param postDataBytes The POST data bytes to set content length.
     * @param apiKey        The API key for authentication.
     * @throws Exception if an error occurs during configuration.
     */
    private static void configureConnection(HttpURLConnection conn, byte[] postDataBytes, String apiKey) throws Exception {
        conn.setRequestMethod("POST");
        conn.setRequestProperty("X-API-Key", apiKey);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(false);
        HttpClientConfig.apply(conn);
    }

    /**
     * Performs the actual POST request.
     *
     * @param conn          The configured HttpURLConnection.
     * @param postDataBytes The POST data bytes to be sent.
     * @return The response as a String.
     * @throws Exception if an error occurs during the request.
     */
    private static String performPostRequest(HttpURLConnection conn, byte[] postDataBytes) throws Exception {
        try (java.io.OutputStream out = conn.getOutputStream()) {
            out.write(postDataBytes);
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
