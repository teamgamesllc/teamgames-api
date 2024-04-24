package com.teamgames.https;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author Nelson
 */

public class Post {

	protected static boolean local = false;

	/**
	 * Sends a POST request to our server and fetches an appropriate response.
	 *
	 * @param params   The POST parameters we are sending
	 * @param location The url location or website directory we are sending the
	 *                 parameter to
	 * @throws Exception
	 */


	public static String sendPostData(Map<String, Object> params, String location) throws Exception {
//		SSLUtilities.trustAllHostnames();
//		SSLUtilities.trustAllHttpsCertificates();
		String target = "https://everythingrs.com/" + location;
//		target = "http://localhost:1337/" + location;
		@SuppressWarnings("deprecation")
		// To keep backwards capability suppressing this for now
		URL url = new URL(target);
		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String, Object> param : params.entrySet()) {
			if (postData.length() != 0)
				postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		setRequestProperties(conn, postDataBytes);
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);
		Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (int c; (c = in.read()) >= 0;)
			builder.append((char) c);
		in.close();
		return builder.toString();
	}

	/**
	 * Sets the header properties for the page we are attempting to post on.
	 *
	 * @param conn
	 * @param postDataBytes
	 * @throws Exception
	 */

	public static void setRequestProperties(HttpURLConnection conn, byte[] postDataBytes) throws Exception {
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Host", "everythingrs.com");
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; rv:20.0) Gecko/20121202 Firefox/20.0");
		conn.setRequestProperty("Accept", "*/*");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
	}
}
