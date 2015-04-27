/**
 * 
 */
package edu.rupp.filesystem.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

import edu.rupp.filesystem.common.FTPCommand;

/**
 * @author <a href="mailto:metreysk@gmail.com">Pongsametrey SOK</a>
 * @version 1.0
 */
public class FTPFileClient {
	
	private static final String MSG_INPUT_INFO = "Input command (get <filename> | put <full_file_path> | list | help | quite [to exit]): ";
	private static long startTimer = 0;
	//private static final String MSG_WELCOME = "Welcome to MTR FTP Server";
	
	/**
	 * @param args
	 */
	public static void main(String[] args){
		startTimer = System.currentTimeMillis();
		int errorCode = 0;
		if(!checkArgs(args)){
			return;
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++    WELCOME TO MTR FTP FILE CLIENT     ++");
		System.out.println("++ ------------------------------------- ++");
		System.out.println("++ Assignment - Distributed System, 2011 ++");
		System.out.println("++ Lecturer: Mr. Taing Nguonly, RUPP     ++");
		System.out.println("++ Programmed by: Mr. SOK Pongsametrey   ++");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
		System.out.println();
		
		Socket socket = null;
		final int PORT = 9999;
		String ip = args[0];
		try {
			socket = new Socket(ip, PORT);
			socket.setSoTimeout(60*1000); //Wait for 60s for reading timeout
			System.out.println(" # Connected to server: " + socket.getInetAddress());
			System.out.println();
			
			InputStreamReader converter = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(converter);
			String curLine = "";
			while (!(FTPCommand.QUIT.toString().equals(curLine))) {
				System.out.println(MSG_INPUT_INFO);
				curLine = in.readLine();
				
				if (curLine == null 
						|| "".equals(curLine.trim())) {
					continue;
				} else {
					curLine = curLine.trim();
				}
				System.out.println("");
				System.out.println(" ===================================== ");
				String[] split = curLine.split(" ");
				String command = split[0].trim().toLowerCase();
				String request = "";
				if (split.length > 1) {
					request = split[1];
				}
				FTPCommand FTP = FTPCommand.getFtpCommand(command);
				System.out.println(" > Your command: " + command + " " + request);
				try {
					if (FTPCommand.GET.equals(FTP)) {
						FTPFileClient.get(socket, request);
					} else if (FTPCommand.PUT.equals(FTP)) {
						FTPFileClient.put(socket, request);
					} else if (FTPCommand.LIST.equals(FTP)) {
						FTPFileClient.list(socket);
					} else if (FTPCommand.HELP.equals(FTP)) {
						FTPFileClient.help();
					} else if (FTPCommand.QUIT.equals(FTP)) {
						FTPFileClient.quit(socket);
					} else {
						System.out.println("Command: [" + command + " " + request + "] is not supported currently.");
					}	
				} catch (IllegalStateException eState) {
					System.out.println(" > Invalid command usage: ");
					System.out.println("  # " + eState.getMessage());
					System.out.println(" > TRY AGAIN!");
				} finally {
					System.out.println(" =====================================");
				}
			}
		} catch(IOException ioe) {
			System.out.println(ioe);
			errorCode = 1;
		} finally {
			exit(errorCode);
		}
		
	} // end of main
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
			out.println("" + FTPCommand.PUT.getUsage());
			// provide filename to server
			out.println(filename);
			// provide filesize to server
			out.println(filesize);
			// prepare input stream to server
			OutputStream outSocket = socket.getOutputStream();
			FileInputStream inFile = new FileInputStream(file);
			byte[] buf = new byte[1024];
			int b; 
			while((b=inFile.read(buf, 0, 1024)) != -1){
				outSocket.write(buf, 0, b);
			}
			inFile.close();
			long status = Long.valueOf(in.readLine());
			if (status < 0) {
				System.out.println(" > File sending got interrupted, please send again.");
				return;
			}
			System.out.println(" > File sent successfully to server.");
		} catch (IOException ioe) {
			System.out.println(ioe);
		} finally  {
			System.out.println();
			System.out.println(" END OF COMMAND: PUT");
		}
	} // end of put
	
	/**
	 * 
	 * @param filename
	 */
	private static void get (Socket socket, String filename) {
		if (filename == null
				|| "".equals(filename)) {
			throw new IllegalStateException(" > Invalid syntax to get a file from server: get <filename>");
		}
		System.out.println(" _____________________________");
		System.out.println("         GET COMMAND          ");
		System.out.println("  Get a file from FTP Server  ");
		System.out.println(" _____________________________");
		System.out.println();
		System.out.println(" # GET " + filename);
		
		String currentDir = System.getProperty("user.dir") + "/clientsaved";
		// make sure the client dir exists
		File f = new File(currentDir);
		if (!f.exists()) {
			f.mkdir();
		}
		
		String savedfilename = currentDir + "/" + filename;
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			// inform server the FTP command to use
			out.println("" + FTPCommand.GET.getUsage());
			// Send requested filename to server
			out.println(filename);
			
			// Wait for server replies whether file exists or returns file size
			long filesize = Long.parseLong(in.readLine());
			if (filesize < 0) {
				System.out.println(" > File [" + filename + "] requested does not exists, try command [list] to check available files in the server");
				return;
			}
			
			System.out.println(" # File size = " + filesize + " bytes");
			
			File file = new File(savedfilename);
			if (file.exists()
					&& file.isFile()) {
				savedfilename = currentDir + "/" + System.currentTimeMillis() 
						+ "-" + filename;
			}
			
			// request file sending
			out.println("GIVE_ME_FILE");
			
			System.out.println("");
			System.out.println(" > Start receiving " + filename + " from server");
			System.out.println(" # File will save at " + savedfilename);
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
				System.out.println(" > Receiving completed!");				
			} catch(SocketTimeoutException ste) {
				System.out.println(" > [Error] Receiving timeout!!!!");
			} finally {
				outFile.close();
				System.out.println("");
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
		} finally {
			System.out.println();
			System.out.println(" END OF COMMAND: GET");
		}
	} // end of get
	
	/**
	 * List files from server
	 * @param socket
	 */
	private static void list (Socket socket) {
		PrintWriter out = null;
		BufferedReader in = null;
		System.out.println(" _____________________________");
		System.out.println("         LIST COMMAND         ");
		System.out.println(" List all files in FTP Server ");
		System.out.println(" _____________________________");
		System.out.println();
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			// inform server the FTP command to use
			out.println("" + FTPCommand.LIST.getUsage());
			
			long nbOfFiles = Long.valueOf(in.readLine());
			if (nbOfFiles < 0) {
				System.out.println(" > No any file found in server");
				return;
			}
			String availableFiles = in.readLine();
			System.out.println(" File(s) in server: " + nbOfFiles);
			// I split each filename by a tab (\t) at server
			String [] fileSplit = availableFiles.split("\t");
			for (String filenameTmp : fileSplit) {
				System.out.println(" # " + filenameTmp);
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
		} finally {
			System.out.println();
			System.out.println(" END OF COMMAND: LIST");
		}
	} // end of list
	
	/**
	 * Quit command
	 * @param socket
	 */
	private static void quit(Socket socket) {
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			// inform server the FTP command to use
			out.println("" + FTPCommand.QUIT.getUsage());
			long status = Long.valueOf(in.readLine());
			
			if (status == 1) {
				System.out.println(" > Disconnected from server successfully!");
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
		} finally {
			System.out.println();
			System.out.println(" END OF COMMAND: QUIT");
		}
	} // end of quit
	
	/**
	 * Provide user an easy way to look for all syntaxes and info
	 */
	private static void help () {
		System.out.println(" _____________________________");
		System.out.println("         LIST COMMAND         ");
		System.out.println(" List all files in FTP Server ");
		System.out.println(" _____________________________");
		System.out.println();
		System.out.println("How to use: ");
		System.out.println(" . FTP Server should be started before ");
		System.out.println(" . Pre-connection to server: java FTPFileClient <IP> ");
		System.out.println("Available FTP commands: ");
		System.out.println(" . Get file from server: get <filename>");
		System.out.println(" ... Where <filename> : is a filename with extension ");
		System.out.println(" . Put a file to server: put <full_file_path>");
		System.out.println(" ... Where <full_file_path> : is a full file path, Ex: C:/temp/myfile.zip");
		System.out.println(" . List all file names in server: list");
		System.out.println(" . Find help: help");
		System.out.println(" _____________________________");
		System.out.println("# About the program: ");
		System.out.println(" _____________________________");
		System.out.println(". FTP Server, Client Program for Distributed System Assignment ");
		System.out.println(". To practise on TCP Socket Programming ");
		System.out.println(". Lecturer: Mr. Taing Nguonly, RUPP, 2011 ");
		System.out.println();
		System.out.println(". Developed by: Mr. SOK Pongsametrey ");
		System.out.println(".               http://osify.com ");
	} // end of help
	
	/**
	 * Check program syntax
	 * @param args
	 * @return
	 */
	private static boolean checkArgs(String[] args){
		if(args.length < 1){
			System.out.println("Wrong syntax, you need to connect to server as: java FTPFileClient <IP> ");
			return false;
		}
		
		
		return true;
	}
	
	/**
     * Exiting with given return code
     * @param res return code
     */
    private static void exit(int res) {
        startTimer = System.currentTimeMillis() - startTimer;
        long ms = startTimer % 1000;
        startTimer = startTimer / 1000;
        long s = startTimer % 60; 
        startTimer = startTimer / 60;
        long m = startTimer % 60; 
        startTimer = startTimer / 60;
        long h = startTimer % 24; 
        long d = startTimer / 24;
        startTimer = 0;
        StringBuffer executionTime = new StringBuffer();
        executionTime.append(d).append("d ");
        executionTime.append(h).append("h ");
        executionTime.append(m).append("m ");
        executionTime.append(s).append("s ");
        executionTime.append(ms).append("ms");
        if (res == 0) {
            System.out.println("FTP Client process completed (" + executionTime.toString() + ')');
            System.exit(0);
        } else {
        	System.out.println("Some errors were raised. (" + executionTime.toString() + ")");
            System.exit(res);
        }
    }
	

}
