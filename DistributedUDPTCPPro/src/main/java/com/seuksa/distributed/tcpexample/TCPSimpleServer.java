package com.seuksa.distributed.tcpexample;

import java.net.*;
import java.io.*;

public class TCPSimpleServer {
	public static void main(String[] args){
		try{
			ServerSocket server = new ServerSocket(9999);
			
			while(true){
				System.out.println("Wait for client connection...");
				Socket socket = server.accept();
				System.out.println("Got a client : " + socket.getInetAddress());
				
			}
		}catch(IOException ioe){
			System.out.println(ioe);
		}
		
	}
}
