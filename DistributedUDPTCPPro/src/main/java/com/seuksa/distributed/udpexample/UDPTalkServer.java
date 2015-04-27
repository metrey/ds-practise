package com.seuksa.distributed.udpexample;


import java.io.*;
import java.net.*;
import java.util.*;

public class UDPTalkServer {
	public static void main(String[] args){
		DatagramSocket socket = null;
		DatagramPacket inPacket = null;
		DatagramPacket outPacket = null;
		byte[] inBuf;
		byte[] outBuf;
		final int SERVER_PORT = 4444;
		String inMsg;
		String strKbd;
		Scanner keyboard;
		
		int client_port;
		InetAddress client_address;
		try{
			socket = new DatagramSocket(SERVER_PORT);
			
			while(true){
				inBuf = new byte[256];
				inPacket = new DatagramPacket(inBuf, inBuf.length);
				System.out.println("Wait for client ... ");
				socket.receive(inPacket);
				
				client_port = inPacket.getPort();
				client_address = inPacket.getAddress();
				inMsg = new String(inPacket.getData());
				
				System.out.println("Got a connection from " + client_address +":" + client_port);
				System.out.println("Client : " + inMsg);
				System.out.println("Type any message to send back!");
				
				while(true){
					//Keyboard binder
					keyboard = new Scanner(System.in);
					
					System.out.print("Server : ");
					
					strKbd = keyboard.nextLine();
					
					outBuf = strKbd.getBytes();
					outPacket = new DatagramPacket(outBuf, 0, outBuf.length,
								client_address, client_port);
					socket.send(outPacket);
					
					//Wait for receiving from client
					inBuf = new byte[256];
					inPacket = new DatagramPacket(inBuf, inBuf.length);
					socket.receive(inPacket);
					inMsg = new String(inPacket.getData());
					
					if(inMsg.trim().equals("quit")){
						System.out.println("Client Disconnected!!!");
						break;
					}
					
					System.out.println("Client : " + inMsg);
				}
			}
			
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
