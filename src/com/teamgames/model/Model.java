package com.teamgames.model;

/**
 * A model class. That represents the update that the user is pushing to the server
 * @author Nelson
 *
 */

public abstract class Model {
	
	/**
	 * The name of the model that we are creating
	 */
	
	private String name;
	
	/**
	 * A secret key to connect to our EverythingRS API
	 */
	
	private String secret;
	
	/**
	 * Gets the secret key
	 * @return the secret key
	 */
	
	public String getSecret() {
		return secret;
	}
	
	/**
	 * Sets the secret key
	 * @param secret the secret key
	 */

	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * Gets the name of the model
	 * @return the name of the model
	 */

	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the model
	 * @param name the name of the model
	 */

	public void setName(String name) {
		this.name = name;
	}

}
