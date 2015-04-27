/**
 * 
 */
package edu.rupp.search.words.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import edu.rupp.search.words.common.util.ZipHelper;
import edu.rupp.search.words.server.dao.SEWordFinderDAO;
import edu.rupp.search.words.server.vo.ParamVO;
import edu.rupp.search.words.shared.IRemoteSearch;
import edu.rupp.search.words.shared.vo.FileTransferVO;
import edu.rupp.search.words.shared.vo.MapReduceVO;
import edu.rupp.search.words.shared.vo.StatusVO;

/**
 * @author sok.pongsametrey
 *
 */
public class WordFinderSever extends UnicastRemoteObject implements IRemoteSearch {

	private ParamVO paramVO = null;
	
	int threadPoolSize = 8;
	int threadPoolKeepaliveTime = -1;
	int commitThreshold = 500;
	private PooledExecutor threadPool = null;
	SEWordFinderDAO wordFinderDAO = null;
	
	protected WordFinderSever(ParamVO paramVO) throws RemoteException {
		super();
		this.paramVO = paramVO;
		this.wordFinderDAO = this.paramVO.getWordFinderDao();
		
		try {
			this.wordFinderDAO.createDBModel();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RemoteException("Database could not create", e.getCause());
		}
		
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public StatusVO transfer(FileTransferVO fileVO) throws RemoteException {
		if (fileVO == null
				|| fileVO.getEncoded64File() == null 
				|| "".equals(fileVO.getEncoded64File())) {
			throw new IllegalStateException("No file object found");
		}
		long start = System.nanoTime();
		List<String> lstFiles = null;
		String message = "";
		StatusVO statusVO = new StatusVO ();
		System.out.println("Recieving and extracting a zip file: " + fileVO.getFilename());
		try {
			lstFiles = ZipHelper.decode64CharacterAndUnzipFile(fileVO.getEncoded64File(), this.paramVO.getServerDir());
			
			if (lstFiles != null
					&& lstFiles.size() > 0) {
				for (String file : lstFiles) {
					System.out.println(file);
					message = message + "# " + file + " ";
					this.process(this.paramVO.getServerDir() + "/" + file);
				}
				statusVO.setOK(true);
				this.wordFinderDAO.saveFile(fileVO);
			} else {
				message = "No file has been sent to the server: " + paramVO.getServerIP();
				statusVO.setOK(false);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RemoteException("SQLException", e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("Exception", e);
		} finally {
			
		}
		long time = System.nanoTime() - start;
		message = message + "\n" + "Transfer and do mapreduce spent " + (time / 1e9) + " seconds";
		statusVO.setMessage(message);
		statusVO.toString();
		System.out.println("Transfer and do mapreduce spent " + (time / 1e9) + " seconds");
		return statusVO;		
	}

	/**
	 * 
	 */
	public MapReduceVO search(String word) throws RemoteException {
		System.out.println(".:FIND A WORD [" + word + "]:.");
		System.out.println("-------------------------------");
		try {
			return this.wordFinderDAO.searchWord(word);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("ERROR DURING SEARCH WORD: " + e.getMessage(), e.getCause());
		}
	}

	/**
	 * 
	 */
	public StatusVO verifyDir() throws RemoteException {
		File file = new File(paramVO.getServerDir());
		File [] allFiles = file.listFiles();
		StatusVO statusVO = new StatusVO();
		statusVO.setOK(true);
		
		if (allFiles == null
				|| allFiles.length == 0) {
			statusVO.setOK(false);
			statusVO.setMessage("No file found in this server: " + this.paramVO.getServerIP());
			return statusVO;
		}
		String message = "";
		for (File f : allFiles) {
			message = message + "# " + f.getName() + " ";
		}
		statusVO.setMessage(message);
		return statusVO;
	}
	
	private void process (String filename) throws RemoteException {
		long start = System.nanoTime();
		threadPool = new PooledExecutor(threadPoolSize);
        // shorten keep alive time to have a more rapid termination
        threadPool.setKeepAliveTime(threadPoolKeepaliveTime);
        // do not execute any task in the main thread because the database
        // connection is already in use in the main thread
        threadPool.waitWhenBlocked();		
		
		// read file
        List<String> lstLines = new ArrayList<String>();
        int counter = 0;
        WordFinderSeverThread thread = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = br.readLine()) != null) {				
				lstLines.add(line);
				counter++;
				
				if (counter % commitThreshold == 0) {
					thread = new WordFinderSeverThread(lstLines, this.paramVO);
					threadPool.execute(thread);
					lstLines = null;
					lstLines = new ArrayList<String>();
				}
				
			}
			
			if (lstLines != null && lstLines.size() > 0) {
				thread = new WordFinderSeverThread(lstLines, this.paramVO);
				threadPool.execute(thread);
				lstLines = null;
			}
			
			br.close();
			
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RemoteException("FileNotFoundException", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RemoteException("IOException", e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RemoteException("InterruptedException", e);
		} finally {
			File file = new File(filename);
			File dir = new File(paramVO.getBackupDir());
			// Move file to new directory
			boolean success = file.renameTo(new File(dir, file.getName()));			
			if (!success) {
			    // File was not successfully moved
				System.out.println("File was not successfully moved to dir:" + paramVO.getBackupDir());
			}
			success = file.delete();
			if (!success) {
				System.out.println("Failed to delete file:" + filename);
			}
			long time = System.nanoTime() - start;
			System.out.println("Do mapreduce spent " + (time / 1e9) + " seconds");
		}
 
	}

}
