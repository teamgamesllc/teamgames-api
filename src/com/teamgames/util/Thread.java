package com.teamgames.util;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Thread {
	
	public static final Executor executor = Executors.newFixedThreadPool(10);
	
	public static final ExecutorService itemAdditionExecutor = Executors.newSingleThreadExecutor();

}
