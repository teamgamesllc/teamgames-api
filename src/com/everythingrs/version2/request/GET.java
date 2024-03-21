package com.everythingrs.version2.request;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author Nelson Sanchez
 */

public class GET {

	public String fetch(String connectionURL) {
		try {
			return connection(connectionURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @deprcated This method is now deprecated and will no longer be used with the
	 *            newer versions of our API. All updated versions are set to use
	 *            HTTPS & POST
	 */

	private final String connection(String connectionURL) throws Exception {
		connectionURL = connectionURL.replace(" ", "%20");
		URL url = new URL(connectionURL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		setRequestProperties(connection);
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		String html = "";
		while ((inputLine = in.readLine()) != null) {
			html += inputLine;
		}
		in.close();
		return html;
	}

	private void setRequestProperties(HttpURLConnection connection) throws ProtocolException {
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Host", "everythingrs.com");
		connection.setRequestProperty("User-Agent", "EverythingRS Client API");
		connection.setRequestProperty("Accept", "*/*");
		connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	}
}
