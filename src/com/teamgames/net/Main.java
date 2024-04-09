package com.teamgames.net;

import com.teamgames.service.Service;

public class Main {

	public static Service websiteService;

	public static void startService() {
		if (websiteService == null) {
			websiteService = new Service();
		}
		Main.websiteService.start();
	}

	public static void main(String[] args) throws Exception {
		System.out.println("...");
		final String playerName = "Test";
		final String id = "1";
		final String amount = "1";

		for (int i = 0; i < 1000; i++) {
			com.teamgames.vote.Vote.service.execute(new Runnable() {
				@Override
				public void run() {
					try {
						com.teamgames.vote.Vote[] reward = com.teamgames.vote.Vote.reward("PLACEHOLDER", playerName,
								id, amount);
						if (reward[0].message != null) {
							System.out.println(reward[0].message);
							return;
						}

						System.out.println("Thank you for voting! You now have " + reward[0].vote_points + " vote points.");
					} catch (Exception e) {
						System.out.println("Api Services are currently offline. Please check back shortly");
						e.printStackTrace();
					}
				}

			});
		}
	}

}
