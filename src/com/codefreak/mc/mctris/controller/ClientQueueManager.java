package com.codefreak.mc.mctris.controller;

import java.net.InetAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.codefreak.mc.mctris.MCTris;

public class ClientQueueManager {
	private ConcurrentLinkedQueue<ControllerClientHandler> queuedClients = new ConcurrentLinkedQueue<ControllerClientHandler>();
	private ControllerClientHandler active = null;
	private MCTris plugin;
	
	public ClientQueueManager(MCTris plugin) {
		this.plugin = plugin;
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
			this.plugin.onKeyPress(key);
		} else { //not the active client!
			//do nothing. No one cares.
		}
	}
	
	public InetAddress getInetAddressOfActivePlayer() {
		return this.active.getInetAddress();
	}
}
