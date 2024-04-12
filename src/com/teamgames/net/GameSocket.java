package com.teamgames.net;

import java.net.*;
import java.util.HashMap;
import java.io.*;

/**
 * Creates a serverSocket this way the server can accept API calls.
 * 
 * @author Nelson
 *
 */

public class GameSocket extends Thread {

	/**
	 * A queue of calls received from our end.
	 */

	public static HashMap<String, String> queue = new HashMap<>();

	protected ServerSocket serverSocket;

	/**
	 * Our socket constructor
	 * 
	 * @throws IOException
	 */

	public GameSocket() throws IOException {
		serverSocket = new ServerSocket(55555);
		serverSocket.setSoTimeout(10000);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket server = serverSocket.accept();
				System.out.println("EverythingRS bound to port " + serverSocket.getLocalPort() + " at "
						+ server.getRemoteSocketAddress());
				DataInputStream in = new DataInputStream(server.getInputStream());
				System.out.println(in.readUTF());
				// server.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("There was an issue with our EverythingRS api, please report the above error.");
			}
		}
	}

	public static void main(String args[]) throws Exception {
		new GameSocket().start();
	}

}
