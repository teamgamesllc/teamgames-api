package com.teamgames.endpoints.leaderboard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestLeaderboard {

	public static void main(String args[]) {

		List<PlayerMetric> metrics = new ArrayList<>();

		final String[] skillNames = { "Attack", "Defense", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic",
				"Cooking", "Woodcutting", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore",
				"Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Hunter", "Construction" };

		for (String skillName : skillNames) {
			metrics.add(new PlayerMetric(skillName).setValue(99).setProgress(1000000));
		}

		metrics.add(new PlayerMetric("Total level").setValue(5).setProgress(10));
		metrics.add(new PlayerMetric("Wins").setValue(10));

		try {
			new Leaderboard().setApiKey("api-key")
			.setGameMode("Normal Mode").setPlayerName("Nelson")
					.setPlayerMetrics(metrics).setDebugMessage(false) // Optional. Used if you want to receive debug
					.submitAsync();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
