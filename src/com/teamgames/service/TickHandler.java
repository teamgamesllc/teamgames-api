package com.teamgames.service;

public class TickHandler implements Runnable {
	
	/**
	 * This is the parent / service that we will communicate with
	 */
	
	protected Service service;
	
	/**
	 * Constructor for our TickHandler.
	 * @param service the service that we want to communicate with
	 */
	
	public TickHandler(Service service) {
		this.service = service;
	}
	
	/**
	 * This will run every tick
	 */

	@Override
	public void run() {
		service.queue();
	}
	
	

}
