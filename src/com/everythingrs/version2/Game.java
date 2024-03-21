package com.everythingrs.version2;

import java.util.Scanner;

import com.everythingrs.version2.request.NetworkRequest;
import com.everythingrs.version2.store.Transaction;
import com.everythingrs.version2.thread.NetworkThread;
import com.everythingrs.version2.thread.ThreadService;

public class Game extends Thread {

	private Scanner scanner;

	public Game() {
		scanner = new Scanner(System.in);
	}

	public void run() {
		while (true) {
			System.out.println("Please type a command");
			final String input = scanner.next();
			if (scanner.hasNextLine()) {
				System.out.println("You typed: " + input);
				if (input.equalsIgnoreCase("sync")) {
					claimSync();
				}
				if (input.equalsIgnoreCase("claim")) {
					NetworkThread networkService = ThreadService.getThreadService().getNetworkThread();
					networkService.addToQueue(new NetworkRequest("username", "<secret>", "claim-store"));
					System.out.println("Processing transaction. Please wait a few seconds and then type ::finish");
				}
				if (input.equalsIgnoreCase("finish")) {
					System.out.println("Finishing command");
					NetworkThread networkService = ThreadService.getThreadService().getNetworkThread();
					final TeamGamesRequest[] teamgamesRequest = networkService.getAndRemoveFromMap("username");
					if (teamgamesRequest != null) {
						final String className = teamgamesRequest[0].getClassName();
						if (className.equalsIgnoreCase("Transaction")) {
							final Transaction[] transactions = (Transaction[]) teamgamesRequest.clone();
							if (transactions.length == 0) {
								System.out.println("You currently don't have any items waiting. You must donate first!");
								continue;
							}
							if (transactions[0].getMessage() != null) {
								System.out.println(transactions[0].getMessage());
								continue;
							}
							for (Transaction transaction : transactions) {
								// c.getItems().addItem(transaction.getProductId(),
								// transaction.getProductAmount());
							}
							System.out.println("Thank you for donating!");
						}
					} else {
						System.out.println("Request is null");
					}
				}
				if (input.equalsIgnoreCase("network")) {
					claim();
				}
				if (input.equalsIgnoreCase("reward")) {
					claimReward();
				}
			}
		}
	}

	public void claimSync() {
		try {
			Transaction[] transactions = new Transaction().transactionSync("<secret>", "<username>");
			if (transactions.length == 0) {
				System.out.println("You currently don't have any items waiting. You must donate first!");
				return;
			}
			if (transactions[0].getMessage() != null) {
				System.out.println(transactions[0].getMessage());
				return;
			}
			System.out.println("Thank you for donating!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void claim() {
		NetworkThread networkService = ThreadService.getThreadService().getNetworkThread();
		networkService.addToQueue(new NetworkRequest("username", "<secret>", "claim-store"));
		System.out.println("Processing transaction. Please wait a few seconds and then type ::finish");
	}

	public void finishClaim() {
		NetworkThread networkService = ThreadService.getThreadService().getNetworkThread();
		final TeamGamesRequest[] teamgamesRequest = networkService.getAndRemoveFromMap("username");
		if (teamgamesRequest != null) {
			final String className = teamgamesRequest[0].getClassName();
			if (className.equalsIgnoreCase("Transaction")) {
				final Transaction[] transactions = (Transaction[]) teamgamesRequest.clone();
				if (transactions.length == 0) {
					System.out.println("You currently don't have any items waiting. You must donate first!");
					return;
				}
				if (transactions[0].getMessage() != null) {
					System.out.println(transactions[0].getMessage());
					return;
				}
				for (Transaction transaction : transactions) {
					// c.getItems().addItem(transaction.getProductId(),
					// transaction.getProductAmount());
				}
				System.out.println("Thank you for donating!");
			}
		}
	}

	public void claimReward() {
		NetworkThread networkService = ThreadService.getThreadService().getNetworkThread();
		networkService.setMaxQueueSize(5);
		networkService.addToQueue(new NetworkRequest("username", "<secret>", "claim-reward"));
	}
}
