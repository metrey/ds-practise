/**
 * 
 */
package edu.rupp.filesystem.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import edu.rupp.filesystem.common.FTPCommand;

/**
 * @author <a href="mailto:metreysk@gmail.com">Pongsametrey SOK</a>
 * @version 1.0
 */
public class FTPFileServer {
	private static FTPFileServer instant;
	static ServerSocket server = null;
	private static String serverDir = "C:/tmp/ftpserver";
	private static long startTimer = 0;
	
	public static FTPFileServer getInstant () {
		if (instant == null) {
			instant = new FTPFileServer();
		}
		return instant;
	}
	/**
	 * Nested class for Worker Thread
	 *  To handle each client access in multi-thread
	 *  Each FTP commands to implement over here
	 * @author <a href="mailto:metreysk@gmail.com">Pongsametrey SOK</a>
	 * @version 1.0
	 */
	public class FTPFileServerWorkerThread implements Runnable {
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		String localServerDir = null;
		private String threadName;
		
		public FTPFileServerWorkerThread (Socket socket, String serverDir) {
			this.socket = socket;
			this.localServerDir = serverDir;
		}
		
		public void run () {
			InetAddress ipClient = socket.getInetAddress();
			String threadNameTmp = Thread.currentThread().getName() + "-"
					+ ipClient.getHostAddress();
			this.setThreadName(threadNameTmp);
			try {
				System.out.println("Start thread: " + threadName);
				System.out.println("Got connection from "
						+ ipClient.getHostAddress());
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				String command = in.readLine();

				/**
				 * Each client could do any command, until, it sent "quit"
				 * message
				 */
				do {
					FTPCommand FTP = FTPCommand.getFtpCommand(command);
					if (FTPCommand.GET.equals(FTP)) {
						get();
					} else if (FTPCommand.PUT.equals(FTP)) {
						put();
					} else if (FTPCommand.LIST.equals(FTP)) {
						list();
					}
					command = in.readLine();
					if (FTPCommand.QUIT.toString().equals(command)) {
						System.out
								.println("Server is about to disconnect this client access");
						// send response to client
						out.println(1);
					}
				} while (!"quit".equalsIgnoreCase(command));

			} catch (IOException ioe) {
				System.out.println("In thread: " + ioe);
				ioe.printStackTrace();
			} finally {
				System.out.println("Finished thread: " + threadName);
			}
		}
		
