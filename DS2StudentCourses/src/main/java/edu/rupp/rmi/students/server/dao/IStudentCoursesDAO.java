/**
 * 
 */
package edu.rupp.rmi.students.server.dao;

import java.sql.SQLException;
import java.util.List;

import edu.rupp.rmi.students.shared.vo.CourseVO;
import edu.rupp.rmi.students.shared.vo.StudentVO;

/**
 * @author sok.pongsametrey
 *
 */
public interface IStudentCoursesDAO {

	/***/
	public static String SQL_STUDENT_SEL = String.valueOf(new StringBuffer ("")
			.append("SELECT st.student_id, st.name, st.birth_date ")
			.append("FROM td_student st ")
			);
	/** List courses related to a student */
	public static String SQL_STU_COURSES_SEL = String.valueOf(new StringBuffer("")
			.append("SELECT sc.student_id, co.course_id, co.subject ")
			.append("FROM td_course co, td_student_courses sc ")
			.append("WHERE co.course_id = sc.course_id ")
			.append("AND sc.student_id = ? ")
			);
	
	/** SQL Student insertion */
	public static final String SQL_STUDENT_INS = String.valueOf(new StringBuffer ("")
			.append("INSERT INTO TD_STUDENT (NAME, BIRTH_DATE) ")
			.append("VALUES (?, ?) ")
			);
	
	/***/
	public static final String SQL_STUDENT_UPD = String.valueOf(new StringBuffer ("")
			.append("UPDATE TD_STUDENT SET NAME = ?, BIRTH_DATE = ? WHERE STUDENT_ID = ? ")
			);
	
	/***/
	public static final String SQL_STUDENT_DEL = String.valueOf(new StringBuffer ("")
			.append("DELETE FROM TD_STUDENT WHERE STUDENT_ID = ? ")
			);
	
	/** SQL Course insertion */
	public static final String SQL_COURSE_INS = String.valueOf(new StringBuffer ("")
			.append("INSERT INTO TD_COURSE (SUBJECT) ")
			.append("VALUES (?) ")
			);
	
	/**  */
	public static final String SQL_COURSE_UPD = String.valueOf(new StringBuffer ("")
			.append("UPDATE TD_COURSE SET SUBJECT = ? WHERE COURSE_ID = ? ")
			);
	
	/**  */
	public static final String SQL_COURSE_DEL = String.valueOf(new StringBuffer ("")
			.append("DELETE FROM TD_COURSE WHERE COURSE_ID = ? ")
			);
	
	/**  */
	public static final String SQL_STU_COURSE_INS = String.valueOf(new StringBuffer ("")
			.append("INSERT INTO TD_STUDENT_COURSES (STUDENT_ID, COURSE_ID) ")
			.append("VALUES (?, ?) ")
			);	
	
	/**  */
	public static final String SQL_STU_COURSE_UPD = String.valueOf(new StringBuffer ("")
			.append("UPDATE TD_STUDENT_COURSES SET COURSE_ID = ? WHERE STUDENT_ID = ? AND COURSE_ID = ? ")
			);
	
	/**  */
	public static final String SQL_STU_COURSE_DEL = String.valueOf(new StringBuffer ("")
			.append("DELETE FROM TD_STUDENT_COURSES WHERE STUDENT_ID = ? AND COURSE_ID = ?")
			);
	
	public static final String SQL_STU_COURSE_ALL_DEL = String.valueOf(new StringBuffer ("")
			.append("DELETE FROM TD_STUDENT_COURSES WHERE STUDENT_ID = ?")
			);
	
	/** Save Student */
	public int saveStudent (StudentVO studentVO) throws SQLException;
	/** Update student */
	public int updateStudent (StudentVO studentVO) throws SQLException;
	/** Delete student */
	public int deleteStudent (Integer studentId) throws SQLException;
	
	public List<StudentVO> selectAllStudents() throws SQLException;
	
	/** Save Course */
	public int saveCourse (CourseVO courseVO) throws SQLException;
	/** Update Course */
	public int updateCourse (CourseVO courseVO) throws SQLException;
	/** Delete Course */
	public int deleteCourse (CourseVO courseVO) throws SQLException;
	
	/** select */
	public StudentVO selectStudentByID (int studentId) throws SQLException;
	/** select */
	public List<StudentVO> selectStudentByCourse(String course) throws SQLException;
	/** select */
	public List<StudentVO> selectStudentByName(String name) throws SQLException;
	/**  */
	public List<CourseVO> listCourses(int studentID) throws SQLException;
	
	/** add link Student and course */
	public int addLinkStudentCourse (Integer studentId, Integer courseId) throws SQLException;	
	/** link Student and course */
	public int updateLinkStudentCourse (Integer studentId, Integer courseId, Integer oldCourseId) throws SQLException;
	/** link Student and course */
	public int deleteLinkStudentCourse (Integer studentId, Integer courseId) throws SQLException;
	/** link Student and course */
	public int deleteLinkStudentCourse (Integer studentId) throws SQLException;
}
