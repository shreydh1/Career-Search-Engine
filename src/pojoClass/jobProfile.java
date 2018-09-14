package pojoClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dbConnection.DbConnection;
import guiCareerManage.CareerServices;

public class jobProfile {
	private int jProfileCode;		//JProfileCode 		INTEGER PRIMARY KEY,
	private String jProfileTitle;	//JProfileTitle 		VARCHAR(30),
	private String jProfileDescription;	//JProfileDescription 	VARCHAR(75));
	
	DbConnection db = new DbConnection();
	Connection connection = null;
	PreparedStatement pstmt = null;
	CareerServices cs = new CareerServices();
	
	@Override
	public String toString() {
		return "jobProfile [jProfileCode=" + jProfileCode + ", jProfileTitle=" + jProfileTitle
				+ ", jProfileDescription=" + jProfileDescription + "]";
	}
	public jobProfile() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getjProfileCode() {
		return jProfileCode;
	}
	public void setjProfileCode(int jProfileCode) {
		this.jProfileCode = jProfileCode;
	}
	public String getjProfileTitle() {
		return jProfileTitle;
	}
	public void setjProfileTitle(String jProfileTitle) {
		this.jProfileTitle = jProfileTitle;
	}
	public String getjProfileDescription() {
		return jProfileDescription;
	}
	public void setjProfileDescription(String jProfileDescription) {
		this.jProfileDescription = jProfileDescription;
	}
	public boolean addJobProfileDetails(int jProfileCode, String jProfileTitle, String jProfileDescription) {
		boolean result = true;

		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				
				pstmt = connection
						.prepareStatement("INSERT into JOBPROFILE values(?,?,?)");
				pstmt.setInt(1, jProfileCode);
				pstmt.setString(2, jProfileTitle);
				pstmt.setString(3,jProfileDescription);
				result = pstmt.execute();
				System.out.println("Record is inserted into JOBPROFILE table!");
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
	public boolean jProfileRequiresCertificate(int jProfileCode, int certCode) {
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				
				pstmt = connection
						.prepareStatement("INSERT into JPROFILEREQUIRESCERTIFICATE values(?,?)");
				pstmt.setInt(1, jProfileCode);
				pstmt.setInt(2, certCode);
				result = pstmt.execute();
				System.out.println("Record is inserted into JPROFILEREQUIRESCERTIFICATE table!");
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
	public boolean jProfileRequiresSkill(int jprofileCode, int ksCode) {
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				
				pstmt = connection
						.prepareStatement("INSERT into JPROFILEREQUIRESSKILL values(?,?)");
				pstmt.setInt(1, jprofileCode);
				pstmt.setInt(2, ksCode);
				result = pstmt.execute();
				System.out.println("Record is inserted into JPROFILEREQUIRESSKILL table!");
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
		 jobProfile jpo = new jobProfile();
		 boolean result = jpo.addJobProfileDetails(241, "sometitle", "somedescription");
		 System.out.println(result);
	}
}
