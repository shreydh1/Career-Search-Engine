package pojoClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dbConnection.DbConnection;
import guiCareerManage.CareerServices;

public class company {
	private int companyId; 			//CompanyID 		INTEGER PRIMARY KEY,
	private String companyName; 	//CompanyName 		VARCHAR(30),
	private String companyAddress;	//CompanyAddress 	INTEGER,
	private String companyStreet;	//CompanyStreet 		VARCHAR(50),
	private String companyCity;		//CompanyCity 		VARCHAR(50),
	private int companyZipCode;		//CompanyZipcode 	NUMERIC(5,0),
	private String companySector;		//CompanySector 	VARCHAR(50),
	private String companyWebsite; 	//CompanyWebsite 	VARCHAR(50) );
	
	DbConnection db = new DbConnection();
	Connection connection = null;
	PreparedStatement pstmt = null;
	CareerServices cs = new CareerServices(); 
	
	public company() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "company [companyId=" + companyId + ", companyName=" + companyName + ", companyAddress=" + companyAddress
				+ ", companyStreet=" + companyStreet + ", companyCity=" + companyCity + ", companyZipCode="
				+ companyZipCode + ", companySector=" + companySector + ", companyWebsite=" + companyWebsite + "]";
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getCompanyStreet() {
		return companyStreet;
	}
	public void setCompanyStreet(String companyStreet) {
		this.companyStreet = companyStreet;
	}
	public String getCompanyCity() {
		return companyCity;
	}
	public void setCompanyCity(String companyCity) {
		this.companyCity = companyCity;
	}
	public int getCompanyZipCode() {
		return companyZipCode;
	}
	public void setCompanyZipCode(int companyZipCode) {
		this.companyZipCode = companyZipCode;
	}
	public String getCompanySector() {
		return companySector;
	}
	public void setCompanySector(String companySector) {
		this.companySector = companySector;
	}
	public String getCompanyWebsite() {
		return companyWebsite;
	}
	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}
	
	public boolean addCompanyDetails(int companyId, String companyName, String companyAddress,
			String companyStreet, String companyCity, int companyZipCode, String companySector, String companyWebsite) {
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				
				pstmt = connection
						.prepareStatement("INSERT into COMPANY values(?,?,?,?,?,?,?,?)");
				pstmt.setInt(1, companyId);
				pstmt.setString(2, companyName);
				pstmt.setString(3, companyAddress);
				pstmt.setString(4, companyStreet);
				pstmt.setString(5, companyCity);
				pstmt.setInt(6, companyZipCode);
				pstmt.setString(7, companySector);
				pstmt.setString(8, companyWebsite);
				result = pstmt.execute();
				System.out.println("Record is inserted into COMPANY table!");
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
	
	public boolean addCompanyLaunchesProject(int companyId, int projectId) {
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				
				pstmt = connection
						.prepareStatement("INSERT into COMPANYLAUNCHESPROJECT values(?,?)");
				pstmt.setInt(1, companyId);
				pstmt.setInt(2, projectId);
				result = pstmt.execute();
				System.out.println("Record is inserted into COMPANYLAUNCHESPROJECT table!");
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
		 company c1 = new company();
		 boolean result = c1.addCompanyDetails(397,"dubakoor","00123","dubakoor","dummy",70122,"cheating","www.dubakoor.com");
		//c1.deleteCertificateDetails(850);
		 System.out.println(result);
	}
	
	
}
