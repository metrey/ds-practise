package com.seuksa.distributed.tcpexample;

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPDateServer {
	public static void main(String[] args){
		String strClient;
		PrintWriter out;
		try{
			ServerSocket welcomeSocket = new ServerSocket(8888);
			
			while(true){
				System.out.println("Wait for client connection....");
				Socket conSocket = welcomeSocket.accept();
				System.out.println("Got Connection from client " + conSocket.getInetAddress() + " on port " + conSocket.getPort());
				System.out.println("Server local port = " + conSocket.getLocalPort());
				System.out.println("Remote port = " + conSocket.getPort());
				
				out = new PrintWriter(conSocket.getOutputStream(), true);
				
				Date d = new Date();
				out.println(d.toString());
	
				out.close();
			}
		}catch(IOException ioe){
			System.out.println(ioe);
			ioe.printStackTrace();
		}
	}
}
