/**
 * 
 */
package edu.rupp.search.words.shared.vo;

import java.io.Serializable;

/**
 * @author sok.pongsametrey
 *
 */
public class FileTransferVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2171711048179523589L;
	/**
	 * 
	 */
	private String clientIP;
	private String filename;
	private String encoded64File;
	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * @return the encoded64File
	 */
	public String getEncoded64File() {
		return encoded64File;
	}
	/**
	 * @param encoded64File the encoded64File to set
	 */
	public void setEncoded64File(String encoded64File) {
		this.encoded64File = encoded64File;
	}
	/**
	 * @return the clientIP
	 */
	public String getClientIP() {
		return clientIP;
	}
	/**
	 * @param clientIP the clientIP to set
	 */
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	
}
