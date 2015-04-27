/**
 * 
 */
package edu.rupp.search.words.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

import edu.rupp.search.words.shared.vo.FileTransferVO;
import edu.rupp.search.words.shared.vo.MapReduceVO;
import edu.rupp.search.words.shared.vo.StatusVO;

/**
 * @author pongsametrey.sok
 *
 */
public interface IRemoteSearch extends Remote {
	/** Test if server has receive file yet */
	public StatusVO verifyDir () throws RemoteException;
	/** Transfer file */
	public StatusVO transfer (FileTransferVO fileVO) throws RemoteException;
	/** Search word */
	public MapReduceVO search (String word) throws RemoteException;
}
