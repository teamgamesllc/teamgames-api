package com.teamgames.logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.teamgames.lib.gson.Gson;

public class Logger {

	protected String username;
	protected String message;
	public static AtomicInteger values = new AtomicInteger();

	public static ScheduledExecutorService service = Executors.newScheduledThreadPool(2);

	public static void init(String secret, int seconds, boolean debug) {
		service.scheduleAtFixedRate(new Runnable() {
			public void run() {

			}
		}, 0, 5, TimeUnit.SECONDS);
	}

	public static void update(String secret, boolean debug) {

	}

	public Logger logger() {
		Gson gson = new Gson();
		return gson.fromJson("", Logger.class);
	}

}
