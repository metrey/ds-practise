package com.seuksa.distributed.udpexample;
import java.net.*;
import java.io.*;

public class UDPServer {
	public static void main(String[] args){
		DatagramSocket ds = null;
		try{
			ds = new DatagramSocket(9999);
			while(true){
				System.out.println("Waiting for client...");
				byte[] buf = new byte[1024];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				ds.receive(packet);
				String content = new String(packet.getData(), 0, packet.getLength());
				InetAddress clientAddress = packet.getAddress();
				int clientPort = packet.getPort();
				System.out.println("A message from client with ip " + clientAddress.getHostAddress() + ":" + clientPort);
				System.out.println("Client says: " + content);
				
				buf = new byte[1024];
				buf = content.toUpperCase().getBytes();
				packet = new DatagramPacket(buf, buf.length, clientAddress, clientPort);
				ds.send(packet);
			}
			
		}catch(IOException ioe){
			System.out.println(ioe);
			ds.close();
		}
	}
}
