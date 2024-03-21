package com.everythingrs.version2.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * Creates a web server to send Webhook information
 * 
 * @author Nelson Sanchez
 *
 */

// TODO validate that the request is coming from a TeamGames address

public class Webhook extends Thread {

	private ServerSocket serverSock;
	Socket sock;

	/**
	 * Initialize the Webhook constructor
	 * 
	 * @throws IOException An IOException
	 */

	public Webhook() throws IOException {
		serverSock = new ServerSocket(55555);
		sendWebhookData();
	}

	/**
	 * @param args
	 */
	public void run() {
		while (true) {
			try {
				System.out.println("New Webhook Request from TeamGames");
				sock = serverSock.accept();
				System.out.println(sock.getInetAddress());
				InputStream inputStream = sock.getInputStream();
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
				getRequestHeaders(bufferReader);
				PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
				sendHTMLResponse(out);
				bufferReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get the headers for the request
	 * 
	 * @param bufferReader The buffer reader
	 * @throws IOException Exception
	 */

	public void getRequestHeaders(BufferedReader bufferReader) throws IOException {
		String request;
		boolean loop = true;
		while ((request = bufferReader.readLine()) != null && loop) {
			System.out.println(request);
			if (request.isEmpty() || getPostMessage(request)) {
				loop = false;
				break;
			}
		}
	}

	/**
	 * Gets the POST message from TeamGames
	 * 
	 * @param request the request that was made
	 * @return Whether or not the response message contains a POST request
	 */

	public boolean getPostMessage(String request) {
		if (request.startsWith("X-POST: ")) {
			return true;
		}
		return false;
	}

	/**
	 * Send an HTML Response back to the requester
	 * 
	 * @param out the HTML response
	 */

	public void sendHTMLResponse(PrintWriter out) {
		out.write("HTTP/1.0 200 OK\r\n");
		out.write(new Date() + "\r\n");
		out.write("Content-Type: text/html\r\n");
		out.write("\r\n");
		out.write("<html>");
		out.write("<title>EverythingRS Webhooks</title>");
		out.write("<body>");
		out.write("<p>Now accepting webhook requests from EverythingRS.</p>");
		out.write("<p>For more information please visit <a href='https://everythingrs.com'>https://everythingrs.com</a></p>");
		out.write("\r\n");
		out.write("TeamGames version 2.01");
		out.write("</body>");
		out.write("</html>");
		out.flush();
		out.close();
	}

	/**
	 * Send data pertaining to the Webhook so EverythingRS knows where to send the
	 * response to
	 * 
	 * @throws IOException
	 */

	public void sendWebhookData() throws IOException {
		// Send Data to EverythingRS that says about the Webhook information
//		try (Socket socket = new Socket()) {
//			socket.connect(new InetSocketAddress("teamgames.io", 80));
//			System.out.println("Local address is " + socket.getLocalAddress());
//		}
	}

}