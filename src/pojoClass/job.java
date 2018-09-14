package pojoClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dbConnection.DbConnection;
import guiCareerManage.CareerServices;

public class job {
	private int jobCode; 		//JobCode 	INTEGER PRIMARY KEY,
	private String jobType;		//JobType 	VARCHAR(20),
	private int payRate;		//PayRate 	NUMERIC(8,2),
	private String payType;		//PayType 	VARCHAR(10),
	private int companyId;		//CompanyID 	INTEGER,
	private int jProfileCode;	//JProfileCode 	INTEGER,
	
	DbConnection db = new DbConnection();
	Connection connection = null;
	PreparedStatement pstmt = null;
	CareerServices cs = new CareerServices();
	
	public int getJobCode() {
		return jobCode;
	}
	public void setJobCode(int jobCode) {
		this.jobCode = jobCode;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public int getPayRate() {
		return payRate;
	}
	public void setPayRate(int payRate) {
		this.payRate = payRate;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public int getjProfileCode() {
		return jProfileCode;
	}
	public void setjProfileCode(int jProfileCode) {
		this.jProfileCode = jProfileCode;
	}
	public job() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "job [jobCode=" + jobCode + ", jobType=" + jobType + ", payRate=" + payRate + ", payType=" + payType
				+ ", companyId=" + companyId + ", jProfileCode=" + jProfileCode + "]";
	}
	
	public boolean addJobDetails(int jobCode,String jobType, int payRate, String payType, int companyId, int jProfileCode) {
		boolean result = true;

		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				
				pstmt = connection
						.prepareStatement("INSERT into JOB values(?,?,?,?,?,?)");
				pstmt.setInt(1, jobCode);
				pstmt.setString(2, jobType);
				pstmt.setInt(3, payRate);
				pstmt.setString(4, payType);
				pstmt.setInt(5, companyId);
				pstmt.setInt(6, jProfileCode);
				result = pstmt.execute();
				System.out.println("Record is inserted into JOB table!");
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
	
	public boolean addJobInProject(int jobCode, int projectId) {
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				
				pstmt = connection
						.prepareStatement("INSERT into JOBINPROJECT values(?,?)");
				pstmt.setInt(1, jobCode);
				pstmt.setInt(2, projectId);
				result = pstmt.execute();
				System.out.println("Record is inserted into JOBINPROJECT table!");
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
	
}
