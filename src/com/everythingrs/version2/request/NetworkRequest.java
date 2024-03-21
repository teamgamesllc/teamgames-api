package com.everythingrs.version2.request;

/**
 * Results to be polled
 * 
 * @author Nelson Sanchez
 *
 */

public class NetworkRequest {

	private final String username;
	private final String secret;
	private final String command;
	private final long expiration;

	/**
	 * The network object that will be polled
	 * 
	 * @param username the username of the customer
	 * @param secret   the API key from EverythingRS.com
	 * @param command  the command that will be run
	 */

	public NetworkRequest(String username, String secret, String command) {
		this.username = username;
		this.secret = secret;
		this.command = command;
		// Expires in two minutes
		final long expirationTime = 2 * 60 * 1000;
		this.expiration = System.currentTimeMillis() + expirationTime;
	}

	/**
	 * The username of the customer
	 * 
	 * @return the username of the customer
	 */

	public String getUsername() {
		return username;
	}

	/**
	 * The API Key from https://everythingrs.com
	 * 
	 * @return The API key from https://everythingrs.com
	 */

	public String getSecret() {
		return secret;
	}

	/**
	 * The command
	 * 
	 * @return the command
	 */

	public String getCommand() {
		return command;
	}

	/**
	 * The timestamp
	 * 
	 * @return the timestamp
	 */

	public long getTimeStamp() {
		return expiration;
	}

}
