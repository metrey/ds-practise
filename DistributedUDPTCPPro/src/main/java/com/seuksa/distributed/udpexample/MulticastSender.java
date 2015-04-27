package com.seuksa.distributed.udpexample;
import java.io.*;
import java.net.*;

public class MulticastSender {
	public static void main(String[] args){
		DatagramSocket socket = null;
		DatagramPacket outPacket = null;
		byte[] outBuf;

		try{
			socket = new DatagramSocket();
			String msg = "This is multicast!";
			outBuf = msg.getBytes();
			final int PORT = 9999;
			InetAddress address = InetAddress.getByName("224.2.2.3");
			outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
			
			socket.send(outPacket);
			
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
