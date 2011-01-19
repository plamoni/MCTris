package com.codefreak.mc.mctris.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.codefreak.mc.mctris.MCTris;

public class ControllerServer implements Runnable {
	
	//Set this to "true" in order to validate clients. This requires that clients be
	// connected to the minecraft server in order to connect with the controller app.
	// this is almost certainly a good idea. :-)
	//also, this should be "final" but Eclipse will whine about unreachable code.
	private static boolean VALIDATE_CLIENTS = false;
	
	private ServerSocket s; 
	private boolean acceptConnections = true;
	private ClientQueueManager queueManager;
	private Server server;
	
	
	public ControllerServer(int port, Server server, MCTris plugin) {
		try {
			s = new ServerSocket(port);
			this.server = server;
			this.queueManager = new ClientQueueManager(plugin);
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
				
				if(!VALIDATE_CLIENTS || validateClient(client)) {
					System.out.println("Controller client connected from " + client.getInetAddress());
					
					ControllerClientHandler clientHandler = new ControllerClientHandler(client, this.queueManager);
					new Thread(clientHandler).start();
				} else {
					//the client is not connected to the minecraft server. Kill it.
					System.out.println("Controller connection denied for " + client.getInetAddress() + "... Not connected to MineCraft.");
					client.close();
				}
			} catch (IOException e) {
				System.out.println("Controller client disconnected.");
			}
		}
	}
	
	private boolean validateClient(Socket client) {
		InetAddress clientAddr = client.getInetAddress();
		
		for(Player player : this.server.getOnlinePlayers()) {
			if(player.getAddress().getAddress().equals(clientAddr)) {
				return true;
			}
		}
		
		return false;
	}
	
	public InetAddress getInetAddressOfActivePlayer() {
		return this.queueManager.getInetAddressOfActivePlayer();
	}
}
