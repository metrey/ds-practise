package com.seuksa.distributed.udpexample;
import java.io.*;
import java.net.*;

public class UDPFileClient {
	public static void main(String[] args){
		DatagramSocket socket = null;
		DatagramPacket inPacket, outPacket;
		
		byte[] inBuf, outBuf;
		String strFileName = args[1];
		String strMsg;
		
		try{
			socket = new DatagramSocket();
			socket.setSoTimeout(10*1000); //Set timeout for 10s
			InetAddress server_address = InetAddress.getByName(args[0]);
			final int SERVER_PORT = 9999;
			outBuf = strFileName.getBytes();
			outPacket = new DatagramPacket(outBuf, 0, outBuf.length,
											server_address, SERVER_PORT);
			socket.send(outPacket);
			
			//Check whether file exist on server
			inBuf = new byte[256];
			inPacket = new DatagramPacket(inBuf, inBuf.length);
			socket.receive(inPacket);
			strMsg = new String(inBuf, 0, inPacket.getLength());
			if(strMsg.equals("0")){
				System.out.println("File NOT found on the server!");
				return;
			}
			
			//Receiving file size
			long filesize = Long.parseLong(strMsg);
			System.out.println("File Size = " + filesize);
			
			System.out.println("Start receiving file....");
			
			FileOutputStream outFile = new FileOutputStream("bb.pdf");
			long offset = 0;
			while(offset < filesize){
				inBuf = new byte[1024];
				inPacket = new DatagramPacket(inBuf, inBuf.length);
				socket.receive(inPacket);
				outFile.write(inBuf);
				offset += inPacket.getLength();
			}
			
			outFile.close();
			System.out.println("File is completely received!");
			
		}catch(SocketTimeoutException sto){
			System.out.println("Receive timeout");
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
