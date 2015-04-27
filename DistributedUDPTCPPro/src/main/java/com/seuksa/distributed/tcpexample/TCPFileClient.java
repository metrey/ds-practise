package com.seuksa.distributed.tcpexample;

import java.io.*;
import java.net.*;

public class TCPFileClient {
	public static void main(String[] args){
//		if(!checkArgs(args)){
//			return;
//		}
		
		String ip = "localhost";
		String path = "c:/tmp";
		String filename = "show_ads.js.gif";
		
		if (args != null 
				&& checkArgs(args)
				&& !"".equals(args[0])) {
			ip = args[0];
		}
		if (args != null 
				&& checkArgs(args)
				&& !"".equals(args[1])) {
			path = args[1];
		}
		if (args != null 
				&& checkArgs(args)
				&& !"".equals(args[2])) {
			filename = args[2];
		}
		
		
		String savedfilename = path + "/test_" + filename;
		filename =  path + "/" + filename;
		final int PORT = 9999;
		
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		
		try{
			socket = new Socket(ip, PORT);
			socket.setSoTimeout(3*1000); //Wait for 1s for reading timeout
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			//Send requested filename to server
			out.println(filename);
			
			//Wait for server replies whether file exists or returns file size
			long filesize = Long.parseLong(in.readLine());
			if(filesize < 0){
				System.out.println("Oop! File does not exist on server!");
				return;
			}else{
				System.out.println("File size = " + filesize + " bytes");
			}
			
			out.println("OK");
			
			System.out.println("Start receiving " + filename + " from server");
			FileOutputStream outFile = new FileOutputStream(savedfilename);
			try{
				InputStream inSocket = socket.getInputStream();
				int b; long l=0;
				byte[] buf = new byte[1024];
				while((b = inSocket.read(buf, 0, 1024)) != -1){
					l += b;
					outFile.write(buf, 0, b);
					if(l == filesize) break;
				}
				System.out.println("Receiving completed!");
			}catch(SocketTimeoutException ste){
				System.out.println("[Error] Receiving timeout!!!!");
			}finally{
				outFile.close();
			}
		}catch(IOException ioe){
			System.out.println(ioe);
		}
		
	}
	
	private static boolean checkArgs(String[] args){
		if(args.length<3){
			System.out.println("Usage: java TCPClient ip filename");
			return false;
		}
		return true;
	}
}
