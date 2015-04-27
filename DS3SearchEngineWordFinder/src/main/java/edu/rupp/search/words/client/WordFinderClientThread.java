/**
 * 
 */
package edu.rupp.search.words.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import edu.rupp.search.words.client.vo.InputVO;
import edu.rupp.search.words.common.IConstant;
import edu.rupp.search.words.shared.IRemoteSearch;
import edu.rupp.search.words.shared.vo.FileTransferVO;
import edu.rupp.search.words.shared.vo.MapReduceVO;
import edu.rupp.search.words.shared.vo.StatusVO;

/**
 * @author sok.pongsametrey
 *
 */
public class WordFinderClientThread extends Thread {

	private IRemoteSearch remoteSearch;
	private String ipAddress;
	private Integer port;
	private MapReduceVO mapReduceVO = new MapReduceVO();
	private InputVO inputVO;
	private StatusVO statusVO = new StatusVO();
	
	public WordFinderClientThread (String ipAddress, Integer port, InputVO inputVO) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.inputVO = inputVO;
	}
	
	public void run() {
		String threadName = Thread.currentThread().getName();   
		System.out.println("STARTING " + threadName + " with command " + this.inputVO.getCommand());
		
		try {
			synchronized (this) {
				remoteSearch = (IRemoteSearch) Naming.lookup("rmi://" + ipAddress + ":" + port + "/WordFinderServer");
				
				if (remoteSearch == null) {
					System.out.println("No connection to host: " + ipAddress);
					return;
				}
			}
		} catch (RemoteException e) {
			System.out.println("RemoteException - No connection to host: " + ipAddress + " : " + e.getMessage());
			return;
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException - No connection to host: " + ipAddress + " : " + e.getMessage());
			return;
		} catch (NotBoundException e) {
			System.out.println("NotBoundException - No connection to host: " + ipAddress + " : " + e.getMessage());
			return;
		}
		
		try {	
			if (IConstant.COMMAND_PUT_FILES.equals(this.inputVO.getCommand())) {
				System.out.println("# To process fles ["+ this.ipAddress +"]: " + this.inputVO.getLstFiles().size());
				for (FileTransferVO fileVO : this.inputVO.getLstFiles()) {
					remoteSearch.transfer(fileVO);
				}
			} else if (IConstant.COMMAND_SEARCH_WORD.equals(this.inputVO.getCommand())) {
				this.mapReduceVO = remoteSearch.search(this.inputVO.getWordToSearch());
				this.statusVO.setOK(true);
				if (this.mapReduceVO == null) {
					this.statusVO.setOK(false);
					this.statusVO.setMessage("Result not found with the search: " + this.inputVO.getWordToSearch());
				} else {
					this.statusVO.setMessage("Found result: " + this.mapReduceVO.getMapReduce().size());
				}
				
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	/**
	 * @return the mapReduceVO
	 */
	public synchronized MapReduceVO getMapReduceVO() {
		return mapReduceVO;
	}

	/**
	 * @return the statusVO
	 */
	public StatusVO getStatusVO() {
		return statusVO;
	}
	

}
