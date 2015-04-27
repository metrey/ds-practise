package com.seuksa.distributed.tcpexample;

import java.net.*;
import java.io.*;

public class TCPEchoServer {
	public static void main(String[] args){
		ServerSocket server = null;
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		
		try{
			server = new ServerSocket(8888);
			
			while(true){
				System.out.println("Wait for client to connect......");
				socket = server.accept();
				System.out.println("Got connnection from " + socket.getInetAddress());
				
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				while(true){
					//Read message from client
					String msg = in.readLine();
					System.out.println("Client Say : " + msg);
					
					if(msg.equals("quit")){
						break;
					}
					
					//Send echo back to server
					out.println(msg.toUpperCase());
				}
				
			}
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
