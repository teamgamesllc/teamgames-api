package com.everythingrs.model.ers;

import com.teamgames.model.Model;

public class HiscoresModel extends Model {

	private String gameMode;
	private String playerName;
	private int rights;
	private int[] playerXP;
	private boolean debugMessage;
	
	/**
	 * Gets the current game mode of the Hiscores
	 * @return the current game mode
	 */

	public String getGameMode() {
		return gameMode;
	}
	
	/**
	 * Sets the current game mode of the Hiscores
	 * @param gameMode the current game mode
	 */

	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
	}
	
	/**
	 * Gets the player name to insert into the Hiscores
	 * @return the player's name
	 */

	public String getPlayerName() {
		return playerName;
	}
	
	/**
	 * Sets the player's name into the Hiscores
	 * @param playerName the player's name
	 */

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	/**
	 * Gets the player's privileges
	 * @return the player's privileges
	 */

	public int getRights() {
		return rights;
	}
	
	/**
	 * Sets the player's privileges
	 * @param rights the player's privileges
	 */

	public void setRights(int rights) {
		this.rights = rights;
	}
	
	/**
	 * Gets the player's experience
	 * @return the player's experience
	 */

	public int[] getPlayerXP() {
		return playerXP;
	}
	
	/**
	 * Sets the player's experience
	 * @param playerXP the player's experience
	 */

	public void setPlayerXP(int[] playerXP) {
		this.playerXP = playerXP;
	}
	
	/**
	 * Sets whether or not to receive debug messages
	 * @return whether or not to receive debug messages
	 */

	public boolean isDebugMessage() {
		return debugMessage;
	}
	
	/**
	 * Sets the debug message
	 * @param debugMessage the debug message
	 */

	public void setDebugMessage(boolean debugMessage) {
		this.debugMessage = debugMessage;
	}

}
