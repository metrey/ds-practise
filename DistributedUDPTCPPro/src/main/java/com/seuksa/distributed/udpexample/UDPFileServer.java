package com.seuksa.distributed.udpexample;
import java.io.*;
import java.net.*;

public class UDPFileServer {
	public static void main(String[] args){
		DatagramSocket socket = null;
		DatagramPacket outPacket = null;
		DatagramPacket inPacket = null;
		byte[] inBuf, outBuf;
		
		final int PORT = 9999;
		
		InetAddress client_address = null;
		int client_port = 0;
		
		String strFileName, strMsg;
		try{
			socket = new DatagramSocket(PORT);
			
			while(true){
				System.out.println("Wait for client request....");
				inBuf = new byte[256];
				inPacket = new DatagramPacket(inBuf, inBuf.length);
				socket.receive(inPacket);	//Wait for receiving filename
				strFileName = new String(inBuf, 0, inPacket.getLength());
				//Get client ip and port
				client_address = inPacket.getAddress();
				client_port = inPacket.getPort();
				
				//Check filename whether it exists on the server
				File file = new File(strFileName);
				if(!file.exists()){
					//file not exist on the server
					//Send back to client to inform the file doesn't exist
					strMsg = "0"; //0 = File NOT found on the server!
					outBuf = strMsg.getBytes();
					outPacket = new DatagramPacket(outBuf, 0, outBuf.length,
													client_address, client_port);
					socket.send(outPacket);
					
					System.out.println("File does not exist : " + strFileName);
					
				}else{
				
					long filesize = file.length();
					System.out.println("File Size = " + filesize);
					outBuf = ("" + filesize).getBytes();
					outPacket = new DatagramPacket(outBuf, 0, outBuf.length,
							client_address, client_port);
					socket.send(outPacket);
					
					System.out.println("File exists. Start reading file and send to client");
					FileInputStream inFile = new FileInputStream(strFileName);
					int file_pos = -1;
					outBuf = new byte[1024];
					while((file_pos = inFile.read(outBuf)) != -1){
						outPacket = new DatagramPacket(outBuf,0, outBuf.length,
														client_address, client_port);
						socket.send(outPacket);
						try{
							Thread.sleep(1); // For reducing error rate at Receiver side
						}catch(InterruptedException ie){}
					}
					inFile.close();
				}
			}
			
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
