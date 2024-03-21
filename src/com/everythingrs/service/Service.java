package com.everythingrs.service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.everythingrs.hiscores.Hiscores;
import com.everythingrs.model.Model;
import com.everythingrs.model.ers.*;
import com.everythingrs.service.TickHandler;

public class Service {

	/**
	 * Executor Service, this is now used for all the newer API's instead of
	 * creating a separate Executor Service for it.
	 */

	public static ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(1);

	/**
	 * A scheduled executor service that will be ran every 600ms.
	 */

	public ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

	/**
	 * A queue of all tasks to perform
	 */

	public Queue<Model> queue = new ConcurrentLinkedQueue<>();

	/**
	 * Whether or not the service has already been started
	 */

	public boolean isStarted;

	/**
	 * Gets the scheduled executor service thread
	 * 
	 * @return service
	 */

	public ScheduledExecutorService getService() {
		return scheduledService;
	}

	public void start() {
		if (!isStarted) {
			isStarted = true;
			service.scheduleAtFixedRate(new TickHandler(this), 0, 600, TimeUnit.MILLISECONDS);
		}
	}

	public void queue() {
		
		// If the queue is empty then return
		
		if (queue.isEmpty())
			return;
		
		// Now let's check the size of the queue

		int updatesPerCycle = queue.size();
		
		// If the size is greater than 50, then we will set it to 50. As we don't 
		// want too many updates per cycle.

		if (queue.size() > 50)
			updatesPerCycle = 50;
		
		// Now let's loop through the updates

		for (int i = 0; i < updatesPerCycle; i++) {

			Model model = this.queue.poll();

			switch (model.getName()) {
			case "updateHiscores":
				
				HiscoresModel hiscores = (HiscoresModel) model;
				
				Hiscores.submitHiscores(model.getSecret(), hiscores.getGameMode(), hiscores.getPlayerName(),
						hiscores.getRights(), hiscores.getPlayerXP(), hiscores.isDebugMessage());
				
				break;
				
			}

		}

	}

}
