package com.everythingrs.version2.limit;

public class RateLimiter {

	private int counter;
	private String username;
	private long lastRequest;

	/**
	 * How many times the username has attempted to create a request
	 * 
	 * @param counter     the counter
	 * @param username    the username
	 * @param lastRequest the last request
	 */

	public RateLimiter(int counter, String username, long lastRequest) {
		this.counter = counter;
		this.username = username;
		this.lastRequest = lastRequest;
	}

	/**
	 * @return the counter
	 */
	public synchronized int getCounter() {
		return counter;
	}

	/**
	 * @param counter the counter to set
	 */
	public synchronized void setCounter(int counter) {
		this.counter = counter;
	}

	/**
	 * @return the username
	 */
	public synchronized String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public synchronized void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the lastRequest
	 */
	public synchronized long getLastRequest() {
		return lastRequest;
	}

	/**
	 * @param lastRequest the lastRequest to set
	 */
	public synchronized void setLastRequest(long lastRequest) {
		this.lastRequest = lastRequest;
	}

}
