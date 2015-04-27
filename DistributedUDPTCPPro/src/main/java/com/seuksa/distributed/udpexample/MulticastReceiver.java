package com.seuksa.distributed.udpexample;
import java.net.*;
import java.io.*;

public class MulticastReceiver {
	public static void main(String[] args){
		MulticastSocket socket = null;
		DatagramPacket inPacket = null;
		byte[] inBuf = new byte[256];
		try{
			socket = new MulticastSocket(9999);
			InetAddress address = InetAddress.getByName("224.2.2.3");
			socket.joinGroup(address);
			
			while(true){
				inPacket = new DatagramPacket(inBuf, inBuf.length);
				socket.receive(inPacket);
				String msg = new String(inPacket.getData());
				System.out.println("Msg : " + msg);
			}
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
