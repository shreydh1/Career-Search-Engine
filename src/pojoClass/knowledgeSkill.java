package pojoClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dbConnection.DbConnection;
import guiCareerManage.CareerServices;

public class knowledgeSkill {
	private int ksCode;//KSCode 	INTEGER PRIMARY KEY,
	private String ksTitle; //KSTitle 	VARCHAR(50),
	private String ksDescription; //KSDescription 	VARCHAR(100),
	private String ksLevel; //KSLevel 	VARCHAR(20));
	
	DbConnection db = new DbConnection();
	Connection connection = null;
	PreparedStatement pstmt = null;
	CareerServices cs = new CareerServices();
	
	public knowledgeSkill() {
		super();
	}
	@Override
	public String toString() {
		return "knowledgeSkill [ksCode=" + ksCode + ", ksTitle=" + ksTitle + ", ksDescription=" + ksDescription
				+ ", ksLevel=" + ksLevel + "]";
	}
	public int getKsCode() {
		return ksCode;
	}
	public void setKsCode(int ksCode) {
		this.ksCode = ksCode;
	}
	public String getKsTitle() {
		return ksTitle;
	}
	public void setKsTitle(String ksTitle) {
		this.ksTitle = ksTitle;
	}
	public String getKsDescription() {
		return ksDescription;
	}
	public void setKsDescription(String ksDescription) {
		this.ksDescription = ksDescription;
	}
	public String getKsLevel() {
		return ksLevel;
	}
	public void setKsLevel(String ksLevel) {
		this.ksLevel = ksLevel;
	}
	public boolean addKnowledgeSkillDetails(int ksCode, String ksTitle, String ksDescription,
			String ksLevel) {
		/*KSCode 	INTEGER PRIMARY KEY,
			KSTitle 	VARCHAR(50),
			KSDescription 	VARCHAR(100),
			KSLevel 	VARCHAR(20));
		 	*/
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				
				pstmt = connection
						.prepareStatement("INSERT into KNOWLEDGESKILL values(?,?,?,?)");
				pstmt.setInt(1, ksCode);
				pstmt.setString(2, ksTitle);
				pstmt.setString(3, ksDescription);
				pstmt.setString(4, ksLevel);
				result = pstmt.execute();
				System.out.println("Record is inserted into KNOWLEDGESKILL table!");
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
	public boolean deleteKnowledgeSkillDetails(int ksCode) {
		boolean result = true;
		String query = "DELETE KNOWLEDGESKILL WHERE KSCODE = " + ksCode;
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
						.println(ksCode + " Record is deleted from KNOWLEDGESKILL table!");
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
		 knowledgeSkill ks1 = new knowledgeSkill();
		 //boolean result = ks1.addKnowledgeSkillDetails(5999, "Senior Sales Associate", "The more you know...", "Einstein");
		 ks1.deleteKnowledgeSkillDetails(5999);
		 //System.out.println(result);
	}
}
