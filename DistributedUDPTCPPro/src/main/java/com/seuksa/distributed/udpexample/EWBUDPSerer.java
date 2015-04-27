package com.seuksa.distributed.udpexample;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
public class EWBUDPSerer extends JFrame{
   int xvalue = -10, yvalue = -10,x2=20,y2=20;
   byte[] buf;
   DatagramSocket ds;
   DatagramPacket dp;
   String str;
   public EWBUDPSerer() {
      super( "Server Panel" );
      addMouseMotionListener(
           new MouseMotionAdapter(){
                public void mouseDragged( MouseEvent event){
                        xvalue = event.getX();
                        yvalue = event.getY();
                       try{
							ds=new DatagramSocket();
							str=xvalue+"#"+yvalue+"#"+x2+"#"+y2;
							buf=str.getBytes();
							dp=new DatagramPacket(buf,buf.length,InetAddress.getByName("224.2.2.3"),9999);
							ds.send(dp);
						}catch(Exception ex){ex.printStackTrace();}
                        repaint();
					}
                   }
            );
        setSize( 500,500 );
	    setVisible( true );
   		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
   		}
    public void paint ( Graphics g ){
         g.fillOval( xvalue, yvalue, x2, y2 );
		}
    public static void main( String args[] ){
         EWBUDPSerer application = new EWBUDPSerer();
      }
}