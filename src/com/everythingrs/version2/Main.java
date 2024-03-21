package com.everythingrs.version2;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.everythingrs.version2.thread.ThreadService;

// TODO Startup boolean is not thread safe

public class Main {

	public static AtomicBoolean init = new AtomicBoolean(false);

	public static void main(String args[]) throws Exception {
		init();
	}

	public static void init() throws IOException {
		if (getInit().get() == true) {
			return;
		}

//		System.out.println("Starting the WebHook Webserver on 127.0.0.1:55555.");
//		System.out.println("For more information please visit EverythingRS.com");
		Executors.newFixedThreadPool(10).execute(new Game());
//		new ThreadService();
	}

	public static AtomicBoolean getInit() {
		synchronized (init) {
			return init;
		}
	}

	public static void setBoolean() {
		synchronized (init) {
			init.set(true);
		}
	}

}
