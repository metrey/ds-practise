/**
 * 
 */
package edu.rupp.search.words.shared.vo;

import java.io.Serializable;

/**
 * @author sok.pongsametrey
 *
 */
public class StatusVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7853893304636270024L;
	
	private boolean isOK;
	private String message;
	/**
	 * @return the isOK
	 */
	public boolean isOK() {
		return isOK;
	}
	/**
	 * @param isOK the isOK to set
	 */
	public void setOK(boolean isOK) {
		this.isOK = isOK;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString() {
		System.out.println("# Status: " + (this.isOK? "OK" : "KO"));
		System.out.println("# Message: " + this.message);
		return message;
	}

}
