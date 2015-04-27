package com.seuksa.distributed.other;
/**
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.seuksa.distributed.other.util.ZipHelper;


/**
 * @author sok.pongsametrey
 *
 */
public class MainTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Socket socket = null;
		final int PORT = 9999;
		String ip = "localhost";
		try {
			socket = new Socket(ip, PORT);
			socket.setSoTimeout(60*1000); //Wait for 60s for reading timeout
			System.out.println(" # Connected to server: " + socket.getInetAddress());
			System.out.println();
			
			//InputStreamReader converter = new InputStreamReader(System.in);
			//BufferedReader in = new BufferedReader(converter);
			//String curLine = "";
			
			String filepath = "M:/bmw/carmen_test_files/customer_update/carmen_xml/CustProfUpd_Req.xml";
			MainTester.put(socket, filepath);
			
		} catch(IOException ioe) {
			System.out.println(ioe);
		} finally {

		}

	}
	
	/**
	 * put command
	 * @param filepath
	 */
	private static void put (Socket socket, String filepath) {
		if (filepath == null
				|| "".equals(filepath)) {
			throw new IllegalStateException(" > Invalid syntax to put a file to server: put <full file path 'no space'>");
		}
		
		System.out.println(" _____________________________");
		System.out.println("          PUT COMMAND         ");
		System.out.println("   Put a file to FTP Server   ");
		System.out.println(" _____________________________");
		System.out.println();
		
		// verify given file first
		File file = new File(filepath);
		if (!file.exists()
				|| !file.isFile()) {
			throw new IllegalStateException(" > Your input file: " + filepath + " is not valid. Expected a valid file path. Please check the path.");
		}
		
		String filename = file.getName();
		long filesize = file.length();
		
		System.out.println(" # File above to send to server: " + filename);
		System.out.println(" # File location: " + file.getPath());
		System.out.println(" # File size: " + filesize + " bytes");
		
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			// inform server the FTP command to use
			out.println("put");
			// provide filename to server
			out.println(filename);
			// provide filesize to server
			out.println(filesize);
			// prepare input stream to server
			String zipFile = filename + ".zip";
			ZipHelper.doZip(filename, zipFile);
	        String inputDateBase64Character = ZipHelper.transformedInputDataToBase64Character(zipFile);
			
	        out.println("" + inputDateBase64Character);
			
			long status = Long.valueOf(in.readLine());
			if (status < 0) {
				System.out.println(" > File sending got interrupted, please send again.");
				return;
			}
			System.out.println(" > File sent successfully to server.");
		} catch (IOException ioe) {
			System.out.println(ioe);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
			System.out.println();
			System.out.println(" END OF COMMAND: PUT");
		}
	} // end of put
	

}
