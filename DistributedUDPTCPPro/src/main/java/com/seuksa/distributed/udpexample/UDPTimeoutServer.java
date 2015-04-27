package com.seuksa.distributed.udpexample;

import java.io.*;
import java.net.*;

public class UDPTimeoutServer {
	public static void main(String[] args){
		DatagramSocket socket = null;
		DatagramPacket inPacket, outPacket;
		byte[] inBuf, outBuf;
		
		try{
			socket = new DatagramSocket(9999);
			socket.setSoTimeout(10*1000);
			inBuf = new byte[256];
			inPacket = new DatagramPacket(inBuf, inBuf.length);
			socket.receive(inPacket);
			
		}catch(SocketTimeoutException sto){
			System.out.println("Time out!");
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
