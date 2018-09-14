package pojoClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dbConnection.DbConnection;
import guiCareerManage.CareerServices;

public class project {
	private int projectId; // ProjectID 	 INTEGER PRIMARY KEY,
	private String projectTitle; //ProjectTitle 	 VARCHAR(50),
	private String projectDirector; //ProjectDirector  VARCHAR(30),
	private String budgetCode; //BudgetCode 	 VARCHAR(10),
	private String projectStartDate;
	private String projectEndDate;
	
	DbConnection db = new DbConnection();
	Connection connection = null;
	PreparedStatement pstmt = null;
	CareerServices cs = new CareerServices();
	
	
	
	public project() {
		super();
	}
	@Override
	
	public String toString() {
		return "project [projectId=" + projectId + ", projectTitle=" + projectTitle + ", projectDirector="
				+ projectDirector + ", budgetCode=" + budgetCode + "]";
	}
	
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getProjectTitle() {
		return projectTitle;
	}
	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}
	public String getProjectDirector() {
		return projectDirector;
	}
	public void setProjectDirector(String projectDirector) {
		this.projectDirector = projectDirector;
	}
	public String getBudgetCode() {
		return budgetCode;
	}
	public void setBudgetCode(String budgetCode) {
		this.budgetCode = budgetCode;
	}
	public String getProjectStartDate() {
		return projectStartDate;
	}
	public void setProjectStartDate(String projectStartDate) {
		this.projectStartDate = projectStartDate;
	}
	public String getProjectEndDate() {
		return projectEndDate;
	}
	public void setProjectEndDate(String projectEndDate) {
		this.projectEndDate = projectEndDate;
	}
	
	public boolean AddProjectDetails(int projectId, String projectTitle, String projectDirector,
			String budgetCode, String projectStartDate, String projectEndDate) {
		/*ProjectID 	 INTEGER PRIMARY KEY,
		ProjectTitle 	 VARCHAR(50),
		ProjectDirector  VARCHAR(30),
		BudgetCode 	 VARCHAR(10),*/
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				
				pstmt = connection
						.prepareStatement("INSERT into PROJECT values(?,?,?,?,?,?)");
				pstmt.setInt(1, projectId);
				pstmt.setString(2, projectTitle);
				pstmt.setString(3, projectDirector);
				pstmt.setString(4, budgetCode);
				pstmt.setString(5, projectStartDate);
				pstmt.setString(6, projectEndDate);
				result = pstmt.execute();
				System.out.println("Record is inserted into PROJECT table!");
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
	public boolean deleteProjectDetails(int projectId) {
		boolean result = true;
		String query = "DELETE PROJECT WHERE PROJECTID = " + projectId;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully \n");
				System.out.println(query + "\n");
				pstmt = connection
						.prepareStatement(query);
				result = pstmt.execute();
				System.out.println(result + "\n");
				System.out
						.println(projectId + " Record is deleted from PROJECT table!");
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
	public String convertToDateFormat(){
		String convertedDate = null;
		/*
		 * Code to change inserted input to DD-MM-YY format
		 * otherwise the SQL Insert statement willl through error
		 */
		return convertedDate;
	}
	public static void main(String[] args){
		 project p1 = new project();
		 //boolean result = p1.AddProjectDetails(470,"Mail Directory Upgrade","Angela Richards","400045","19-07-13","02-10-15");
		 p1.deleteProjectDetails(470);
		// System.out.println(result);
	}
}
