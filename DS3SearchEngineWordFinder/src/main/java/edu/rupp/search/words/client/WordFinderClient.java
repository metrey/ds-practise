/**
 * 
 */
package edu.rupp.search.words.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import edu.rupp.search.words.client.vo.InputVO;
import edu.rupp.search.words.common.IConstant;
import edu.rupp.search.words.common.util.ZipHelper;
import edu.rupp.search.words.shared.IRemoteSearch;
import edu.rupp.search.words.shared.vo.FileTransferVO;
import edu.rupp.search.words.shared.vo.MapReduceVO;


/**
 * @author sok.pongsametrey
 *
 */
public class WordFinderClient implements IConstant {

	/**
	 * 
	 * @param args
	 */
	private static final String MSG_INPUT_INFO = String.valueOf(new StringBuffer("Command available:")
		.append("(1: Put, 2: Search, 0: Quit).\n")
		.append("Type word (Ex: Search) or its number (Ex: 1): ")
			);
	
	private static long startTimer = 0;
	private static String [] ipAddresses = null;
	private static Integer servicePort = null;
	private static String clientIP = null;
	
	private static int threadPoolSize = 4;
	private static int threadPoolKeepaliveTime = -1;
	private static int commitThreshold = 8;
	
	private static PooledExecutor threadPool = null;
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
		System.out.println("++    WELCOME TO WORDING SEARCH ENGINE   ++");
		System.out.println("++ ------------------------------------- ++");
		System.out.println("++ Assignment - Distributed System, 2011 ++");
		System.out.println("++ Lecturer: Mr. Taing Nguonly, RUPP     ++");
		System.out.println("++ Team:   Mr. SOK Pongsametrey          ++");
		System.out.println("++         Mr. Thou Bunhann              ++");
		System.out.println("++         Mr. Thy Poty                  ++");
		System.out.println("++         Mr. Cheap Eng                 ++");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
		System.out.println();
		
		final int PORT = 1099;
		ipAddresses = args[0].split(";");
		servicePort = PORT;
		try {
			servicePort = Integer.valueOf(args[1]);
		} catch (Exception e) {
			servicePort = PORT;
		}
		if (ipAddresses == null || ipAddresses.length == 0) {
			throw new IllegalStateException("Please input all server IP address separated by semi-colon ';'");
		} else {
			IRemoteSearch rs = null;
			boolean canConnect = true;
			List<String> copyIPs = new ArrayList<String>();
			for (String ip : ipAddresses) {
				try {
					rs = (IRemoteSearch) Naming.lookup("rmi://" + ip + ":" + servicePort + "/WordFinderServer");
					if (rs == null) {
						canConnect = false;
					} else {
						rs.verifyDir();
					}
				} catch (RemoteException e) {
					canConnect = false;
				} catch (MalformedURLException e) {
					canConnect = false;
				} catch (NotBoundException e) {
					canConnect = false;
				} finally {
					if (!canConnect) {
						System.out.println("Can't connect to IP: " + ip + " - no server resource so remove it");
						canConnect = true;
					} else {
						copyIPs.add(ip);
					}
				}
			}
			ipAddresses = copyIPs.toArray(new String[copyIPs.size()]);
			if (ipAddresses.length == 0) {
				System.out.println("No connection to server, please check your configuraiton");
				return;
			}
			System.out.println("Found IPs: " + ipAddresses.length);
		}
		threadPoolSize = ipAddresses.length;
		
