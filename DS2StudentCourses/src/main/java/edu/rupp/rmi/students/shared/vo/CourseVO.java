/**
 * 
 */
package edu.rupp.rmi.students.shared.vo;

import java.io.Serializable;

/**
 * @author sok.pongsametrey
 *
 */
public class CourseVO implements Serializable {

	private Integer courseId;
	private String courseName;
	/**
	 * 
	 */
	private static final long serialVersionUID = -918715295372929935L;
	/**
	 * @return the courseId
	 */
	public Integer getCourseId() {
		return courseId;
	}
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}
	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public void info () {
		System.out.println(this.courseId + "\t" + this.courseName);
	}
}
