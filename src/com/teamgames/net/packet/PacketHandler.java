package com.teamgames.net.packet;

import com.everythingrs.net.packet.impl.*;

public class PacketHandler {

	public static Packet[] packets = new Packet[20];
	
	/**
	 * Initializes all EverythingRS Packets
	 */
	
	public void init() {
		packets[Packet.DEFAULT] = new DefaultPacket();
		packets[Packet.YELL] = new Yell();
		packets[Packet.OFFENSE] = new Offense();
//		packets[Packet.GIVERANK] = new Offense();

	}
	
	/**
	 * Handles and reads all incoming packets
	 * @param id
	 */
	
	public static void handle(int id, String message) {
		if (id > packets.length || id < 0) {
			packets[0].read(message);
			return;
		}
		packets[id].read(message);
	}

}
