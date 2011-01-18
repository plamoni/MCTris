package com.codefreak.mc.mctris.controller;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientQueueManager {
	private ConcurrentLinkedQueue<ControllerClientHandler> queuedClients = new ConcurrentLinkedQueue<ControllerClientHandler>();
	private ControllerClientHandler active = null;
	
	public ClientQueueManager() {
		
	}
	
	public void clientConnect(ControllerClientHandler client) {
		this.queuedClients.add(client);
		if(active == null) {
			activateNextClient();
		}
	}
	
	public void clientDisconnect(ControllerClientHandler client) {
		System.out.println("Removing " + client.getInetAddress() + " from queue...");
		this.queuedClients.remove(client);
		if(client == this.active) {
			this.activateNextClient();
		}
	}
	
	public void activateNextClient() {
		System.out.println("Activating next client...");
		this.active = this.queuedClients.poll();
		
		if(this.active != null) {
			this.active.setActive();
			System.out.println("Now active: " + this.active.getInetAddress());
		} else {
			System.out.println("No clients waiting...");
		}
	}
	
	public void pressKey(ControllerClientHandler handler, int key) {
		if(handler == this.active) {
			System.out.println("Pressing key: " + (char)key);
		} else { //not the active client!
			//do nothing. No one cares.
		}
	}
}
