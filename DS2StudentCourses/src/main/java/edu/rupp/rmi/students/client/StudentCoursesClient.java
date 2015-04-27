/**
 * 
 */
package edu.rupp.rmi.students.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.rupp.rmi.students.shared.IRemoteStudent;
import edu.rupp.rmi.students.shared.vo.CourseVO;
import edu.rupp.rmi.students.shared.vo.StudentVO;


/**
 * @author sok.pongsametrey
 *
 */
public class StudentCoursesClient {

	/**
	 * 
	 * @param args
	 */
	private static final String MSG_INPUT_INFO = String.valueOf(new StringBuffer("Command available:")
		.append("(1: Save, 2: Update, 3: Delete, 4: FindAll, 5: FindById, 6: FindByName, 7: FindByCourse, 0: Quit).\n")
		.append("Type word (Ex: Save) or its number (Ex: 1): ")
			);
	
	private static long startTimer = 0;
	private static IRemoteStudent rs = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args){
		startTimer = System.currentTimeMillis();
		int errorCode = 0;
		if(!checkArgs(args)){
			return;
		}
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++    WELCOME TO MTR RMI STUDENT CLIENT     ++");
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++ Assignment 2  - Distributed System, 2011 ++");
		System.out.println("++ Lecturer: Mr. Taing Nguonly, RUPP, MITE  ++");
		System.out.println("++ Programmed by: Mr. SOK Pongsametrey      ++");
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println();
		
		final int PORT = 1099;
		String ip = args[0];
		int port = PORT;
		try {
			port = Integer.valueOf(args[1]);
		} catch (Exception e) {
			port = PORT;
		}
		
		try {
			rs = (IRemoteStudent) Naming.lookup("rmi://" + ip + ":" + port + "/StudentServer");
			
			InputStreamReader converter = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(converter);
			String curLine = "";

			while (!("quit".equals(curLine))) {
				System.out.println(MSG_INPUT_INFO);
				curLine = in.readLine();
				
				if (curLine == null 
						|| "".equals(curLine.trim())) {
					continue;
				} else {
					curLine = curLine.trim();
				}
				System.out.println("");
				System.out.println("---------------------------------------------------------");
				String command = curLine.trim().toLowerCase();

				try {
					if ("1".equals(command) ||
							"save".equals(command)) {
						StudentCoursesClient.save();						
					} else if ("2".equals(command) ||
							"update".equals(command)) {
						StudentCoursesClient.update();
					} else if ("3".equals(command) ||
							"delete".equals(command)) {
						StudentCoursesClient.delete();
					} else if ("4".equals(command) ||
							"findall".equals(command)) {
						StudentCoursesClient.findAll();
					} else if ("5".equals(command) ||
							"findbyid".equals(command)) {
						StudentCoursesClient.findById();
					} else if ("6".equals(command) ||
							"findbyname".equals(command)) {
						StudentCoursesClient.findByName();
					} else if ("7".equals(command) ||
							"findbycourse".equals(command)) {
						StudentCoursesClient.findByCourse();
					} else if ("0".equals(command) ||
							"quit".equals(command)) {
						System.out.println("BYE BYE!");
					} else {
						System.out.println("Command: [" + command + "] is not supported currently.");
					}	
				} catch (IllegalStateException eState) {
					System.out.println(" > Invalid command usage: ");
					System.out.println("  # " + eState.getMessage());
					System.out.println(" > TRY AGAIN!");
				} finally {
					System.out.println(" =====================================");
				}
			}
		} catch (MalformedURLException e) {			
			e.printStackTrace();
			errorCode = 1;
		} catch (RemoteException e) {
			e.printStackTrace();
			errorCode = 1;
		} catch (NotBoundException e) {
			e.printStackTrace();
			errorCode = 1;
		} catch (Exception e) {
			e.printStackTrace();
			errorCode = 1;
		} finally {
			exit(errorCode);
		}
		
	} // end of main
	
	/**
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	private static void save () throws RemoteException, Exception {
		System.out.println(".:SAVE NEW STUDENT RECORD:.");
		System.out.println("---------------------------------------------------------");

		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		String input = "";		
		try {
			StudentVO studentVo = new StudentVO();
			System.out.println("Student Name: ");
			input = in.readLine();
			studentVo.setName(input);
			
			System.out.println("Date of Birth (dd-MM-yyyy): ");
			input = in.readLine();
			Date dt = studentVo.stringToDate(input);
			studentVo.setDateOfBirth(dt);
			
			System.out.println("Courses (Separated by comma ','): ");
			input = in.readLine();
			studentVo.setLstCourses(coursesToList(input));
			
			rs.save(studentVo);
		} catch (RemoteException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	private static void update () throws RemoteException, Exception {
		System.out.println(".:UPDATE A STUDENT RECORD:.");
		System.out.println("---------------------------------------------------------");
		
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		String input = "";	
		int studentId = 0;
		try {
			
			System.out.println("Select a student Id to update: ");
			input = in.readLine();
			try {
				if (input != null && !"".equals(input)) {
					studentId = Integer.valueOf(input);
				}
			} catch (Exception e) {
				studentId = 0;
			}
			StudentVO studentVo = rs.findById(studentId);
			if (studentVo == null) {
				System.out.println("Could not find student with the ID, please give a valid ID.");
				return;
			}
			
			System.out.println("Student Name: ");
			input = in.readLine();
			studentVo.setName(input);
			
			System.out.println("Date of Birth (dd-MM-yyyy): ");
			input = in.readLine();
			Date dt = studentVo.stringToDate(input);
			studentVo.setDateOfBirth(dt);
			
			System.out.println("Courses (Separated by comma ','): ");
			input = in.readLine();
			studentVo.setLstCourses(coursesToList(input));
			
			rs.update(studentVo);
		} catch (RemoteException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	private static void delete () throws RemoteException, Exception {
		System.out.println(".:DELETE A STUDENTS RECORD BY ID:.");
		System.out.println("---------------------------------------------------------");
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		System.out.println("Student Id: ");
		String input = in.readLine();
		int studentId = 0;
		try {
			if (input != null && !"".equals(input)) {
				studentId = Integer.valueOf(input);
			}
		} catch (Exception e) {
			studentId = 0;
		}
		StudentVO studentVo = rs.findById(studentId);
		if (studentVo == null) {
			System.out.println("Could not find student with the ID, please give a valid ID.");
			return;
		}
		rs.delete(studentId);
	}
	/**
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	private static void findAll() throws RemoteException, Exception{
		System.out.println(".:LIST ALL STUDENTS RECORD:.");
		System.out.println("---------------------------------------------------------");
		display(rs.findAll());
	}
	/**
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	private static void findById() throws RemoteException, Exception {		
		System.out.println(".:FIND A STUDENTS RECORD BY ID:.");
		System.out.println("---------------------------------------------------------");
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		System.out.println("Student Id: ");
		String input = in.readLine();
		int studentId = 0;
		try {
			if (input != null && !"".equals(input)) {
				studentId = Integer.valueOf(input);
			}
		} catch (Exception e) {
			studentId = 0;
		}
		System.out.println("STUDENT LIST:");
		System.out.println("---------------------------------------------------------");
		System.out.println("ID\tName\tDateOfBirth\tCourses");
		displayStudent(rs.findById(studentId));
	}
	/**
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	private static void findByName() throws RemoteException, Exception {		
		System.out.println(".:FIND A STUDENTS RECORD BY NAME:.");
		System.out.println("---------------------------------------------------------");
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		System.out.println("Student Name: ");
		String input = in.readLine();
		
		display(rs.findByName(input));
	}
	
	/**
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	private static void findByCourse() throws RemoteException, Exception {		
		System.out.println(".:FIND A STUDENTS RECORD BY COURSE:.");
		System.out.println("---------------------------------------------------------");
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		System.out.println("Course: ");
		String input = in.readLine();
		display(rs.findByCourse(input));
	}
	
	private static void display (List<StudentVO> lstStudents) {
		System.out.println("STUDENT LIST:");
		System.out.println("---------------------------------------------------------");
		System.out.println("ID\tName\tDateOfBirth\tCourses");
		
		Iterator<StudentVO> itStu = lstStudents.iterator();
		while (itStu.hasNext()) {
			StudentVO studentVo = itStu.next();
			displayStudent(studentVo);
		}
		System.out.println("---------------------------------------------------------");
	}
	
	private static void displayStudent (StudentVO studentVo) {
		System.out.println(studentVo.getStudentId() + 
				"\t" + studentVo.getName() + 
				"\t" + studentVo.dateToString(studentVo.getDateOfBirth()) + 
				"\t" + courseBySeparator(studentVo.getLstCourses(), ",")
				);
	}
	/**
	 * 
	 * @param lstCourse
	 * @param separator
	 * @return
	 */
	private static String courseBySeparator (List<CourseVO> lstCourse, String separator) {
		
		if (lstCourse.size() == 0) return "";
		String strCourse = "";
		
		Iterator<CourseVO> itCourse = lstCourse.iterator();
		
		while (itCourse.hasNext()) {
			CourseVO courseVo = itCourse.next();
			if (!"".equals(strCourse)) {
				strCourse = strCourse + separator;
			}
			strCourse = strCourse + courseVo.getCourseName();
		}
		
		return strCourse;
	}
	
	private static List<CourseVO> coursesToList (String strCourses){
		List<CourseVO> lstCourse = new ArrayList<CourseVO>();
		
		if (strCourses == null) return lstCourse;
		
		String [] splitCourses = strCourses.split(",");
		
		for (String course : splitCourses) {
			CourseVO courseVo = new CourseVO();
			if (course != null) course = course.trim();
			courseVo.setCourseName(course);
			lstCourse.add(courseVo);
		}
		
		return lstCourse;
	}
	
	/**
	 * Check program syntax
	 * @param args
	 * @return
	 */
	private static boolean checkArgs(String[] args){
		if(args.length < 2){
			System.out.println("Wrong syntax, you need to connect to server as: java StudentCoursesClient <IP> <PORT>");
			return false;
		}
		
		
		return true;
	}
	
	/**
     * Exiting with given return code
     * @param res return code
     */
    private static void exit(int res) {
        startTimer = System.currentTimeMillis() - startTimer;
        long ms = startTimer % 1000;
        startTimer = startTimer / 1000;
        long s = startTimer % 60; 
        startTimer = startTimer / 60;
        long m = startTimer % 60; 
        startTimer = startTimer / 60;
        long h = startTimer % 24; 
        long d = startTimer / 24;
        startTimer = 0;
        StringBuffer executionTime = new StringBuffer();
        executionTime.append(d).append("d ");
        executionTime.append(h).append("h ");
        executionTime.append(m).append("m ");
        executionTime.append(s).append("s ");
        executionTime.append(ms).append("ms");
        if (res == 0) {
            System.out.println("RMI Student Client process completed (" + executionTime.toString() + ')');
            System.exit(0);
        } else {
        	System.out.println("Some errors were raised. (" + executionTime.toString() + ")");
            System.exit(res);
        }
    }
}
