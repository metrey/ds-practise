package com.seuksa.distributed.udpexample;
import java.io.*;
import java.net.*;

public class MulticastMovieServer {

   static public void main(String args[]) {
     DatagramSocket socket = null;
     String[] files = {"duke0.gif","duke1.gif","duke2.gif","duke3.gif"};
     try {
        InetAddress grupo = InetAddress.getByName("224.2.2.3");
        socket = new DatagramSocket();
        while (true) {
                for (int i=0; i<4; i++) {
                   File f = new File(files[i]);
                   int size = (int)f.length();
                   byte[] buf = new byte[size]; 
				   FileInputStream inFile = new FileInputStream(files[i]);
				   int b= inFile.read(buf,0,size);
				   if (b != size) continue;
				   DatagramPacket packet = new DatagramPacket(buf, buf.length, grupo, 4446);
				   System.out.println("Sending image in file : "+files[i]);
				   socket.send(packet);
				   try {
					  Thread.sleep(300); 
				   }catch (InterruptedException e) {}
		        }
          }
       } catch (IOException e) {
                e.printStackTrace();
       }
   }
}



