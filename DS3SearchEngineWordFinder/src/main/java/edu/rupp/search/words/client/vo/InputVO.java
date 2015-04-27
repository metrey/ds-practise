/**
 * 
 */
package edu.rupp.search.words.client.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.rupp.search.words.shared.vo.FileTransferVO;

/**
 * @author sok.pongsametrey
 *
 */
public class InputVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1867099771895575986L;
	private String command;
	private String ipAddress;
	private String wordToSearch;
	private List<FileTransferVO> lstFiles = new ArrayList<FileTransferVO>();
	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}
	
	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}
	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	/**
	 * @return the wordToSearch
	 */
	public String getWordToSearch() {
		return wordToSearch;
	}
	/**
	 * @param wordToSearch the wordToSearch to set
	 */
	public void setWordToSearch(String wordToSearch) {
		this.wordToSearch = wordToSearch;
	}
	/**
	 * @return the lstFiles
	 */
	public List<FileTransferVO> getLstFiles() {
		return lstFiles;
	}
	/**
	 * @param lstFiles the lstFiles to set
	 */
	public void setLstFiles(List<FileTransferVO> lstFiles) {
		this.lstFiles = lstFiles;
	}
	
	public void addFile (FileTransferVO fileVO) {
		this.lstFiles.add(fileVO);
	}
}
