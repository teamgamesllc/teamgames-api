package com.everythingrs.net.packet.impl;

import com.everythingrs.net.packet.Packet;

public class GiveRank extends Packet {
	
	private String username;
	private String rank;

	@Override
	public void read(String message) {
		setUsername(message.split("::")[0]);
		setRank(message.split("::")[1]);
		addQueue();
	}

	@Override
	public int getType() {
		return Packet.GIVERANK;
	}
	
	public void addQueue() {
		Packet.queue.offer(this);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}
	
	

}
