package com.teamgames.net.packet.impl;

import com.teamgames.net.packet.Packet;

/**
 * Handles all offenses from the ERS api console
 * @author Nelson
 *
 */

public class Offense extends Packet {
	
	private String username;
	private String offenseType;
	private String duration;

	@Override
	public void read(String message) {
		setUsername(message.split("::")[0]);
		setOffenseType( message.split("::")[1]);
		setDuration(message.split("::")[2]);
		addQueue(this);
	}
	
	@Override
	public int getType() {
		return Packet.OFFENSE;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOffenseType() {
		return offenseType;
	}

	public void setOffenseType(String offense) {
		this.offenseType = offense;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
	

}
