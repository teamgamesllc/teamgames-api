package com.everythingrs.net.packet.impl;

import com.everythingrs.net.packet.Packet;

public class DefaultPacket extends Packet {

	@Override
	public void read(String message) {
		addQueue(this);
	}
	
	@Override
	public int getType() {
		return Packet.DEFAULT;
	}
}
