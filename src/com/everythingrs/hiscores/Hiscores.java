package com.everythingrs.hiscores;

import com.everythingrs.model.ers.HiscoresModel;
import com.everythingrs.net.HTTPS;
import com.everythingrs.net.Main;

/**
 * @author Genesis
 */

public class Hiscores {

	/**
	 * This method generates an EverythingRS URL based on the skills provided.
	 *
	 * @param playerName
	 *            The name of the player
	 * @param rights
	 *            The players current rights
	 * @param playerXP
	 *            An XP array
	 */

	public static void update(String secret, String gameMode, String playerName, int rights, int[] playerXP,
			boolean debugMessage) {

//		Main.startService();
//
//		HiscoresModel hiscoresModel = new HiscoresModel();
//
//		hiscoresModel.setName("updateHiscores");
//		hiscoresModel.setSecret(secret);
//		hiscoresModel.setGameMode(gameMode);
//		hiscoresModel.setPlayerName(playerName);
//		hiscoresModel.setRights(rights);
//		hiscoresModel.setPlayerXP(playerXP);
//		hiscoresModel.setDebugMessage(debugMessage);
//
//		Main.websiteService.queue.add(hiscoresModel);
		
		StringBuilder builder = new StringBuilder();
		long totalExperience = 0;
		builder.append("https://everythingrs.com/api/account/hiscores/server/");
		builder.append(secret + "/");
		builder.append(gameMode.replace(" ", "%20") + "/");
		builder.append(playerName.replace(" ", "%20") + "/");
		builder.append(rights + "/");
		builder.append(getTotalLevel(playerXP) + "/");
		for (int i = 0; i < 25; i++) {
			try {
				int experience = playerXP[i];
				builder.append(experience + "/");
				totalExperience += experience;
			} catch (Exception e) {
				builder.append("0/");
			}
		}
		builder.append(totalExperience + "/");
		builder.append(calculateCombat(playerXP) + "/");
		if (debugMessage)
			System.out.println(builder.toString());
		try {
			final String response = HTTPS.connection(builder.toString());
			if (debugMessage == true && response.equalsIgnoreCase("Sucessfully added record to Hiscores")) {
				System.out.println("Successfully added record for " + playerName + " on the hiscores api.");
			}
		} catch (Exception e) {
			System.out.println("There was an error connecting to EverythingRS.com");
			e.printStackTrace();
		}
		
	}
	
	public static void update(String secret, String gameMode, String playerName, int rights, int totalLevel, int[] playerXP,
			boolean debugMessage) {
		
		StringBuilder builder = new StringBuilder();
		long totalExperience = 0;
		builder.append("https://everythingrs.com/api/account/hiscores/server/");
		builder.append(secret + "/");
		builder.append(gameMode.replace(" ", "%20") + "/");
		builder.append(playerName.replace(" ", "%20") + "/");
		builder.append(rights + "/");
		builder.append(totalLevel + "/");
		for (int i = 0; i < 25; i++) {
			try {
				int experience = playerXP[i];
				builder.append(experience + "/");
				totalExperience += experience;
			} catch (Exception e) {
				builder.append("0/");
			}
		}
		builder.append(totalExperience + "/");
		builder.append(calculateCombat(playerXP) + "/");
		if (debugMessage)
			System.out.println(builder.toString());
		try {
			final String response = HTTPS.connection(builder.toString());
			if (debugMessage == true && response.equalsIgnoreCase("Sucessfully added record to Hiscores")) {
				System.out.println("Successfully added record for " + playerName + " on the hiscores api.");
			}
		} catch (Exception e) {
			System.out.println("There was an error connecting to EverythingRS.com");
			e.printStackTrace();
		}
		
	}

	public static void submitHiscores(String secret, String gameMode, String playerName, int rights, int[] playerXP,
			boolean debugMessage) {
		StringBuilder builder = new StringBuilder();
		long totalExperience = 0;
		builder.append("https://everythingrs.com/api/account/hiscores/server/");
		builder.append(secret + "/");
		builder.append(gameMode.replace(" ", "%20") + "/");
		builder.append(playerName.replace(" ", "%20") + "/");
		builder.append(rights + "/");
		builder.append(getTotalLevel(playerXP) + "/");
		for (int i = 0; i < 25; i++) {
			try {
				int experience = playerXP[i];
				builder.append(experience + "/");
				totalExperience += experience;
			} catch (Exception e) {
				builder.append("0/");
			}
		}
		builder.append(totalExperience + "/");
		builder.append(calculateCombat(playerXP) + "/");
		if (debugMessage)
			System.out.println(builder.toString());
		try {
			final String response = HTTPS.connection(builder.toString());
			if (debugMessage == true && response.equalsIgnoreCase("Sucessfully added record to Hiscores")) {
				System.out.println("Successfully added record for " + playerName + " on the hiscores api.");
			}
		} catch (Exception e) {
			System.out.println("There was an error connecting to EverythingRS.com");
			e.printStackTrace();
		}
	}

	/**
	 * Calculates our combat level. At the moment this feature isn't implemented in
	 * our API, but will be used in the near future. This method was taken straight
	 * from project insanity, if anyone would like to contribute a better combat
	 * formula please message me.
	 *
	 * @return
	 */

