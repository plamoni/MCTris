package com.codefreak.mc.mctris.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ControllerServer implements Runnable {
	private ServerSocket s; 
	private boolean acceptConnections = true;
	private ClientQueueManager queueManager = new ClientQueueManager();
	
	public ControllerServer(int port) {
		try {
			s = new ServerSocket(port);
			new Thread(this).start();
		} catch (IOException e) {
			System.err.println("Whoops, can't open socket (" + port + ") for controller! Is it in use? Or maybe you don't have permission?");
		}
	}
	
	@Override
	public void run() {
		while(acceptConnections) {
			try {
				Socket client = s.accept();
				System.out.println("Controller client connected from " + s.getInetAddress());
				
				ControllerClientHandler clientHandler = new ControllerClientHandler(client, this.queueManager);
				new Thread(clientHandler).start();
			} catch (IOException e) {
				System.out.println("Controller client disconnected.");
			}
		}
	}
}
