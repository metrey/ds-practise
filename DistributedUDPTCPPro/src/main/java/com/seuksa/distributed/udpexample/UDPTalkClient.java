package com.seuksa.distributed.udpexample;


import java.io.*;
import java.net.*;
import java.util.*;

public class UDPTalkClient {
	public static void main(String[] args){
		DatagramSocket socket = null;
		DatagramPacket inPacket = null;
		DatagramPacket outPacket = null;
		String inMsg;
		byte[] inBuf, outBuf;
		Scanner keyboard = null;
		String strKbd;
		
		final int SERVER_PORT = 4444;
		InetAddress server_address;
		String host = "localhost";
		try{
			server_address = InetAddress.getByName(host);
			socket = new DatagramSocket();
			System.out.println("Type any message to send!");
			
			while(true){
				System.out.print("Client : ");
				keyboard = new Scanner(System.in);
				strKbd = keyboard.nextLine();
				outBuf = strKbd.getBytes();
				
				outPacket = new DatagramPacket(outBuf, 0, outBuf.length,
								server_address, SERVER_PORT);
				
				socket.send(outPacket);
				
				if(strKbd.equals("quit")){
					System.out.println("Client initiates disconnection");
					break;
				}
				
				//Receive packet from server
				inBuf = new byte[256];
				inPacket = new DatagramPacket(inBuf, inBuf.length);
				socket.receive(inPacket);
				inMsg = new String(inPacket.getData());
				
				System.out.println("Server : " + inMsg);
			}
			
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
