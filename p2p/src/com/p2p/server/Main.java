package com.p2p.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	public static void main(String []args){
		String port = args[0];
		ServerSocket servSocket;
		try {
	  		servSocket = new ServerSocket(Integer.parseInt(port), 5);
	  		System.out.println("Listening for connections on port " + servSocket.getLocalPort());
	  		
	  		while (true) { 
	     		Socket connection = servSocket.accept();
	     		System.out.printf("Connection established with client %s:%d \n" ,connection.getInetAddress(),connection.getPort());
	     		Thread sendFile = new SendFile(connection);
	    		sendFile.start( );
	  		 }
		 }
		catch (IOException ex) {
	 		 ex.printStackTrace( );
		 } 
	}
	
}
