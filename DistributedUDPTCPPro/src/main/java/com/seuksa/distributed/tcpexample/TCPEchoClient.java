package com.seuksa.distributed.tcpexample;

import java.net.*;
import java.io.*;
import java.util.*;

public class TCPEchoClient {
	public static void main(String[] args){
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		
		try{
			//Connect to server
			socket = new Socket(args[0], 8888);
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			Scanner kbd = new Scanner(System.in);
			String kbd_msg;
			String msg;
			while(true){
				System.out.print("Client : ");
				kbd_msg = kbd.nextLine();
				
				out.println(kbd_msg);
				
				if(kbd_msg.equals("quit")){
					break;
				}
				
				msg = in.readLine();
				System.out.println("Server : " + msg);
			}
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
}
