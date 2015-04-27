/**
 * 
 */
package edu.rupp.rmi.students.server.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import edu.rupp.rmi.common.dao.AbstractDAO;
import edu.rupp.rmi.students.shared.vo.CourseVO;
import edu.rupp.rmi.students.shared.vo.StudentVO;

/**
 * @author sok.pongsametrey
 * @version 1.0
 */
public class StudentCoursesDAO extends AbstractDAO implements IStudentCoursesDAO {

	@Override
	public void createDBModel() throws SQLException {
		try {
			this.getConnection();
		    
		    this.getStatement().executeUpdate("create table if not exists td_student (student_id integer PRIMARY KEY AUTOINCREMENT, name string, birth_date date)");
		    this.getStatement().executeUpdate("create table if not exists td_course (course_id integer PRIMARY KEY AUTOINCREMENT, subject string)");
		    this.getStatement().executeUpdate("create table if not exists td_student_courses (student_id integer, course_id integer, " +
		    		"PRIMARY KEY (student_id, course_id), " +
		    		"FOREIGN KEY(student_id) REFERENCES td_student(student_id), " +
		    		"FOREIGN KEY(course_id) REFERENCES td_course(course_id) )");
		} catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
			throw e;
		} finally {
			this.close();
		}
		System.out.println("DB Model created for database: " + this.dbName + "!");
	}

	/**
	 * 
	 */
	public int saveStudent(StudentVO studentVO) throws SQLException {

		PreparedStatement ps = null;
		int nbReturn = -1;
		Integer id;
		try {
			System.out.println("> SQL saveStudent: " + SQL_STUDENT_INS);
			ps = this.getConnection().prepareStatement(SQL_STUDENT_INS);
			ps.setString(1, studentVO.getName());
			if (studentVO.getDateOfBirth() != null) {
				ps.setDate(2, new java.sql.Date(studentVO.getDateOfBirth().getTime()));
			} else {
				ps.setNull(2, Types.DATE);
			}
			
			nbReturn = ps.executeUpdate();	
			ResultSet rs = ps.getGeneratedKeys();
			if (rs != null && rs.next()) {
				id = rs.getInt(1);
				studentVO.setStudentId(id);
				System.out.println("> Student ID: " + id);
			}
			
			// insert list of course
			// insert link between student and course
		} catch (SQLException e) {
			throw e;
		} finally {
			if (ps != null) {
				ps.close();
			}
			this.close();
			System.out.println(nbReturn + " student(s) inserted. ");
		}
		
		
		return nbReturn;
	}

	public int updateStudent(StudentVO studentVO) throws SQLException {
		PreparedStatement ps = null;
		int nbReturn = -1;
		try {
			System.out.println("> SQL updateStudent: " + SQL_STUDENT_UPD);
			ps = this.getConnection().prepareStatement(SQL_STUDENT_UPD);
			
			ps.setString(1, studentVO.getName());
			if (studentVO.getDateOfBirth() != null) {
				ps.setDate(2, new java.sql.Date(studentVO.getDateOfBirth().getTime()));
			} else {
				ps.setNull(2, Types.DATE);
			}
			ps.setInt(3, studentVO.getStudentId());
			
			nbReturn = ps.executeUpdate();
			
			// insert list of course
			// insert link between student and course
		} catch (SQLException e) {
			throw e;
		} finally {
			System.out.println(nbReturn + " student(s) updated. ");
		}
		return nbReturn;
	}

	public int deleteStudent(Integer studentId) throws SQLException {
		PreparedStatement ps = null;
		int nbReturn = -1;
		try {
			System.out.println("> SQL deleteStudent: " + SQL_STUDENT_DEL);
			ps = this.getConnection().prepareStatement(SQL_STUDENT_DEL);
			
			ps.setInt(1, studentId);			
			nbReturn = ps.executeUpdate();
			
			// delete link between student and course			
		} catch (SQLException e) {
			throw e;
		} finally {
			System.out.println(nbReturn + " student(s) updated. ");
		}
		return nbReturn;
	}

	public int saveCourse(CourseVO courseVO) throws SQLException {
		PreparedStatement ps = null;

		ResultSet rs = null;
		String request = "SELECT * FROM td_course WHERE subject = '" + courseVO.getCourseName() + "'";
		int nbReturn = -1;
		Integer id;
		try {
			rs = this.getStatement().executeQuery(request);
			if (rs.next()) {
				courseVO.setCourseId(rs.getInt(1));
				System.out.println("Subject: " + courseVO.getCourseName() + " exists in db.");
				return -1;
			}
			
			System.out.println("> SQL saveCourse: " + SQL_COURSE_INS);
			ps = this.getConnection().prepareStatement(SQL_COURSE_INS);
			ps.setString(1, courseVO.getCourseName());			
			
			nbReturn = ps.executeUpdate();			
			
			ResultSet rs1 = ps.getGeneratedKeys();
			if (rs1 != null && rs1.next()) {
				id = rs1.getInt(1);
				courseVO.setCourseId(id);
				System.out.println("> Course ID: " + id);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			System.out.println(nbReturn + " course inserted. ");
		}	
		
		return nbReturn;
	}

	/**
	 * 
	 */
	public int updateCourse(CourseVO courseVO) throws SQLException {
		PreparedStatement ps = null;
		int nbReturn = -1;
		try {
			System.out.println("> SQL updateCourse: " + SQL_COURSE_UPD);
			ps = this.getConnection().prepareStatement(SQL_COURSE_UPD);
			
			ps.setString(1, courseVO.getCourseName());
			ps.setInt(2, courseVO.getCourseId());			
			
			nbReturn = ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			System.out.println(nbReturn + " course inserted. ");
		}		
		
		return nbReturn;
	}

	public int deleteCourse(CourseVO courseVO) throws SQLException {
		PreparedStatement ps = null;
		int nbReturn = -1;
		try {
			System.out.println("> SQL deleteCourse: " + SQL_COURSE_DEL);
			ps = this.getConnection().prepareStatement(SQL_COURSE_DEL);
			
			ps.setInt(1, courseVO.getCourseId());			
			nbReturn = ps.executeUpdate();
			
			// delete link between student and course			
		} catch (SQLException e) {
			throw e;
		} finally {
			System.out.println(nbReturn + " student(s) updated. ");
		}
		return nbReturn;
	}

	public StudentVO selectStudentByID(int studentId) throws SQLException {

		ResultSet rs = null;
		
		String request = SQL_STUDENT_SEL + " WHERE student_id = " + studentId;
		StudentVO studentVO = null;		
		try {
			System.out.println("> SQL selectStudentByID: " + request);
			rs = this.getStatement().executeQuery(request);
			if (rs.next()) {
				studentVO = this.prepareStudentVO(rs);
				// select list of courses
				studentVO.setLstCourses(this.listCourses(studentVO.getStudentId()));
			}
						
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) rs.close();
			this.close();
			System.out.println((studentVO == null ? " Could not found student"
					: "Found student: " + studentVO.getName())
					+ " with ID: "
					+ studentId);
		}
		return studentVO;
	}
	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private StudentVO prepareStudentVO (ResultSet rs) throws SQLException {
		StudentVO studentVO = new StudentVO();
		studentVO.setStudentId(new Integer(rs.getInt("student_id")));
		studentVO.setName(rs.getString("name"));
		studentVO.setDateOfBirth(rs.getDate("birth_date"));
		return studentVO;
	}
	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private CourseVO preparedCourseVO (ResultSet rs) throws SQLException {
		CourseVO courseVO = new CourseVO();
		courseVO.setCourseId(rs.getInt("course_id"));
		courseVO.setCourseName(rs.getString("subject"));
		
		return courseVO;
	}

	public List<StudentVO> selectStudentByCourse(String course) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String request = SQL_STUDENT_SEL + " WHERE student_id in (" +
				"SELECT sc.student_id " +
				"FROM td_course co, td_student_courses sc " +
				"WHERE co.course_id = sc.course_id " +
				"AND co.subject = ?) ";	
		List<StudentVO> lstStudents = new ArrayList<StudentVO>();
		try {
			System.out.println("> SQL selectStudentByCourse: " + request);
			ps = this.getConnection().prepareStatement(request);
			ps.setString(1, course);
			rs = ps.executeQuery();
			while (rs.next()) {
				StudentVO studentVO = this.prepareStudentVO(rs);
				// select list of courses
				studentVO.setLstCourses(this.listCourses(studentVO.getStudentId()));
				lstStudents.add(studentVO);
			}
						
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) rs.close();
			this.close();
			System.out.println("# Found student(s): " + lstStudents.size());
		}
		return lstStudents;
	}

	public List<StudentVO> selectStudentByName(String name) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String request = SQL_STUDENT_SEL + " WHERE name = ? ";
		List<StudentVO> lstStudents = new ArrayList<StudentVO>();
		try {
			System.out.println("> SQL selectStudentByName: " + request);
			ps = this.getConnection().prepareStatement(request);
			ps.setString(1, name);
			rs = ps.executeQuery();
			while (rs.next()) {
				StudentVO studentVO = this.prepareStudentVO(rs);
				// select list of courses
				studentVO.setLstCourses(this.listCourses(studentVO.getStudentId()));
				lstStudents.add(studentVO);
			}
						
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) rs.close();
			this.close();
			System.out.println("# Found student(s): " + lstStudents.size());
		}
		return lstStudents;
	}
	
	public List<StudentVO> selectAllStudents() throws SQLException {
		ResultSet rs = null;
		
		List<StudentVO> lstStudents = new ArrayList<StudentVO>();
		try {
			System.out.println("> SQL selectAllStudents: " + SQL_STUDENT_SEL);
			rs = this.getStatement().executeQuery(SQL_STUDENT_SEL);
			while (rs.next()) {
				StudentVO studentVO = this.prepareStudentVO(rs);
				// select list of courses
				studentVO.setLstCourses(this.listCourses(studentVO.getStudentId()));
				lstStudents.add(studentVO);
			}
						
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) rs.close();
			this.close();
			System.out.println("# Found student(s): " + lstStudents.size());
		}
		return lstStudents;
	}

	/**
	 * 
	 */
	public List<CourseVO> listCourses(int studentId) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CourseVO> lstCourseByStudent = new ArrayList<CourseVO>();	
		try {
			System.out.println("> SQL listCourses: " + SQL_STU_COURSES_SEL);
			ps = this.getConnection().prepareStatement(SQL_STU_COURSES_SEL);
			ps.setInt(1, studentId);
			rs = ps.executeQuery();
			while (rs.next()) {
				CourseVO courseVO = this.preparedCourseVO(rs);				
				lstCourseByStudent.add(courseVO);
			}
						
		} catch (SQLException e) {
			throw e;
		} finally {
			// do not close connection here
			// since we have to call it in another DAO method
			
			System.out.println("# Found " + lstCourseByStudent.size() + " courses link" 
					+ " with student ID: "
					+ studentId);
		}
		return lstCourseByStudent;
	}

	public int addLinkStudentCourse(Integer studentId, Integer courseId)
			throws SQLException {
		PreparedStatement ps = null;
		int nbReturn = -1;
		
		ResultSet rs = null;
		String request = "select * from td_student_courses where student_id = " + studentId + " and course_id = " + courseId;
		try {
			rs = this.getStatement().executeQuery(request);
			if (rs.next()) {
				System.out.println("Student id: " + studentId + " already linked with course: " + courseId);
				return nbReturn;
			}
			System.out.println("> SQL addLinkStudentCourse: " + SQL_STU_COURSE_INS);
			ps = this.getConnection().prepareStatement(SQL_STU_COURSE_INS);
			ps.setInt(1, studentId);
			ps.setInt(2,courseId);			
			
			nbReturn = ps.executeUpdate();			
		} catch (SQLException e) {
			throw e;
		} finally {
			System.out.println("# Student ID: " + studentId + " linked with course id: " + courseId);
		}	
		
		return nbReturn;
	}

	/**
	 * 
	 */
	public int updateLinkStudentCourse(Integer studentId, Integer courseId, Integer oldCourseId)
			throws SQLException {
		PreparedStatement ps = null;
		int nbReturn = -1;
		try {
			System.out.println("> SQL updateLinkStudentCourse: " + SQL_STU_COURSE_UPD);
			ps = this.getConnection().prepareStatement(SQL_STU_COURSE_UPD);
			
			ps.setInt(1, courseId);
			ps.setInt(2, studentId);
			ps.setInt(3, oldCourseId);
			nbReturn = ps.executeUpdate();
			
			// delete link between student and course			
		} catch (SQLException e) {
			throw e;
		} finally {
			System.out.println("# Update link between student id [" + studentId + "] to course id [" + courseId + "]. ");
		}
		return nbReturn;
	}

	/**
	 * 
	 */
	public int deleteLinkStudentCourse(Integer studentId, Integer courseId)
			throws SQLException {
		PreparedStatement ps = null;
		int nbReturn = -1;
		try {
			System.out.println("> SQL deleteLinkStudent: " + SQL_STU_COURSE_DEL);
			ps = this.getConnection().prepareStatement(SQL_STU_COURSE_DEL);
			
			ps.setInt(1, studentId);
			ps.setInt(2, courseId);
			nbReturn = ps.executeUpdate();
			
			// delete link between student and course			
		} catch (SQLException e) {
			throw e;
		} finally {
			System.out.println("# Delete link between student id [" + studentId + "] and course id [" + courseId + "]. ");
		}
		return nbReturn;
	}

	public int deleteLinkStudentCourse(Integer studentId) throws SQLException {
		PreparedStatement ps = null;
		int nbReturn = -1;
		try {
			System.out.println("> SQL deleteLinkStudentCourse: " + SQL_STU_COURSE_ALL_DEL);
			ps = this.getConnection().prepareStatement(SQL_STU_COURSE_ALL_DEL);
			
			ps.setInt(1, studentId);
			nbReturn = ps.executeUpdate();
			
			// delete link between student and course			
		} catch (SQLException e) {
			throw e;
		} finally {
			System.out.println("# Deleted links between student id [" + studentId + "] and all courses. ");
		}
		return nbReturn;
	}

}
