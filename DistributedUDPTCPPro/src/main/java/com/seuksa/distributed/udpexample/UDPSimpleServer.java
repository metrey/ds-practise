package com.seuksa.distributed.udpexample;
import java.net.*;
import java.io.*;

public class UDPSimpleServer {
	public static void main(String[] args){
		DatagramSocket socket = null;
		byte[] buf = new byte[256];
		
		try{
			socket = new DatagramSocket(9999);
			
			while(true){
				System.out.println("Waiting for client request....");
				DatagramPacket packet1 = new DatagramPacket(buf, buf.length);
				socket.receive(packet1);
				InetAddress address = packet1.getAddress();
				int port = packet1.getPort();
				System.out.println("I got a packet from " + address + ":" + port);
				String rcvMsg = new String(buf, 0, packet1.getLength());
				System.out.println("Message from client: " + rcvMsg);
			}
		}catch(IOException ioe){
			System.out.println(ioe);
		}
		socket.close();
	}
}
