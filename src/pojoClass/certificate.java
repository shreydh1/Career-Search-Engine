package pojoClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dbConnection.DbConnection;
import guiCareerManage.CareerServices;

public class certificate {
	private int certCode; //CertCode 	INTEGER PRIMARY KEY,
	private String certTitle; //CertTitle 	VARCHAR(50),
	private String certDescription; //CertDescription VARCHAR(100),
	private String expDate; //ExpDate 	DATE,
	private String issuedBy; //IssuedBy 	VARCHAR(50),
	private int certToolCode; //CertToolCode 	INTEGER;
	DbConnection db = new DbConnection();
	Connection connection = null;
	PreparedStatement pstmt = null;
	CareerServices cs = new CareerServices();
	
	public certificate() {
		super();
	}
	@Override
	public String toString() {
		return "certificate [certCode=" + certCode + ", certTitle=" + certTitle + ", certDescription=" + certDescription
				+ ", expDate=" + expDate + ", issuedBy=" + issuedBy + ", certToolCode=" + certToolCode + "]";
	}
	public int getCertCode() {
		return certCode;
	}
	public void setCertCode(int certCode) {
		this.certCode = certCode;
	}
	public String getCertTitle() {
		return certTitle;
	}
	public void setCertTitle(String certTitle) {
		this.certTitle = certTitle;
	}
	public String getCertDescription() {
		return certDescription;
	}
	public void setCertDescription(String certDescription) {
		this.certDescription = certDescription;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public String getIssuedBy() {
		return issuedBy;
	}
	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}
	public int getCertToolCode() {
		return certToolCode;
	}
	public void setCertToolCode(int certToolCode) {
		this.certToolCode = certToolCode;
	}
	
	public boolean AddCertificateDetails(int certCode, String certTitle, String certDescription, String expDate, String issuedBy,
			int certToolCode) {
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				
				pstmt = connection
						.prepareStatement("INSERT into CERTIFICATE values(?,?,?,?,?,?)");
				pstmt.setInt(1, certCode);
				pstmt.setString(2, certTitle);
				pstmt.setString(3, certDescription);
				pstmt.setString(4, expDate);
				pstmt.setString(5, issuedBy);
				pstmt.setInt(6, certToolCode);
				result = pstmt.execute();
				System.out.println("Record is inserted into CERTIFICATE table!");
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
	public boolean deleteCertificateDetails(int certCode) {
		boolean result = true;
		String query = "DELETE CERTIFICATE WHERE CERTCODE = " + certCode;
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
						.println(certCode + " Record is deleted from CERTIFICATE table!");
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
		 certificate c1 = new certificate();
		 //boolean result = c1.AddCertificateDetails(850,"tester", "Java testing", "01-Dec-2018", "UNO", 64077);
		c1.deleteCertificateDetails(850);
		 //System.out.println(result);
	}
}
