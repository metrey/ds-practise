package com.seuksa.distributed.tcpexample;

import java.net.*;
import java.io.*;

public class TCPSimpleClient {
	public static void main(String[] args){
		try{
			Socket clientSocket = new Socket("127.0.0.1", 9999);
			
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
