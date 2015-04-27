package com.seuksa.distributed.udpexample;
import java.net.*;
import java.awt.*;

public class MulticastMovieClient{

    public static void main(String[] args) throws Exception {
		MulticastSocket socket = new MulticastSocket(4446);
		InetAddress address = InetAddress.getByName("224.2.2.3"); //multicast address
		socket.joinGroup(address);
		byte[] buf = new byte[20000];	//be sure that it's long enough to hold image size
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		byte[] imagebytes = packet.getData();
		ImageViewer iv = new ImageViewer(imagebytes);
		while(true) {
		   packet = new DatagramPacket(buf, buf.length);
		   socket.receive(packet);
		   imagebytes = packet.getData();
		   iv.setImage(imagebytes);	
		}
    }
}

class ImageViewer extends Frame {

    Image image;

    ImageViewer(byte[] imb) {	//Create an image
        super("Movie");
        image = getToolkit().createImage(imb);
        setVisible(true);
        setSize(50,100);  //will call paint method
    }

    public void setImage(byte[] imb) {	//Change the image
         image = getToolkit().createImage(imb); 
         repaint();
    }
    
    public void paint(Graphics g) {
        Insets insets = getInsets();	//Show it in a frame
        g.drawImage(image, insets.left, insets.top, this); //paint the image when necessary

    } 
}