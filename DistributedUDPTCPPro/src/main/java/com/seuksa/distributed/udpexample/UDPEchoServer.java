package com.seuksa.distributed.udpexample;
import java.io.*;
import java.net.*;

public class UDPEchoServer {
	public static void main(String[] args){
		DatagramSocket socket = null;
		DatagramPacket inPacket = null;
		DatagramPacket outPacket = null;
		byte[] inBuf, outBuf;
		String msg;
		final int PORT = 9999;
		
		try{
			while(true){
				socket = new DatagramSocket(PORT);
				System.out.println("Waiting for client .....");
				
				inBuf = new byte[256];
				inPacket = new DatagramPacket(inBuf, inBuf.length);
				socket.receive(inPacket);
				
				int client_port = inPacket.getPort();
				InetAddress client_address = inPacket.getAddress();
				System.out.println("Got connection from " + client_address + ":" + client_port);
				
				//Display message on server side
				msg = new String(inPacket.getData());
				System.out.println("Client say : " + msg);
				
				while(true){
					//Send back to client as an echo
					outBuf = msg.getBytes();
					outPacket = new DatagramPacket(outBuf, 0, outBuf.length,
													client_address, client_port);
					socket.send(outPacket);
					
					//Wait for another messages
					inBuf = new byte[256];
					inPacket = new DatagramPacket(inBuf, inBuf.length);
					socket.receive(inPacket);
					msg = new String(inPacket.getData());
					System.out.println("Client say : " + msg);
				}
			}
			
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
