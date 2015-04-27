package com.seuksa.distributed.tcpexample;

import java.net.*;

public class Lookup {
	public static void main(String[] args){
		try{
			InetAddress addr = InetAddress.getByName(args[0]);
			System.out.println("IP address: " + addr.getHostAddress());
		}catch(UnknownHostException ue){
			System.out.println("Host is unknown");
		}
	}
}