		/**
		 * GET command
		 * . Lookup in the server folder for a given filename
		 */
		private synchronized void get () {
			System.out.println("+++++++++++++++++++++++++++");
			System.out.println("++ GET FILE FROM SERVER  ++");
			System.out.println("+++++++++++++++++++++++++++");
			System.out.println();
			System.out.println("Requested in " + this.getThreadName());
			String filename;
			try {
				filename = in.readLine();
				System.out.println(" # Filename: " + filename);
				System.out.println(" # Lookup in folder: " + this.localServerDir);
				File file = new File(this.localServerDir + "/" + filename);
				long filesize = file.length();
				if(!file.exists()) {
					System.out.println(" > File does not exists on server");
					out.println("-1"); //File not exist
					return;
				} else {
					System.out.println(" # File size = " + filesize + " bytes");
					out.println(""+filesize);
				}

				if(in.readLine().equals("GIVE_ME_FILE")){
					System.out.println(" > Sending " + filename + " ...");
					OutputStream outSocket = socket.getOutputStream();
					FileInputStream inFile = new FileInputStream(this.localServerDir + "/" + filename);
					byte[] buf = new byte[1024];
					int b; 
					//long l = 0;
					while((b=inFile.read(buf, 0, 1024)) != -1){
						//l += b;
						outSocket.write(buf, 0, b);
					}
					inFile.close();
					System.out.println(" > Sending completed!");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				System.out.println("-------------------------------");
				System.out.println();
			}
		} // end of get
		/**
		 * PUT a file to server
		 * We need following input from client
		 * . filename
		 * . Input file stream: to read and write in server back
		 */
		private synchronized void put () {
			System.out.println("+++++++++++++++++++++++++++");
			System.out.println("++ PUT A FILE TO SERVER  ++");
			System.out.println("+++++++++++++++++++++++++++");
			System.out.println();
			System.out.println("Requested in " + this.getThreadName());
			
			String filename;
			String savedFilename;
			
			try {
				filename = in.readLine();
				long filesize = Long.parseLong(in.readLine());
				savedFilename = this.localServerDir + "/" + filename; 
				System.out.println(" # Receiving a file: " + filename);
				System.out.println(" # File size: " + filesize);
				System.out.println(" # Will save in server: " + savedFilename);
				System.out.println(" > Processing saving in server");
				FileOutputStream outFile = new FileOutputStream(savedFilename);
				try {
					InputStream inSocket = socket.getInputStream();
					int b; long l=0;
					byte[] buf = new byte[1024];
					while((b = inSocket.read(buf, 0, 1024)) != -1){
						l += b;
						outFile.write(buf, 0, b);
						if(l == filesize) break;
					}
					System.out.println(" > File saved in server completed!");
					out.println(1);
				} catch(SocketTimeoutException ste) {
					System.out.println("[Error] Receiving timeout!!!!");
					out.println(-1);
				} finally {
					outFile.close();
					System.out.println("");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				System.out.println("-------------------------------");
				System.out.println();
			}
		} // end of put
		
		/**
		 * List files from server
		 */
		private synchronized void list () {
			System.out.println("+++++++++++++++++++++++++++");
			System.out.println("++ LIST ALL SERVER FILES ++");
			System.out.println("+++++++++++++++++++++++++++");
			System.out.println();
			System.out.println("Requested in " + this.getThreadName());
			
			StringBuffer allFiles = new StringBuffer();
			File fDir = new File(this.localServerDir);
			File [] files = fDir.listFiles();
			if (files == null
					|| files.length <=0) {
				System.out.println(" > No any file found in server directory: " + this.localServerDir);
				out.println(-1);
				return;
			}
			System.out.println("List all files: " + files.length);
			for (File file : files) {
				System.out.println(" # " + file.getName());
				allFiles.append(file.getName()).append("\t");
			}
			// send number of files
			out.println("" + files.length);
			out.println(allFiles.toString());
			System.out.println("-------------------------------");
			System.out.println();
		} // end of list
		
		private synchronized void setThreadName (String threadName) {
			this.threadName = threadName;
		}
		
		private synchronized String getThreadName () {
			return this.threadName;
		}
		
	} // end of thread class

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		int errorCode = 0;
		if(!checkArgs(args)){
			System.out.println("Server terminated!");
			return;
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++    WELCOME TO MTR FTP FILE SERVER     ++");
		System.out.println("++ ------------------------------------- ++");
		System.out.println("++ Assignment - Distributed System, 2011 ++");
		System.out.println("++ Lecturer: Mr. Taing Nguonly, RUPP     ++");
		System.out.println("++ Programmed by: Mr. SOK Pongsametrey   ++");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
		System.out.println();
		
		Socket socket = null;
		
		try{
			server = new ServerSocket(9999);
			
			while(true){
				System.out.println("-------------------------");
				System.out.println("Wait for client to connect....");
				socket = server.accept();
				// call thread
				FTPFileServerWorkerThread mt = FTPFileServer.getInstant().new FTPFileServerWorkerThread(socket, serverDir);
				Thread newThrd = new Thread(mt);
			    newThrd.start();
			    
			}
			
		} catch(IOException ioe) {
			System.out.println(ioe);
			errorCode = 1;
		} finally {
			exit(errorCode);
		}
	}
	/**
	 * 
	 * @param args
	 * @return
	 */
	private static boolean checkArgs(String[] args){
		String tmp;
		File file = null;
		if(args.length > 1
				&& args[0] != null
				&& "".equalsIgnoreCase(args[0])){
			tmp = args[0];
			file = new File(tmp);
			
			if (file.exists() 
					&& file.isDirectory()) {
				serverDir = tmp;
				return true;
			}
		}
		
		System.out.println("If you want to specify server directory, do as (Ex: java FTPFileServer c:/tmp/server). ");
		System.out.println("Now server use default directory: " + serverDir);
		System.out.println();
		file = new File(serverDir);
		
		if (!file.exists() 
				|| !file.isDirectory()) {
			if (file.mkdirs()) {
				System.out.println(" > Default server directories created: " + serverDir);
				return true;
			} 
			System.out.println(" > Default server directories can't created: " + serverDir + ", please check to continue..");
			return false;
		}
		
		return true;
	}
	
	protected void finalize(){
		//Objects created in run method are finalized when 
		//program terminates and thread exits
		try{
			server.close();
		} catch (IOException e) {
		    System.out.println("Could not close socket");
		    System.exit(-1);
		}
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
            System.out.println("FTP Server process completed (" + executionTime.toString() + ')');
            System.exit(0);
        } else {
        	System.out.println("Some errors were raised. (" + executionTime.toString() + ")");
            System.exit(res);
        }
    }

}
