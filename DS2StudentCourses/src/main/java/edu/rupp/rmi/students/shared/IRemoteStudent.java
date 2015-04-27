/**
 * 
 */
package edu.rupp.rmi.students.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import edu.rupp.rmi.students.shared.vo.StudentVO;

/**
 * @author sok.pongsametrey
 *
 */
public interface IRemoteStudent extends Remote {
	
	/** to insert student record to database */
	public void save (StudentVO studentVO) throws RemoteException;
	/** to update the existing record based on Id */
	public void update (StudentVO studentVO) throws RemoteException;
	/** to delete record based on Id */
	public void delete (int studentId) throws RemoteException;
	
	/** to get all the available students in the table */
	public List<StudentVO> findAll () throws RemoteException;
	/** get a student record based on Id given (only one record retrieved) */
	public StudentVO findById (int studentId) throws RemoteException;
	/** get a student record based on name given */
	public List<StudentVO> findByName (String name) throws RemoteException;
	/** get list of student records based on Course given (possibly many records retrieved) */
	public List<StudentVO> findByCourse (String course) throws RemoteException;
	
}
