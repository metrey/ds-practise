package com.seuksa.distributed.udpexample;
import java.io.*;
import java.net.*;

public class UDPSimpleClient {
	public static void main(String[] args){
		DatagramSocket socket = null;
		DatagramPacket outPacket = null;
		byte[] outBuf;
		String msg;
		
		try{
			socket = new DatagramSocket();
			msg = "hello";
			outBuf = msg.getBytes();
			outPacket = new DatagramPacket(outBuf, outBuf.length,
							InetAddress.getByName(args[0]), 9999);
			socket.send(outPacket);
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
