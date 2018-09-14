package pojoClass;

import java.sql.Connection;
import java.sql.*;

import dbConnection.DbConnection;
import guiCareerManage.CareerServices;

public class section {
	private int sectionId; //SectionID 	INTEGER,
	private int courseId; //CourseID 	INTEGER,
	private String year; //Year	 	NUMERIC(4,0),
	private String completeDate; //CompleteDate 	DATE,
	private String offeredBy; //OfferedBy 	VARCHAR(30),
	private String format; //Format 		VARCHAR(20),
	private double sectionPrice; //SectionPrice 	NUMERIC(8,2),
	
	DbConnection db = new DbConnection();
	Connection connection = null;
	PreparedStatement pstmt = null;
	CareerServices cs = new CareerServices();
	
	public section() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	
	public String toString() {
		return "section [sectionID=" + sectionId + ", courseID=" + courseId + ", year=" + year + ", completeDate="
				+ completeDate + ", offeredBy=" + offeredBy + ", format=" + format + ", sectionPrice=" + sectionPrice
				+ "]";
	}
	public int getSectionID() {
		return sectionId;
	}
	public void setSectionID(int sectionID) {
		this.sectionId = sectionID;
	}
	public int getCourseID() {
		return courseId;
	}
	public void setCourseID(int courseID) {
		this.courseId = courseID;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}
	public String getOfferedBy() {
		return offeredBy;
	}
	public void setOfferedBy(String offeredBy) {
		this.offeredBy = offeredBy;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public double getSectionPrice() {
		return sectionPrice;
	}
	public void setSectionPrice(double sectionPrice) {
		this.sectionPrice = sectionPrice;
	}
	
	public String convertToDateFormat(){
		String convertedDate = null;
		/*
		 * Code to change inserted input to DD-MM-YY format
		 * otherwise the SQL Insert statement will throw error
		 */
		return convertedDate;
	}
	
	public boolean addSectionDetails(int sectionId,int courseId, String year, String completeDate,
			String offeredBy, String format, double sectionPrice) {
		/*SectionID 	INTEGER,
		CourseID 	INTEGER,
		Year	 	NUMERIC(4,0),
		CompleteDate 	DATE,
		OfferedBy 	VARCHAR(30),
		Format 		VARCHAR(20),
		SectionPrice 	NUMERIC(8,2),*/
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				
				pstmt = connection
						.prepareStatement("INSERT into SECTION values(?,?,?,?,?,?,?)");
				pstmt.setInt(1, sectionId);
				pstmt.setInt(2, courseId);
				pstmt.setString(3, year);
				pstmt.setString(4, completeDate);
				pstmt.setString(5, offeredBy);
				pstmt.setString(6, format);
				pstmt.setDouble(7, sectionPrice);
				result = pstmt.execute();
				System.out.println("Record is inserted into SECTION table!");
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
		 section ks1 = new section();
		 boolean result = ks1.addSectionDetails(102, 600, "2015", "27-11-2016", "mycompany", "smode", 999.99);
		 //ks1.deleteSectionDetails(102); no need for section delete because section details will be always stored
		 System.out.println(result);
	}
}