		try {
			InetAddress inetAddr = InetAddress.getLocalHost();
			clientIP = inetAddr.getHostAddress();
			
			InputStreamReader converter = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(converter);
			String curLine = "";

			while (!("quit".equals(curLine))) {
				System.out.print(MSG_INPUT_INFO);
				curLine = in.readLine();
				
				if (curLine == null 
						|| "".equals(curLine.trim())) {
					continue;
				} else {
					curLine = curLine.trim();
				}
				System.out.println("");
				System.out.println("---------------------------------------------------------");
				String command = curLine.trim().toLowerCase();

				try {
					if ("1".equals(command) ||
							COMMAND_PUT_FILES.equals(command)) {
						WordFinderClient.put();						
					} else if ("2".equals(command) ||
							COMMAND_SEARCH_WORD.equals(command)) {
						WordFinderClient.search();
					} else if ("0".equals(command) ||
							COMMAND_QUIT.equals(command)) {
						System.out.println("BYE BYE!");
					} else {
						System.out.println("Command: [" + command + "] is not supported currently.");
					}	
				} catch (IllegalStateException eState) {
					System.out.println(" > Invalid command usage: ");
					System.out.println("  # " + eState.getMessage());
					System.out.println(" > TRY AGAIN!");
				} finally {
					System.out.println(" =====================================");
				}
			}
		} catch (MalformedURLException e) {			
			e.printStackTrace();
			errorCode = 1;
		} catch (RemoteException e) {
			e.printStackTrace();
			errorCode = 1;
		} catch (NotBoundException e) {
			e.printStackTrace();
			errorCode = 1;
		} catch (Exception e) {
			e.printStackTrace();
			errorCode = 1;
		} finally {
			exit(errorCode);
		}
		
	} // end of main
	
	private static void put () throws RemoteException, Exception {
		long start = System.nanoTime();
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		System.out.print("Input directory: ");
		String input = in.readLine();

		if (input == null || "".equals(input)) {
			System.out.println("Please input word to search");
			return;
		}
		
		System.out.println();
		List<InputVO> lstInput = new ArrayList<InputVO>();
		
		InputVO inputVO = new InputVO();
		inputVO.setCommand(COMMAND_PUT_FILES);
		File dir = new File (input);
		long totalFileSize = 0;
		int ipIndex = 0;
		if (!dir.isDirectory()) {
			if (dir.isFile()) {
				System.out.println("You are input a file");
				totalFileSize += dir.length();
				inputVO.addFile(buildFileVO(dir));
				inputVO.setIpAddress(ipAddresses[0]);
				lstInput.add(inputVO);
				inputVO = new InputVO();
				inputVO.setCommand(COMMAND_PUT_FILES);
			} else {
				System.out.println("You are input invalid directory");
				return;
			}			
		} else {
			File [] allFiles = dir.listFiles();
			int fileSize = allFiles.length;
			int ind = 0;
			
			commitThreshold = fileSize / threadPoolSize;
			if (commitThreshold == 0) commitThreshold = 1;
			
			for (File f : allFiles) {
				ind++;
				totalFileSize += f.length();
				inputVO.addFile(buildFileVO(f));
				if (ind % commitThreshold == 0) {
					inputVO.setIpAddress(ipAddresses[ipIndex]);
					ipIndex++;
					lstInput.add(inputVO);
					inputVO = new InputVO();
					inputVO.setCommand(COMMAND_PUT_FILES);
					ind = 0;
				}
				if (ipIndex == threadPoolSize) {
					ipIndex = 0;
				}
			}
		}
        
		if (inputVO != null && inputVO.getLstFiles().size() > 0) {
			inputVO.setIpAddress(ipAddresses[ipIndex]);
			lstInput.add(inputVO);
		}
		try {
			threadPool = new PooledExecutor(threadPoolSize);
			// shorten keep alive time to have a more rapid termination
			threadPool.setKeepAliveTime(threadPoolKeepaliveTime);
			// do not execute any task in the main thread because the database
			// connection is already in use in the main thread
			threadPool.waitWhenBlocked();

			WordFinderClientThread thread = null;
			System.out.println("# Processing with all total file size: " + totalFileSize + " bytes");
			if (lstInput != null) {
				for (InputVO processInputVO : lstInput) {
					thread = new WordFinderClientThread(processInputVO.getIpAddress(), servicePort, processInputVO);
					threadPool.execute(thread);
				}
			}

			System.out.println("is main thread alive "
					+ Thread.currentThread().isAlive());

			/**
			 * Terminate threads after processing all elements currently in
			 * queue. Any tasks entered after this point will be discarded. A
			 * shut down pool cannot be restarted.
			 **/
			threadPool.shutdownAfterProcessingCurrentlyQueuedTasks();

			/** Wait for a shutdown pool to fully terminate. */
			threadPool.awaitTerminationAfterShutdown();

		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new Exception("InterruptedException", e);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}		
		
		long time = System.nanoTime() - start;
		System.out.println("Transfer files and process MapReduce spent " + (time / 1e9) + " seconds");
	}
	
	private static FileTransferVO buildFileVO (File file) throws Exception{
		FileTransferVO fileVO = new FileTransferVO();
		File dir = file.getParentFile();
		String directory = dir.getPath();
		
		fileVO.setClientIP(clientIP);
		fileVO.setFilename(file.getName());
		
		String zipFileName = directory + "/" + file.getName() + ".zip";
		String inputFile = directory + "/" + file.getName();
		ZipHelper.doZip(inputFile, zipFileName);
		fileVO.setEncoded64File(ZipHelper.transformedInputDataToBase64Character(zipFileName));
		System.out.println("Base 64 encoded file to " + fileVO.getEncoded64File().length() + " characters");
		File tmp = new File(zipFileName);
		boolean success = tmp.delete();
		if (!success) {
			System.out.println("Can't delete zip file: " + zipFileName);
		}
		return fileVO;
	}
	
	private static void search () throws RemoteException, Exception {
		long start = System.nanoTime();
		System.out.println(".:FIND A WORD OCCURED IN THE FILES:.");
		System.out.println("------------------------------------");
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		System.out.print("Search word: ");
		String input = in.readLine();

		if (input == null || "".equals(input)) {
			System.out.println("Please input word to search");
			return;
		}
		List<WordFinderClientThread> lstThread = new LinkedList<WordFinderClientThread>();
		InputVO inputVO = new InputVO();
		inputVO.setCommand(COMMAND_SEARCH_WORD);
		inputVO.setWordToSearch(input);
		for (String ip : ipAddresses) {
			WordFinderClientThread thread = new WordFinderClientThread(ip, servicePort, inputVO);
			thread.start();
			lstThread.add(thread);
		}
				
		try {
			for (WordFinderClientThread thread : lstThread) {
	            thread.join();
			}
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
		
		MapReduceVO mainMapReduceVO = new MapReduceVO();
		for (WordFinderClientThread thread : lstThread) {
			MapReduceVO mapVO = thread.getMapReduceVO();
			mainMapReduceVO.reduce(mapVO.getMapReduce());			
		}
		System.out.println("WORDING LIST:");
		System.out.println("---------------------------------------------------------");
		System.out.println("Word\tOccurance");
		System.out.println(mainMapReduceVO.toString());
		System.out.println();
		long time = System.nanoTime() - start;
		System.out.println("Search on RMI and on DB spent " + (time / 1e9) + " seconds");
	}
	
	
	/**
	 * Check program syntax
	 * @param args
	 * @return
	 */
	private static boolean checkArgs(String[] args){
		if(args.length < 2){
			System.out.println("Wrong syntax, you need to connect to server as: java DS3SEWordFinderClient <IPs> <PORT>");
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
            System.out.println("Client process completed (" + executionTime.toString() + ')');
            System.exit(0);
        } else {
        	System.out.println("Some errors were raised. (" + executionTime.toString() + ")");
            System.exit(res);
        }
    }
}
