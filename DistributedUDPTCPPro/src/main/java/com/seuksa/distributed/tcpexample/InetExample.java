package com.seuksa.distributed.tcpexample;


import java.io.*;
import java.net.*;

public class InetExample {
	 public static void main(String[] args) throws IOException {

		try {
			//asking for local ip and hostname
			InetAddress me = InetAddress.getLocalHost();
			System.out.println("My name  : "+me.getHostName());
			System.out.println("My IP    : "+me.getHostAddress());
			System.out.println("My class : "+iPClass(me.getHostAddress()));
		}catch (UnknownHostException e) {
			 System.out.println("I could not find myself !!!");
		}
		 
		BufferedReader kbd = new BufferedReader(
				new InputStreamReader(System.in));
		String name;
		while (true) {
			try {
				//read hostnames until "end" is given
				System.out.print("Hostname ? (end for break)"); 
				name = kbd.readLine();
				if (name.equals("end")) break;
				System.out.println("Making DNS lookup for "+name);
				InetAddress remote = InetAddress.getByName(name);
				System.out.println("The IP    : "+remote.getHostAddress());
				System.out.println("The Name  : "+remote.getHostName());
				System.out.println("The class : "+iPClass(remote.getHostAddress()));
			}catch (UnknownHostException e) {
				System.out.println("sorry, did not find it !!!");
			}catch (Exception e) {
				System.out.println("Problems "+e);
			}
		}
		
	}
	
	public static char iPClass(String ip) {
		int firstbyte = Integer.parseInt(ip.substring(0,ip.indexOf(".")));
		if (firstbyte < 128 ) return 'A';
		if (firstbyte < 192 ) return 'B';
		if (firstbyte < 224 ) return 'C';
		if (firstbyte < 240 ) return 'D';
		return 'E';
	}
}
