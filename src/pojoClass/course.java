package pojoClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dbConnection.DbConnection;
import guiCareerManage.CareerServices;

public class course {
	private int courseId; //CourseID 	INTEGER PRIMARY KEY,
	private String courseTitle; //CourseTitle 	VARCHAR(40),
	private String courseLevel; //CourseLevel 	VARCHAR(20),
	private String courseDescription; //CourseDescription VARCHAR(100),
	private String courseStatus; //CourseStatus 	VARCHAR(10),
	private double coursePrice; //CoursePrice 	NUMERIC(8,2));
	DbConnection db = new DbConnection();
	Connection connection = null;
	PreparedStatement pstmt = null;
	CareerServices cs = new CareerServices();
	
	public course() {
		super();
	}
	@Override
	public String toString() {
		return "course [courseId=" + courseId + ", courseTitle=" + courseTitle + ", courseLevel=" + courseLevel
				+ ", courseDescription=" + courseDescription + ", courseStatus=" + courseStatus + ", coursePrice="
				+ coursePrice + "]";
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	public String getCourseLevel() {
		return courseLevel;
	}
	public void setCourseLevel(String courseLevel) {
		this.courseLevel = courseLevel;
	}
	public String getCourseDescription() {
		return courseDescription;
	}
	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}
	public String getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}
	public double getCoursePrice() {
		return coursePrice;
	}
	public void setCoursePrice(float coursePrice) {
		this.coursePrice = coursePrice;
	}
	/**
	 * This method is used to add new row to course table.
	 * 
	 * @param courseId
	 * @param courseTitle
	 * @param courseLevel
	 * @param courseDescription
	 * @param courseStatus
	 * @param coursePrice
	 */
	public boolean AddCourseDetails(int courseId, String courseTitle, String courseLevel,
			String courseDescription, String courseStatus, double coursePrice) {
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				
				pstmt = connection
						.prepareStatement("INSERT into COURSE values(?,?,?,?,?,?)");
				pstmt.setInt(1, courseId);
				pstmt.setString(2, courseTitle);
				pstmt.setString(3, courseLevel);
				pstmt.setString(4, courseDescription);
				pstmt.setString(5, courseStatus);
				pstmt.setDouble(6, coursePrice);
				result = pstmt.execute();
				System.out.println("Record is inserted into course table!");
			}
		} catch (SQLException e) {
			e.getMessage();
		} finally {
			System.out.println("Entered finally block..");
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.getMessage();
			}
		}
		return result;
	}
	
	public boolean AddSkillToCourse(int courseId, int ksCode) {
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				/*CourseCoversSkill(
						CourseID INTEGER,
						KSCode INTEGER)*/
				pstmt = connection
						.prepareStatement("INSERT into COURSECOVERSSKILL values(?,?)");
				pstmt.setInt(1, courseId);
				pstmt.setInt(2, ksCode);
				result = pstmt.execute();
				System.out
						.println("Record is inserted into COURSECOVERSSKILL table!");
			}
		} catch (SQLException e) {
			e.getMessage();
		} finally {
			System.out.println("Entered finally block..");
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.getMessage();
			}
		}
		return result;
	}
	
	/**
	 * This method is used to delete a row from the course table.
	 * 
	 * @param courseId
	 */
	
	public boolean deleteCourseDetails(int courseId) {
		boolean result = true;
		String query = "DELETE COURSE WHERE COURSEID = " + courseId ;
		String query2 = "DELETE COURSECOVERSSKILL WHERE COURSEID = " + courseId ;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully \n");
				System.out.println(query + "\n");
				pstmt = connection
						.prepareStatement(query2);
				result = pstmt.execute();
				System.out.println(result + "\n");
				System.out
						.println(courseId + " Record is deleted from COURSECOVERSSKILL table!");
				pstmt = connection
						.prepareStatement(query);
				result = pstmt.execute();
				System.out.println(result + "\n");
				System.out
						.println(courseId + " Record is deleted from COURSE table!");
			}
		} catch (SQLException e) {
			e.getMessage();
		} finally {
			System.out.println("Entered finally block..");
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.getMessage();
			}
		}
		return result;
	}
	
	 public static void main(String[] args){
		 course c1 = new course();
		 c1.deleteCourseDetails(877);
		 
		 }
}
