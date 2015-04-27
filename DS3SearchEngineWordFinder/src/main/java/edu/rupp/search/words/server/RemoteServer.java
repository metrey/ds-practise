/**
 * 
 */
package edu.rupp.search.words.server;

import java.io.File;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import edu.rupp.search.words.server.dao.SEWordFinderDAO;
import edu.rupp.search.words.server.vo.ParamVO;


/**
 * @author sok.pongsametrey
 *
 */
public class RemoteServer {

	/**
	 * 
	 * 
	 * @param args
	 */
	public static void main (String[] args) {
		if(!checkArgs(args)){
			return;
		}
		final int PORT = 1099;
		int port = PORT;
		try {
			port = Integer.valueOf(args[0]);
		} catch (Exception e) {
			port = PORT;
		}
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			String ipAddress = inetAddress.getHostAddress(); 
			SEWordFinderDAO dao = new SEWordFinderDAO();
			ParamVO paramVO = new ParamVO ();
			paramVO.setServerPort(port);
			paramVO.setServerDir(args[1]);
			paramVO.setBackupDir(args[2]);
			paramVO.setServerIP(ipAddress);
			paramVO.setWordFinderDao(dao);
			
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
			
			if (!verifyParam(paramVO)) {
				throw new IllegalStateException("Invalid argument, check your server configuration");
			}
			
			WordFinderSever wfs = new WordFinderSever(paramVO);
			
			LocateRegistry.createRegistry(port);
			Naming.rebind("WordFinderServer", wfs);
			System.out.println("WordFinderServer is created on port [" + ipAddress + ":" + port + "]!!!");
			System.out.println("Waiting client to connect...");
			System.out.println("=========================================================");
			
		} catch(Exception e) {
			System.out.println(e);
			System.exit(-1);
		}
	}
	
	/**
	 * Check program syntax
	 * @param args
	 * @return
	 */
	private static boolean checkArgs(String[] args){
		if(args.length < 3){
			System.out.println("Wrong syntax, you need to connect to server as: java RemoteServer <PORT> <INPUT_DIR> <BACKUP_DIR>");
			return false;
		}		
		
		return true;
	}
	
	private static boolean verifyParam (ParamVO paramVO) {
		if ("".equals(paramVO.getServerDir())) {
			return false;
		}
		if ("".equals(paramVO.getBackupDir())) {
			return false;
		}
		
		if (paramVO.getServerDir().equals(paramVO.getBackupDir())) {
			System.out.println("Please input back directory different from input directory");
			return false;
		}
		
		File file = new File(paramVO.getServerDir());
		if (!file.exists()) {
			System.out.println("No input directory found");
			return false;			
		}
		if (!file.isDirectory()) {
			System.out.println("Input directory, it's not the directory: " + paramVO.getServerDir());
			return false;
		}
		
		file = new File(paramVO.getBackupDir());
		if (!file.exists()) {
			System.out.println("No backup directory found");
			return false;			
		}
		if (!file.isDirectory()) {
			System.out.println("Backup directory, it's not the directory: " + paramVO.getBackupDir());
			return false;
		}
		
		return true;
	}

}
