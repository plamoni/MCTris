package com.codefreak.mc.mctris.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class ControllerClientHandler implements Runnable {
	private Socket s;
	private PrintWriter out;
	private Scanner in;
	private ClientQueueManager queueManager;
	
	public ControllerClientHandler(Socket clientSocket, ClientQueueManager queueManager) {
		this.s = clientSocket;
		this.queueManager = queueManager;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Handling connection from " + s.getInetAddress());
			this.out = new PrintWriter(this.s.getOutputStream());
			this.in = new Scanner(this.s.getInputStream());
			
			this.queueManager.clientConnect(this);
			
			String data;
			
			
			while(true) {
				data = this.in.nextLine();
				this.processData(data);
			}
		} catch(IOException e) {
			System.out.println("Client disconnected.");
		} catch(NoSuchElementException e) {
			//hackish.
			System.out.println("Client disconnected (end of stream?)");
		} finally {
			this.queueManager.clientDisconnect(this);
		}
	}
	
	private void processData(String data) {
		try {
			JSONObject dataObj = new JSONObject(data);
			
			//System.out.println(this.s.getInetAddress() + " Pressed: " +  dataObj.get("key"));
			
			this.queueManager.pressKey(this, dataObj.getInt("key"));
			
		} catch (JSONException e) {
			//Only activate this if needed. Otherwise rouge "clients" will chew up your logs... :-)
			//System.out.println("Bad data from " + this.s.getInetAddress());
		}
		
	}
	
	public void setActive() {
		JSONObject dataObj = new JSONObject();
		try {
			dataObj.put("msg", "go");
			
			this.out.println(dataObj.toString());
			this.out.flush();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public InetAddress getInetAddress() {
		return this.s.getInetAddress();
	}
}
