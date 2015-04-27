package com.seuksa.distributed.tcpexample;

import java.io.*;
import java.net.*;

public class TCPObjectServer {
	public static void main(String[] args){
		ServerSocket server_socket = null;
		Socket socket = null;
		try{
			server_socket = new ServerSocket(9999);
			
			while(true){
				System.out.println("Wait for client ....");
				socket = server_socket.accept();
				System.out.println("Accepted from " + socket.getInetAddress());
				
				ObjectOutputStream oo = new ObjectOutputStream(socket.getOutputStream());
				
				MyDate d = new MyDate();
				oo.writeObject(d);
				oo.flush();
			}
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
