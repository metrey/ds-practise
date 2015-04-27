package com.seuksa.distributed.udpexample;
import java.io.*;
import java.net.*;
import java.util.*;

public class UDPEchoClient {
	public static void main(String[] args){
		DatagramSocket socket = null;
		DatagramPacket inPacket = null;
		DatagramPacket outPacket = null;
		byte[] inBuf, outBuf;
		final int PORT = 9999;
		String localhost = "127.0.0.1";
		
		Scanner kbd = null;				//Read from keyboard
		String msgKbd = null;
		
		try{
			InetAddress address = InetAddress.getByName(localhost); //args[0]
			socket = new DatagramSocket();
			
			System.out.println("Type anything to server...");
			
			while(true){
				//Read from keyboard
				System.out.print("Client : ");
				kbd = new Scanner(System.in);
				msgKbd = kbd.nextLine();
				
				//Convert string to byte and send to server
				outBuf = msgKbd.getBytes();
				outPacket = new DatagramPacket(outBuf, 0, outBuf.length,
												address, PORT);
				socket.send(outPacket);
				
				//Receive echo message from server
				inBuf = new byte[256];
				inPacket = new DatagramPacket(inBuf, inBuf.length);
				socket.receive(inPacket);
				
				String data = new String(inPacket.getData(), 0, inPacket.getLength());
				
				System.out.println("Server : " + data);
			}
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
