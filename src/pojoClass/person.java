package pojoClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dbConnection.DbConnection;
import guiCareerManage.CareerServices;

public class person {
	private int personId; //PersonID 	INTEGER PRIMARY KEY,
	private String personName; //PersonName 	VARCHAR(20),
	private String personAddress; //PersonAddress 	INTEGER,
	private String personStreet; //PersonStreet 	VARCHAR(50),
	private String personCity; //PersonCity 	VARCHAR(50),
	private String personZipCode; //PersonZipcode 	NUMERIC(5,0),
	private String personEmail; //Email		VARCHAR(30),
	private String personGender; //Gender 		VARCHAR(20));
	private String personPhone; //PhoneNumber 	VARCHAR(20) PRIMARY KEY,
	private String phoneType; //PhoneType 	VARCHAR(15)
	
	DbConnection db = new DbConnection();
	Connection connection = null;
	PreparedStatement pstmt = null;
	CareerServices cs = new CareerServices();
	
	public person() {
		super();
	}
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getPersonAddress() {
		return personAddress;
	}
	public void setPersonAddress(String personAddress) {
		this.personAddress = personAddress;
	}
	public String getPersonStreet() {
		return personStreet;
	}
	public void setPersonStreet(String personStreet) {
		this.personStreet = personStreet;
	}
	public String getPersonCity() {
		return personCity;
	}
	public void setPersonCity(String personCity) {
		this.personCity = personCity;
	}
	public String getPersonZipCode() {
		return personZipCode;
	}
	public void setPersonZipCode(String personZipCode) {
		this.personZipCode = personZipCode;
	}
	public String getPersonEmail() {
		return personEmail;
	}
	public void setPersonEmail(String personEmail) {
		this.personEmail = personEmail;
	}
	public String getPersonGender() {
		return personGender;
	}
	public void setPersonGender(String personGender) {
		this.personGender = personGender;
	}
	public String getPersonPhone() {
		return personPhone;
	}
	public void setPersonPhone(String personPhone) {
		this.personPhone = personPhone;
	}
	public String getPhoneType() {
		return phoneType;
	}
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}
	
	public boolean addPersonDetails(int personId, String personName, String personAddress,
			String personStreet, String personCity, int personZipCode, String personEmail, String personGender) {
		
		/*PersonID 	INTEGER PRIMARY KEY,
		PersonName 	VARCHAR(20),
		PersonAddress 	INTEGER,
		PersonStreet 	VARCHAR(50),
		PersonCity 	VARCHAR(50),
		PersonZipcode 	NUMERIC(5,0),
		Email		VARCHAR(30),
		Gender 		VARCHAR(20));*/
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				
				pstmt = connection
						.prepareStatement("INSERT into PERSON values(?,?,?,?,?,?,?,?)");
				pstmt.setInt(1, personId);
				pstmt.setString(2, personName);
				pstmt.setString(3, personAddress);
				pstmt.setString(4, personStreet);
				pstmt.setString(5, personCity);
				pstmt.setInt(6, personZipCode);
				pstmt.setString(7, personEmail);
				pstmt.setString(8, personGender);
				result = pstmt.execute();
				System.out.println("Record inserted into PERSON table");
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
		System.out.println(result);
		return result;
	}
	
	public boolean addPersonPhoneDetails(String personPhone,int personId, String phoneType) {
		/*PhoneNumber 	VARCHAR(20) PRIMARY KEY,
		PersonID 	INTEGER,
		PhoneType 	VARCHAR(15),*/
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				/*CourseCoversSkill(
						CourseID INTEGER,
						KSCode INTEGER)*/
				pstmt = connection
						.prepareStatement("INSERT into PHONE values(?,?,?)");
				pstmt.setString(1, personPhone);
				pstmt.setInt(2, personId);
				pstmt.setString(3, phoneType);
				result = pstmt.execute();
				System.out
						.println("Record is inserted into PHONE table!");
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
	
	public boolean addPersonHasSkill(int personId, int ksCode) {
		/*PersonID  	INTEGER,
			KSCode 	INTEGER,*/
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				pstmt = connection
						.prepareStatement("INSERT into PERSONHASSKILL values(?,?)");
				pstmt.setInt(1, personId);
				pstmt.setInt(2, ksCode);
				result = pstmt.execute();
				System.out
						.println(result + " Record is inserted into PERSONHASSKILL table!");
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
	
	public boolean addPersonOwnsCertificate(int personId, int certCode) {
		/*PersonID INTEGER,
		CertCode INTEGER,*/
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				pstmt = connection
						.prepareStatement("INSERT into PERSONOWNSCERTIFICATE values(?,?)");
				pstmt.setInt(1, personId);
				pstmt.setInt(2, certCode);
				result = pstmt.execute();
				System.out
						.println("Record is inserted into PERSONOWNSCERTIFICATE table!");
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
	
	public boolean addPersonTakesSection(int personId, int sectionId, int courseId, String year, String completeDate) {
		/*PersonID 	INTEGER,
		SectionID 	INTEGER,
		CourseID 	INTEGER,
		Year	 	NUMERIC(4,0),
		CompleteDate 	DATE,*/
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				pstmt = connection
						.prepareStatement("INSERT into PERSONTAKESSECTION values(?,?,?,?,?)");
				pstmt.setInt(1, personId);
				pstmt.setInt(2, sectionId);
				pstmt.setInt(3, courseId);
				pstmt.setString(4, year);
				pstmt.setString(5, completeDate);
				result = pstmt.execute();
				System.out
						.println("Record is inserted into PERSONTAKESSECTION table!");
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
	
	public boolean addWorks(int personId, int jobCode) {
		/*PersonID 	INTEGER,
		JobCode 	INTEGER,*/
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				pstmt = connection
						.prepareStatement("INSERT into WORKS values(?,?)");
				pstmt.setInt(1, personId);
				pstmt.setInt(2, jobCode);
				result = pstmt.execute();
				System.out
						.println("Record is inserted into WORKS table!");
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
	
	public boolean deleteWorks(int personId) {
		boolean result = true;
		String query = "DELETE WORKS WHERE PERSONID = " + personId;
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
						.println(personId + " Record is deleted from WORKS table!");
			}
		} catch (SQLException e) {
			e.getMessage();
		} finally {
			System.out.println("Entered finally block..");
			System.out.println(result);
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
	public boolean addPastWork(int personId, int jobCode, String startDate, String endDate) {
		/*PersonID 	INTEGER,
		JobCode  	INTEGER,
		StartDate 	DATE,
		EndDate 	DATE,*/
		boolean result = true;
		try {
			connection = cs.getSqlconn();
			if (connection != null) {
				System.out.println("Connection received Successfully");
				pstmt = connection
						.prepareStatement("INSERT into PASTWORK values(?,?,?,?)");
				pstmt.setInt(1, personId);
				pstmt.setInt(2, jobCode);
				pstmt.setString(3, startDate);
				pstmt.setString(4, endDate);
				result = pstmt.execute();
				System.out
						.println("Record is inserted into PASTWORK table!");
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
	 
	/*public boolean deletePersonDetails(int personId)
	 * delete person records is not needed as of now this has so many dependencies, to delete person table 
	 we need to delete all records in personhasskill,personownscertificate,persontakessection tables.
	 in reality a person's details will not be deleted*/
	
	 public static void main(String[] args){
		 person p1 = new person();
		 //p1.addPersonDetails(1102, "Saranyan", "1610", "Robert E lee", "New Orleans", 70122, "ssenthiv@uno.edu", "Male");
		 //p1.addPersonPhoneDetails("5046667840", 1100, "mobile");
		 p1.addPersonHasSkill(1100, 565);
		// p1.addPersonOwnsCertificate(1100, 825);
		 //p1.addWorks(1100, 999);
		 //p1.addPastWork(1100, 998, "27-11-2012", "03-07-2015");
		 //p1.addPersonTakesSection(1100, 102, 600, "2015", "27-11-2016");
		 //p1.deleteWorks(1100);
		 }
}
