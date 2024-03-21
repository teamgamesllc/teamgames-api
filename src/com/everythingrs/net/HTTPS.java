package com.everythingrs.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author Genesis
 */



public class HTTPS {
	
	/**
	 * @deprcated
	 * This method is now deprecated and will no longer be used with the newer
	 * versions of our API. All updated versions are set to use HTTPS & POST
	 * */

    public static String connection(String connectionURL) throws Exception {
//        SSLUtilities.trustAllHostnames();
//        SSLUtilities.trustAllHttpsCertificates();
        connectionURL = connectionURL.replace(" ", "%20");
        URL url = new URL(connectionURL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Host", "everythingrs.com");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; rv:20.0) Gecko/20121202 Firefox/20.0");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        String html = "";
        while ((inputLine = in.readLine()) != null) {
            html += inputLine;
        }
        in.close();
        return html;
    }
}
