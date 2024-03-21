package com.everythingrs.version2.thread;

import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.everythingrs.version2.TeamGamesRequest;
import com.everythingrs.version2.command.vote.Vote;
import com.everythingrs.version2.limit.RateLimiter;
import com.everythingrs.version2.request.NetworkRequest;
import com.everythingrs.version2.store.Transaction;

public class NetworkThread extends Thread {

	private final ConcurrentLinkedQueue<NetworkRequest> networkQueue = new ConcurrentLinkedQueue<NetworkRequest>();
	private final ConcurrentHashMap<String, TeamGamesRequest[]> completedMap = new ConcurrentHashMap<String, TeamGamesRequest[]>();
	
	private AtomicInteger maxQueueSize = new AtomicInteger(10);

	/**
	 * Sets the max size of the queue
	 * 
	 * @param The max size of the queue
	 */

	public void setMaxQueueSize(int size) {
		synchronized (maxQueueSize) {
			if (size == this.maxQueueSize.get()) {
				return;
			}
			this.maxQueueSize.set(size);
		}
	}

	/**
	 * Fetches the head of the queue Performs the network task Then removes the head
	 * of the queue
	 */

	public synchronized void run() {
		while (true) {
			synchronized (networkQueue) {
				if (!networkQueue.isEmpty()) {
					// If there is less than 3 threads then spawn a new one(?)
					final NetworkRequest request = networkQueue.peek();
//					System.out.println("Polling request");
					try {
						performNetworkTask(request);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					networkQueue.remove(request);
				}
			}
		}
	}

	/**
	 * Performs a network task
	 * 
	 * @param username
	 * @throws Exception
	 */

	public synchronized void performNetworkTask(NetworkRequest networkObject) throws Exception {
		String username = networkObject.getUsername();
		String secret = networkObject.getSecret();
		String command = networkObject.getCommand();
		switch (command) {
		case "claim-store":
			long start = System.currentTimeMillis();
			Transaction[] transactions = new Transaction().transactionSync(secret, username);
			addToMap(username, transactions);
			long end = System.currentTimeMillis();
//			System.out.println("[NetworkThread] Execution time is "
//					+ new DecimalFormat("#0.00000").format((end - start) / 1000d) + " seconds");
			break;
		case "claim-reward":
			long start2 = System.currentTimeMillis();
			Vote[] reward = new Vote().reward("secret_key", "username", "id", "amount");
			if (reward[0].getMessage() != null) {
				System.out.println(reward[0].getMessage());
				return;
			}
			long end2 = System.currentTimeMillis();
//			System.out.println("[NetworkThread] Execution time is "
//					+ new DecimalFormat("#0.00000").format((end2 - start2) / 1000d) + " seconds");
			break;
		}
	}

	public void addToQueue(NetworkRequest networkRequest) {
		// Check if it was already completed
		synchronized (completedMap) {
			if (completedMap.containsKey(networkRequest.getUsername())) {
				System.out.println("Waiting for the user to be removed from map before re-adding.");
				return;
			}
		}
		// Check if the network queue is full
		synchronized (networkQueue) {
			if (networkQueue.size() > maxQueueSize.get()) {
				System.out.println("The network queue is full");
				return;
			}
			networkQueue.add(networkRequest);
		}
	}

	/**
	 * Adds the username to a map
	 * 
	 * @param username         the username
	 * @param teamGamesRequest the request
	 */

	public void addToMap(String username, TeamGamesRequest[] teamGamesRequest) {
		if (completedMap.containsKey(username)) {
			System.out.println("Does not contain username");
			return;
		}
		completedMap.put(username, teamGamesRequest);
	}

	/**
	 * Gets the TeamGamesRequest from the map
	 * 
	 * @param username The username
	 * @return Returns the TeamGamesRequest
	 */

	public TeamGamesRequest[] getAndRemoveFromMap(String username) {
		return completedMap.remove(username);
	}

}
