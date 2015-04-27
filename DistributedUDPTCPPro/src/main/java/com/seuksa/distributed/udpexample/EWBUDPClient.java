package com.seuksa.distributed.udpexample;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
public class EWBUDPClient extends JFrame{
   int x1 = -10, y1 = -10,x2=20,y2=20;
   byte[] buf;
   MulticastSocket ds;
   DatagramPacket dp;
   String str,arrStr[];
   public EWBUDPClient() {
      super( "Client Panel" );
      setSize( 500,500 );
      setVisible( true );
	  setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	  try{
	       buf=new byte[1024];
	  	   ds=new MulticastSocket(9999);
	  	   ds.joinGroup(InetAddress.getByName("224.2.2.3"));
  		   dp=new DatagramPacket(buf,buf.length);
  		   while(true){
	  		    ds.receive(dp);
    	  		str=new String(dp.getData(),0,dp.getLength());
			  	arrStr=str.split("#");
			  	x1=Integer.parseInt(arrStr[0]);
			  	y1=Integer.parseInt(arrStr[1]);
			  	x2=Integer.parseInt(arrStr[2]);
			  	y2=Integer.parseInt(arrStr[3]);
			  	repaint();
		 	}
	   }
	  catch(Exception ex){ex.printStackTrace();}
   }
   public void paint ( Graphics g ){
	      g.fillOval( x1, y1, x2, y2 );
	  	 }
    public static void main( String args[] ){
         EWBUDPClient application = new EWBUDPClient();
       }
}