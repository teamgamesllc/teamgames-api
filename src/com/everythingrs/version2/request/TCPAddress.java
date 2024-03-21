package com.everythingrs.version2.request;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Fetches the available addresses for TeamGames
 * 
 * @author Nelson Sanchez
 *
 */

public class TCPAddress {

	/**
	 * All valid addresses for TeamGames
	 */

	private Queue<String> addresses = new ConcurrentLinkedQueue<String>();

	/**
	 * Instantiate the TCPAddresses
	 * 
	 * @param teamGamesAddress
	 */

	public TCPAddress(String[] teamGamesAddress) {
		for (String address : teamGamesAddress) {
			addresses.add(address);
		}
	}

	/**
	 * Add the address to the ArrayList
	 * 
	 * @param address the address that we want to add to the array list
	 */

	public void addAddress(String address) {
		addresses.add(address);
	}

	/**
	 * Check if the address is a valid one that belongs to TeamGames
	 * 
	 * @param teamGamesAddress the address that we want to check
	 * @return Whether or not the address is valid
	 */

	public boolean isValidAddress(String teamGamesAddress) {
		for (String address : addresses) {
			if (address == teamGamesAddress) {
				return true;
			}
		}
		return false;
	}

}
