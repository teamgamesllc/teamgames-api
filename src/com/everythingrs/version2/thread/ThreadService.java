package com.everythingrs.version2.thread;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.everythingrs.version2.Game;

public class ThreadService {

	public static ThreadService threadService;

	private Webhook webhook;
	private Game input;
	private NetworkThread networkThread;
	private ExecutorService service = Executors.newFixedThreadPool(10);

	public ThreadService() throws IOException {
		synchronized (this) {
			this.webhook = new Webhook();
			this.input = new Game();
			this.networkThread = new NetworkThread();
//			this.service.execute(this.webhook);
//			this.service.execute(this.input);
			this.service.execute(this.networkThread);
			threadService = this;
		}
	}

	public static synchronized ThreadService getThreadService() {
		if (threadService == null) {
			try {
				new ThreadService();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		synchronized (threadService) {
			return threadService;
		}
	}

	/**
	 * Get the webhook thread
	 * 
	 * @return the webhook thread
	 */

	public Webhook getWebhook() {
		return webhook;
	}

	/**
	 * Get the input thread
	 * 
	 * @return The input thread
	 */

	public Game getInput() {
		return input;
	}

	/**
	 * Get the network thread
	 * 
	 * @return the network thread
	 */

	public synchronized NetworkThread getNetworkThread() {
		return networkThread;
	}

	/**
	 * Get the services
	 * 
	 * @return the service
	 */

	public ExecutorService getService() {
		return service;
	}

}
