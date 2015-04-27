/**
 * 
 */
package edu.rupp.search.words.server.vo;

import java.io.Serializable;

import edu.rupp.search.words.server.dao.SEWordFinderDAO;

/**
 * @author sok.pongsametrey
 *
 */
public class ParamVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7908392430591096239L;
	private String serverDir;
	private String backupDir;
	private String serverIP;
	private int serverPort;
	private SEWordFinderDAO wordFinderDao;
	/**
	 * @return the serverDir
	 */
	public String getServerDir() {
		return serverDir;
	}
	/**
	 * @param serverDir the serverDir to set
	 */
	public void setServerDir(String serverDir) {
		this.serverDir = serverDir;
	}
	
	/**
	 * @return the backupDir
	 */
	public String getBackupDir() {
		return backupDir;
	}
	/**
	 * @param backupDir the backupDir to set
	 */
	public void setBackupDir(String backupDir) {
		this.backupDir = backupDir;
	}
	/**
	 * @return the serverIP
	 */
	public String getServerIP() {
		return serverIP;
	}
	/**
	 * @param serverIP the serverIP to set
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	/**
	 * @return the serverPort
	 */
	public int getServerPort() {
		return serverPort;
	}
	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	/**
	 * @return the wordFinderDao
	 */
	public SEWordFinderDAO getWordFinderDao() {
		return wordFinderDao;
	}
	/**
	 * @param wordFinderDao the wordFinderDao to set
	 */
	public void setWordFinderDao(SEWordFinderDAO wordFinderDao) {
		this.wordFinderDao = wordFinderDao;
	}
	
}
