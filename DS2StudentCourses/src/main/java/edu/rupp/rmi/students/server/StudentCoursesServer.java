/**
 * 
 */
package edu.rupp.rmi.students.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import edu.rupp.rmi.students.server.dao.StudentCoursesDAO;
import edu.rupp.rmi.students.shared.IRemoteStudent;
import edu.rupp.rmi.students.shared.vo.CourseVO;
import edu.rupp.rmi.students.shared.vo.StudentVO;

/**
 * @author sok.pongsametrey
 *
 */
public class StudentCoursesServer extends UnicastRemoteObject implements IRemoteStudent {

	private StudentCoursesDAO studentDao = null;
	protected StudentCoursesServer() throws RemoteException {
		super();
		
		studentDao = new StudentCoursesDAO();
		try {
			studentDao.createDBModel();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RemoteException("Database could not create", e.getCause());
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2106888018926183925L;

	/**
	 * 
	 */
	public void save(StudentVO studentVO) throws RemoteException {
		System.out.println(".:SAVE NEW STUDENT RECORD:.");
		System.out.println("---------------------------------------------------------");
		try {
			this.studentDao.saveStudent(studentVO);
			
			if (studentVO.getLstCourses() != null
					&& studentVO.getLstCourses().size() > 0) {
				Iterator<CourseVO> itCourse = studentVO.getLstCourses().iterator();
				while (itCourse.hasNext()) {
					CourseVO courseVO = itCourse.next();
					
					if (courseVO == null) continue;
					// missing prevent duplicate course link 
					
					studentDao.saveCourse(courseVO);
					studentDao.addLinkStudentCourse(studentVO.getStudentId(), courseVO.getCourseId());
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RemoteException("ERROR DURING SAVE STUDENT: " + e.getMessage(), e.getCause());
		}
		
	}

	public void update(StudentVO studentVO) throws RemoteException {
		System.out.println(".:UPDATE A STUDENT RECORD:.");
		System.out.println("---------------------------------------------------------");
		try {
			// find old studentVO
			//StudentVO previousStuVO = this.studentDao.selectStudentByID(studentVO.getStudentId());			
			this.studentDao.updateStudent(studentVO);
			
			if (studentVO.getLstCourses() != null
					&& studentVO.getLstCourses().size() > 0) {
				// delete all link of a student
				studentDao.deleteLinkStudentCourse(studentVO.getStudentId());
				Iterator<CourseVO> itCourse = studentVO.getLstCourses().iterator();
				while (itCourse.hasNext()) {
					CourseVO courseVO = itCourse.next();
					
					if (courseVO == null) continue;
					
					studentDao.saveCourse(courseVO);
					studentDao.addLinkStudentCourse(studentVO.getStudentId(), courseVO.getCourseId());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RemoteException("ERROR DURING SAVE STUDENT: " + e.getMessage(), e.getCause());
		}
	}

	public void delete(int studentId) throws RemoteException {
		System.out.println(".:DELETE A STUDENTS RECORD BY ID:.");
		System.out.println("---------------------------------------------------------");
		try {
			studentDao.deleteLinkStudentCourse(studentId);
			studentDao.deleteStudent(studentId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("ERROR DURING DELETE STUDENT: " + e.getMessage(), e.getCause());
		}
		
	}

	public List<StudentVO> findAll() throws RemoteException {
		System.out.println(".:LIST ALL STUDENTS RECORD:.");
		System.out.println("---------------------------------------------------------");
		try {
			return studentDao.selectAllStudents();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("ERROR DURING findAll STUDENT: " + e.getMessage(), e.getCause());
		}
	}

	public StudentVO findById(int studentId) throws RemoteException {
		System.out.println(".:FIND A STUDENTS RECORD BY ID:.");
		System.out.println("---------------------------------------------------------");
		try {
			return studentDao.selectStudentByID(studentId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("ERROR DURING findById STUDENT: " + e.getMessage(), e.getCause());
		}
	}

	public List<StudentVO> findByName(String name) throws RemoteException {
		System.out.println(".:FIND A STUDENTS RECORD BY NAME:.");
		System.out.println("---------------------------------------------------------");
		try {
			return studentDao.selectStudentByName(name);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("ERROR DURING findByName STUDENT: " + e.getMessage(), e.getCause());
		}
	}

	public List<StudentVO> findByCourse(String course) throws RemoteException {
		System.out.println(".:FIND A STUDENTS RECORD BY COURSE:.");
		System.out.println("---------------------------------------------------------");
		try {
			return studentDao.selectStudentByCourse(course);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("ERROR DURING findByName STUDENT: " + e.getMessage(), e.getCause());
		}
	}

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
			StudentCoursesServer ps = new StudentCoursesServer();
			
			LocateRegistry.createRegistry(port);
			Naming.rebind("StudentServer", ps);
			System.out.println("StudentServer is created on port [" + port + "]!!!");
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
		if(args.length < 1){
			System.out.println("Wrong syntax, you need to connect to server as: java StudentCoursesServer <PORT>");
			return false;
		}		
		
		return true;
	}
}
