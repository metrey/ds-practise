package com.seuksa.distributed.udpexample;
import java.net.*;
import java.io.*;
import java.util.*;

public class UDPClient {
	public static void main(String[] args){
		try{
			Scanner keyboard = new Scanner(System.in);
			DatagramSocket ds = new DatagramSocket();		
			while(true){
				byte[] buf = new byte[1024];
				System.out.print("client : ");
				String msg = keyboard.nextLine();	
				if(msg.equals("quit")) break;
				buf = msg.getBytes();
				InetAddress serverIP = InetAddress.getByName("127.0.0.1");
				int serverPort = 9999;
				DatagramPacket packet = new DatagramPacket(buf, buf.length, serverIP, serverPort);
				ds.send(packet);
				
				//Receiving from server
				buf = new byte[1024];
				packet = new DatagramPacket(buf, buf.length);
				ds.receive(packet);
				msg = new String(packet.getData(), 0, packet.getLength());
				System.out.println("Server says: " + msg);
			}
			ds.close();
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
