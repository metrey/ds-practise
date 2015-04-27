/**
 * 
 */
package edu.rupp.rmi.students.shared.vo;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sok.pongsametrey
 * @version 1.0
 */
public class StudentVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8669698223269796473L;
	/** Student ID */
	private Integer studentId;
	/** Student Name */
	private String name;
	/** Student Date of Birth */
	private Date dateOfBirth;
	/** All courses */
	private List<CourseVO> lstCourses;
	/**
	 * @return the studentId
	 */
	public Integer getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	/**
	 * @return the lstCourses
	 */
	public List<CourseVO> getLstCourses() {
		return lstCourses;
	}
	/**
	 * @param lstCourses the lstCourses to set
	 */
	public void setLstCourses(List<CourseVO> lstCourses) {
		this.lstCourses = lstCourses;
	}
	/**
	 * 
	 * @param courseVo
	 */
	public void addCourse (CourseVO courseVo) {
		if (this.lstCourses == null) {
			this.lstCourses = new ArrayList<CourseVO>();
		}
		this.lstCourses.add(courseVo);
	}
	/**
	 * 
	 * @param dt
	 * @return
	 */
	public String dateToString (Date dt) {
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return formatter.format(dt);
	}
	
	public Date stringToDate (String dtStr) {
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date dt = null;
		try {
			dt = (Date)formatter.parse(dtStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dt;
	}
	/**
	 * 
	 */
	public void info () {
		System.out.println(this.studentId + "\t" + this.name + "\t" + this.dateToString(this.dateOfBirth));
	}
	
}
