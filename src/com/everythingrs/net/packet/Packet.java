package com.everythingrs.net.packet;

import java.util.PriorityQueue;

public abstract class Packet {
	
	public static PriorityQueue<Packet> queue = new PriorityQueue<>();
	
	public abstract void read(String message);
	
	public abstract int getType();
	
	public void addQueue(Packet packet) {
		queue.offer(packet);
	}
	
	public final static int DEFAULT = 0;
	public final static int YELL = 1;
	public final static int OFFENSE = 2;
	public final static int GIVERANK = 3;

}