	private static int calculateCombat(int[] playerXP) {
		int mage = (int) ((getLevelForXP(playerXP[6])) * 1.5);
		int range = (int) ((getLevelForXP(playerXP[4])) * 1.5);
		int attstr = (int) ((double) (getLevelForXP(playerXP[0])) + (double) (getLevelForXP(playerXP[2])));
		int combatLevel;
		if (range > attstr && range >= mage) {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[4])) * 0.4875)
					+ addSummoning(playerXP));
		} else if (mage > attstr && mage >= range) {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[6])) * 0.4875)
					+ addSummoning(playerXP));
		} else {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[0])) * 0.325)
					+ ((getLevelForXP(playerXP[2])) * 0.325) + addSummoning(playerXP));
		}
		return combatLevel;
	}

	/**
	 * This method is here as a failsafe (try/catch) to make sure that if there is
	 * no summoning on the server that the method calculateCombat() can still
	 * continue.
	 *
	 * @param playerXP
	 *            The players XP array
	 * @return
	 */

	private static double addSummoning(int[] playerXP) {
		try {
			return (getLevelForXP(playerXP[23])) * 0.125;
		} catch (Exception e) {

		}
		return 0;
	}

	/**
	 * Fetches our total level. We calculate the total level through here to make
	 * our script easier to implement. As many servers have their own methods for
	 * calculating this.
	 *
	 * @param playerXP
	 * @return
	 */

	public static int getTotalLevel(int[] playerXP) {
		int totalLevel = 0;
		for (int i = 0; i < 25; i++) {
			totalLevel += checkTotal(playerXP, i);
		}
		return totalLevel;
	}

	/**
	 * We have this extra layer for checking total levels incase there is a certain
	 * skill that the server doesn't have. In the case that the server doesn't have
	 * a skill such as dungeoneering, or summoning, then we return and add the value
	 * of zero. This is essential in preventing the script from breaking for users
	 * that don't have every skill on RuneScape.
	 *
	 * @param playerXP
	 *            the playerXP array.
	 * @param id
	 *            the id of the skill we are checking
	 * @return
	 */

	public static int checkTotal(int[] playerXP, int id) {
		try {
			return getLevelForXP(playerXP[id]);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * Gets the level for the experience
	 *
	 * @param exp
	 *            The exp that we're trying to convert to a level.
	 * @return
	 */

//	private static int getLevelForXP(int exp) {
//		int points = 0;
//		int output;
//		for (int lvl = 1; lvl <= 99; lvl++) {
//			points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
//			output = (int) Math.floor(points / 4);
//			if (output >= exp) {
//				return lvl;
//			}
//		}
//		return 99;
//	}
//	
//	private static int getLevelForXP(int exp) {
//	    int points = 0;
//	    for (int lvl = 1; lvl <= 99; lvl++) {
//	      points = (int)(points + Math.floor(lvl + 300.0D * Math.pow(2.0D, lvl / 7.0D)));
//	      int output = (int)Math.floor((points / 4));
//	      if (output >= exp)
//	        return lvl; 
//	    } 
//	    return 99;
//	}
	
	private static final int EXP_ARRAY[] = { 
            0,
            83,
            174,
            276,
            388,
            512,
            650,
            801,
            969,
            1154,
            1358,
            1584,
            1833,
            2107,
            2411,
            2746,
            3115,
            3523, 
            3973,
            4470,
            5018,
            5624,
            6291,
            7028, 
            7842,
            8740, 
            9730,
            10824,
            12031, 
            13363, 
            14833,
            16456,
            18247,
            20224, 
            22406, 
            24815, 
            27473,
            30408,
            33648,
            37224,
            41171,
            45529, 
            50339, 
            55649, 
            61512, 
            67983, 
            75127,
            83014,
            91721,
            101333,
            111945,
            123660, 
            136594,
            150872, 
            166636, 
            184040, 
            203254, 
            224466, 
            247886,
            273742,
            302288,
            333804,
            368599,
            407015,
            449428,
            496254,
            547953, 
            605032, 
            668051, 
            737627, 
            814445,
            899257,
            992895,
            1096278,
            1210421,
            1336443, 
            1475581,
            1629200,
            1798808,
            1986068,
            2192818,
            2421087,
            2673114,
            2951373,
            3258594,
            3597792, 
            3972294,
            4385776,
            4842295, 
            5346332, 
            5902831, 
            6517253, 
            7195629, 
            7944614, 
            8771558, 
            9684577, 
            10692629,
            11805606, 
            13034431,
            13037431,
            15039431,
            16044431,
            17054431,
            23064431,
            25074431,
            27084431,
            30094431,
            33134431,
            35534431,
            37534431,
            41000000,
            44000000,
            47000000,
            51000000,
            55000000,
            59000000,
            60000000,
            65000000,
            70500000, 
            85500000,
	};
	
	private static final int EXPERIENCE_FOR_120 = 95500000;
	
	public static int getLevelForXP(int experience) {
        if (experience <= EXPERIENCE_FOR_120) {
            for (int j = 119; j >= 0; j--) {
                if (EXP_ARRAY[j] <= experience) {
                    return j + 1;
                }
            }
        } else {
            int points = 0, output = 0;
            for (int lvl = 1; lvl <= 120; lvl++) {
                points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
                output = (int) Math.floor(points / 4);
                if (output >= experience) {
                    return lvl;
                }
            }
        }
        return 120;
    }

}
