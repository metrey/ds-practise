package com.seuksa.distributed.tcpexample;

import java.io.*;
import java.net.*;

public class TCPDateClient {
	public static void main(String[] args){
		BufferedReader inBuf = null;
		//String host = args[0];
		String host = "192.168.0.50";
		try{
			
			Socket conSocket = new Socket(host, 8888);
			
			System.out.println("Client local port = " + conSocket.getLocalPort());
			System.out.println("Remote port = " + conSocket.getPort());
			
			inBuf = new BufferedReader(new InputStreamReader(conSocket.getInputStream()));
			
			System.out.println("Server Date : " + inBuf.readLine());

			inBuf.close();
			conSocket.close();
			
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
