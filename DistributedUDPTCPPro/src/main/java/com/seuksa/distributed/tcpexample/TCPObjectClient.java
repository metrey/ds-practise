package com.seuksa.distributed.tcpexample;

import java.io.*;
import java.net.*;

public class TCPObjectClient {
	public static void main(String[] args){
		Socket socket = null;
		try{
			socket = new Socket(InetAddress.getByName(args[0]), 9999);
			
			ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
			
			MyDate dd = (MyDate)oi.readObject();
			System.out.println("MyDate = " + dd.getDate() +" MyNumber = " + dd.getNumber());
		}catch(ClassNotFoundException cnfe){
			System.out.println(cnfe);
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
