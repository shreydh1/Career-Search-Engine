package dbConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import guiCareerManage.CareerServices;

public class TableInfo {

	private Statement stmt;
	private ResultSet rs;
	static CareerServices cs;

	// static CareerServices cs = new CareerServices();
	public TableInfo(Connection con) throws SQLException {

		stmt = con.createStatement();
	}
	
	public TableInfo() throws SQLException {
		Connection con = DbConnection.getConnection("hlle","csci4125");
		stmt = con.createStatement();
	}

	/**
	 * list the user's table names
	 */
	public String[] listTableName() throws SQLException {
		String str = "SELECT Table_Name FROM USER_TABLES";

		rs = stmt.executeQuery(str);
		ArrayList al = new ArrayList();
		while (rs.next()) {
			al.add(rs.getString("Table_Name"));
		}
		String[] tn = new String[1];
		tn = (String[]) al.toArray(tn);
		return tn;
	}

	/**
	 * return the ResultSet object of a table
	 */
	public ResultSet getTable(String tn) throws SQLException {
		String str = "SELECT * FROM " + tn;

		rs = stmt.executeQuery(str);
		return rs;
	}

	public ResultSet getTable(String tn, int pk) throws SQLException {
		String str = null;
		switch (tn) {
			case "person":
				str = "SELECT * FROM " + tn + " WHERE personid = " + pk;
			break;
			case "phone":
				str = "SELECT * FROM " + tn + " WHERE personid = " + pk;
			break;
			case "company":
				str = "SELECT * FROM " + tn + " WHERE companyid = " + pk;
			break;
			case "jobprofile":
				str = "SELECT * FROM " + tn + " WHERE jprofilecode = " + pk;
			break;
			case "job":
				str = "SELECT * FROM " + tn + " WHERE jobcode = " + pk;
			break;
			case "project":
				str = "SELECT * FROM " + tn + " WHERE projectid = " + pk;
			break;
			case "kwowledgeskill":
				str = "SELECT * FROM " + tn + " WHERE kscode = " + pk;
			break;
			case "course":
				str = "SELECT * FROM " + tn + " WHERE courseid = " + pk;
			break;
			case "certificate":
				str = "SELECT * FROM " + tn + " WHERE certcode = " + pk;
			break;
		}

		System.out.println(str);
		rs = stmt.executeQuery(str);
		return rs;
	}

	/**
	 * convert a ResultSet object to a two dimensional array of String
	 */
	public Vector resultSet2Vector(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int col = rsmd.getColumnCount();
		Vector vec = new Vector();
		Vector row = null;
		while (rs.next()) {
			row = new Vector();
			for (int i = 0; i < col; i++) {
				row.add(rs.getObject(i + 1));
			}
			vec.add(row);
		}
		return vec;
	}

	/**
	 * return the columns' titles of a table as a Vector
	 */
	public Vector getTitlesAsVector(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int col = rsmd.getColumnCount();
		Vector title = new Vector();
		for (int i = 0; i < col; i++) {
			title.add(rsmd.getColumnLabel(i + 1));
		}
		return title;
	}

	/**
	 * return a String array of the column names
	 */

	// query 1
	public ResultSet ListCompanyWorkers(int compID) throws SQLException {
		String str = "SELECT PersonID, PersonName" + " FROM Person NATURAL JOIN Works NATURAL JOIN Job"
				+ " WHERE CompanyID =" + compID;

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 2
	public ResultSet ListCompanyWorkersBySalaryDesc(int compID) throws SQLException {
		String str = "SELECT PersonID, PayRate" + " FROM Job NATURAL JOIN Works" + " WHERE CompanyID = " + compID
				+ " AND PayType = 'Salary' " + " ORDER BY PayRate DESC";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 3
	public ResultSet ListCompaniesLaborCostDesc() throws SQLException {
		String str = "SELECT CompanyID," + "SUM(CASE 	WHEN PayType = 'Salary' THEN PayRate" + " ELSE PayRate*1920"
				+ " END ) AS LaborCost" + " FROM Job NATURAL JOIN Works" + " GROUP BY CompanyID"
				+ " ORDER BY LaborCost DESC";
		System.out.println(str);

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 4
	public ResultSet ListJobsOfPerson(int perID) throws SQLException {
		String str = "SELECT JobCode " + "FROM Works " + "WHERE PersonID = " + perID;

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 5
	public ResultSet ListWorkersInProject(int projID) throws SQLException {
		String str = "SELECT PersonID " + "FROM Works NATURAL JOIN JobInProject " + "WHERE ProjectID = " + projID;

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 6
	public ResultSet ListPersonKnowledgeSkill(int perID) throws SQLException {
		String str = "SELECT KSCode,KSTitle,KSDescription,KSLevel " + "FROM PersonHasSkill NATURAL JOIN KnowledgeSkill "
				+ "WHERE PersonID = " + perID;

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 7
	public ResultSet ListSkillGapFromJobs(int perID) throws SQLException {
		String str = "(SELECT KSCode " + "FROM JProfileREquiresSkill NATURAL JOIN Job NATURAL JOIN Works "
				+ "WHERE PersonID = " + perID + ") " + "MINUS " + "(SELECT KSCode " + "FROM PersonHasskill "
				+ "WHERE PersonID = " + perID + ")";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 8
	public ResultSet ListRequiredSkillsOfJob(int jprofileCode) throws SQLException {
		String str = "SELECT KSCode,KSTitle,KSDescription,KSLevel "
				+ "FROM KnowledgeSkill NATURAL JOIN JprofileRequiresSkill " + "WHERE JProfileCode = " + jprofileCode;

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 9 = FIXED add parameter int perID
	public ResultSet ListMissingSkillsForJob(int jobCode, int perID) throws SQLException {
		String str = "SELECT * " + "FROM KnowledgeSkill " + "WHERE KSCode in " + "((SELECT KSCode "
				+ "FROM JProfileRequiresSkill NATURAL JOIN Job " + "WHERE JobCode = " + jobCode + ") " + "MINUS "
				+ "(SELECT KSCode " + "FROM PersonHasSkill " + "WHERE PersonID = " + perID + "))";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 10
	public ResultSet FindCourseCoverSkillSet(String[] ids) throws SQLException {
		stmt.executeUpdate("DELETE FROM GivenSkillSet");
		for (String id : ids) {
			stmt.executeUpdate("INSERT INTO GivenSkillSet VALUES (" + id +")");
		}

		String str = "SELECT CourseID,CourseTitle " + "FROM Course C " + "WHERE NOT EXISTS  " + "((SELECT * "
				+ "FROM  GivenSkillSet) " + "MINUS " + "(SELECT KSCode " + "FROM CourseCoversSkill CCS "
				+ "WHERE CCS.CourseID = C.CourseID))";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 11
	public ResultSet FindCoursesTeachPersonForJob(int jobCode, int perID) throws SQLException {
		String str = "WITH MissingKS(missingKS) AS  " + "((SELECT KSCode "
				+ "FROM JProfileRequiresSkill NATURAL JOIN Job " + "WHERE JobCode = " + jobCode + ") " + "MINUS "
				+ "(SELECT KSCode " + "FROM PersonHasSkill " + "WHERE PersonID = " + perID + ")) "
				+ "SELECT CourseID,CourseTitle " + "FROM Course C " + "WHERE NOT EXISTS  " + "((SELECT * "
				+ "FROM  MissingKS) " + "MINUS " + "(SELECT KSCode " + "FROM CourseCoversSkill CCS "
				+ "WHERE CCS.CourseID = C.CourseID))";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 12
	public ResultSet FindQuickestCourseForPerson(int jobCode, int perID) throws SQLException {
		String str = "WITH MissingKS(missingKS) AS  " + "((SELECT KSCode "
				+ "FROM JProfileRequiresSkill NATURAL JOIN Job " + "WHERE JobCode = " + jobCode + ") " + "MINUS "
				+ "(SELECT KSCode " + "FROM PersonHasSkill " + "WHERE PersonID = " + perID + ")), "
				+ "QualifiedCourses(C_ID) AS " + "(SELECT CourseID " + "FROM Course C  " + "WHERE NOT EXISTS  "
				+ "((SELECT * " + "FROM  MissingKS) " + "MINUS " + "(SELECT KSCode " + "FROM CourseCoversSkill CCS "
				+ "WHERE CCS.CourseID = C.CourseID))), " + "QualifiedSection(Courseid, CompleteDate) AS "
				+ "(SELECT Courseid, CompleteDate " + "FROM Section S JOIN QualifiedCourses ON S.CourseID = C_ID) "
				+ "SELECT Courseid, CompleteDate " + "FROM QualifiedSection "
				+ "WHERE CompleteDate = (SELECT MIN(CompleteDate) FROM QualifiedSection)";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 13
	public ResultSet FindCourseSetsCoverSkillSet(String[] ids) throws SQLException {
		stmt.executeUpdate("DELETE FROM GivenSkillSet");
		for (String id : ids) {
			stmt.executeUpdate("INSERT INTO GivenSkillSet VALUES (" + id + ")");
		}

		String str = "WITH QualifiedCourseSet AS ( " + "SELECT * " + "FROM CourseSet CS " + "WHERE NOT EXISTS ( "
				+ "SELECT * FROM GivenSkillSet " + "MINUS "
				+ "SELECT KSCode FROM CourseSet_Skill CSS WHERE CS.CourseSetID = CSS.CourseSetID )) "
				+ "SELECT CID1 CourseID1,CID2 CourseID2,CID3 CourseID3 " + "FROM QualifiedCourseSet "
				+ "WHERE C_Count = (SELECT MIN(C_Count) FROM QualifiedCourseSet )";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 14
	public ResultSet FindCourseSetsTeachPersonForJob(int jobCode, int perID) throws SQLException {
		String str = "WITH MissingSkills(KSCode) AS " + "((SELECT KSCode " + "FROM JProfileRequiresSkill "
				+ "WHERE JProfileCode = (SELECT JProfileCode FROM Job WHERE JobCode = " + jobCode + ")) " + "MINUS "
				+ "(SELECT KSCode FROM PersonHasSkill WHERE PersonID = " + perID + ")), " + "QualifiedCourseSet AS "
				+ "( " + "SELECT * " + "FROM CourseSet CS " + "WHERE NOT EXISTS " + "( "
				+ "SELECT * FROM MissingSkills " + "MINUS "
				+ "SELECT KSCode FROM CourseSet_Skill CSS WHERE CS.CourseSetID = CSS.CourseSetID " + ") " + ") "
				+ "SELECT CID1 CourseID1,CID2 CourseID2,CID3 CourseID3 " + "FROM QualifiedCourseSet";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 15
	public ResultSet FindCheapestCourseSetsPersonForJob(int jobCode, int perID) throws SQLException {
		String str = "WITH MissingSkills(KSCode) AS  " + "( " + "( " + "SELECT KSCode " + "FROM JProfileRequiresSkill "
				+ "WHERE JProfileCode = (SELECT JProfileCode FROM Job WHERE JobCode = " + jobCode + ")) " + "MINUS "
				+ "(SELECT KSCode FROM PersonHasSkill WHERE PersonID = " + perID + ") " + "), "
				+ "QualifiedCourseSet AS " + "( " + "SELECT * " + "FROM CourseSet CS " + "WHERE NOT EXISTS "
				+ "( SELECT * FROM MissingSkills " + "MINUS "
				+ "SELECT KSCode FROM CourseSet_Skill CSS WHERE CS.CourseSetID = CSS.CourseSetID) " + ") "
				+ "SELECT CID1 CourseID1,CID2 CourseID2,CID3 CourseID3,CSetPrice MinPrice " + "FROM QualifiedCourseSet "
				+ "WHERE CSetPrice =(SELECT MIN(CSetPrice) FROM QualifiedCourseSet)";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 16
	public ResultSet ListJobProfilesPersonQualify(int perID) throws SQLException {
		String str = "SELECT JprofileCode " + "FROM JobProfile JP " + "WHERE NOT EXISTS  " + "((SELECT KSCode "
				+ "FROM  JProfileRequiresSkill JRS " + "WHERE JRS.JProfileCode = JP.JProfileCode ) " + "MINUS "
				+ "(SELECT KSCode " + "FROM PersonHasSkill " + "WHERE PersonID = " + perID + " ))";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 17
	public ResultSet FindJobHighestPayForPerson(int perID) throws SQLException {
		String str = "WITH Pay_Rate_Table(Job_id,pay_rate) AS " + "( "
				+ "SELECT JobCode, ( CASE WHEN PayType ='Salary' THEN PayRate ELSE PayRate*1920 END) "
				+ "FROM Job NATURAL JOIN  " + "( " + "SELECT JprofileCode " + "FROM JobProfile JP "
				+ "WHERE NOT EXISTS  " + "((SELECT KSCode " + "FROM  JProfileRequiresSkill JRS "
				+ "WHERE JRS.JProfileCode = JP.JProfileCode ) " + "MINUS " + "(SELECT KSCode " + "FROM PersonHasSkill "
				+ "WHERE PersonID = " + perID + " ))" + ") " + ") " + "SELECT * " + "FROM Pay_Rate_Table "
				+ "WHERE pay_rate = (  SELECT MAX(pay_rate) " + "FROM Pay_Rate_Table)";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 18
	public ResultSet ListPeopleQualifiedForJProfile(int jprofileCode) throws SQLException {
		String str = "SELECT PersonName,Email " + "FROM Person P " + "WHERE NOT EXISTS " + "((SELECT KSCode "
				+ "FROM  JProfileRequiresSkill " + "WHERE JProfileCode =" + jprofileCode + " ) " + "MINUS "
				+ "(SELECT KSCode " + "FROM PersonHasSkill PHS " + "WHERE PHS.PersonID = P.PersonID))";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 19
	public ResultSet ListPeopleMissingOneSkillForJProfile(int jprofileCode) throws SQLException {
		String str = "WITH required_skills(KSCode) AS " + "(SELECT KSCode " + "FROM  JProfileRequiresSkill "
				+ "WHERE JProfileCode = " + jprofileCode + " ) " + "SELECT PersonID " + "FROM  Person P "
				+ "WHERE 1 = ( SELECT COUNT(*)" + "FROM	 " + "((SELECT * " + "FROM required_skills) " + "MINUS "
				+ "(SELECT KSCode " + "FROM PersonHasSkill PHS " + "WHERE P.PersonID = PHS.PersonID)))";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 20
	public ResultSet ListSkillWithPeopleCountInMissingOneList(int jprofileCode) throws SQLException {
		String str = "WITH required_skills(KSCode) AS " + "(SELECT KSCode " + "FROM  JProfileRequiresSkill "
				+ "WHERE JProfileCode = " + jprofileCode + "), " + "MissingOneList(PersonID) AS " + "( "
				+ "SELECT PersonID " + "FROM  Person P " + "WHERE 1 = ( SELECT COUNT(*) " + "FROM	 " + "((SELECT * "
				+ "FROM required_skills) " + "MINUS " + "(SELECT KSCode " + "FROM PersonHasSkill PHS "
				+ "WHERE P.PersonID = PHS.PersonID)) " + ")), " + "Person_ID(PersonID) AS " + "(SELECT PersonID  "
				+ "FROM Person) " + "SELECT KSCode, COUNT(*) AS num_of_people "
				+ "FROM ((SELECT * FROM Person_ID,required_skills) MINUS (SELECT * FROM PersonHasSkill)) NATURAL JOIN MissingOneList "
				+ "GROUP BY KSCode " + "ORDER BY num_of_people";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 21
	public ResultSet ListPeopleMissLeastSkillForJProfile(int jprofileCode) throws SQLException {
		String str = "WITH required_skills(KSCode) AS " + "(SELECT KSCode " + "FROM  JProfileRequiresSkill "
				+ "WHERE JProfileCode = " + jprofileCode + "), " + "Person_ID(PersonID) AS " + "(SELECT PersonID  "
				+ "FROM Person), " + "MissingSkillTable(PersonID,Missing_Skill_Count) AS " + "( "
				+ "SELECT PersonID,COUNT(*) "
				+ "FROM ((SELECT * FROM Person_id,required_skills) MINUS (SELECT * FROM PersonHasSkill)) "
				+ "GROUP BY PersonID " + ") " + "SELECT PersonID,Missing_Skill_Count AS Least_Missing_Skill_Count "
				+ "FROM MissingSkillTable "
				+ "WHERE Missing_Skill_Count = ( SELECT MIN(Missing_Skill_Count) FROM MissingSkillTable )";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 22
	public ResultSet MissingKListForJProfile(int jprofileCode, int k) throws SQLException {
		String str = "WITH required_skills(KSCode) AS " + "(SELECT KSCode " + "FROM  JProfileRequiresSkill "
				+ "WHERE JProfileCode = " + jprofileCode + "), " + "Person_ID(PersonID) AS " + "(SELECT PersonID  "
				+ "FROM Person), " + "MissingSkillTable(PersonID,Missing_Skill_Count) AS " + "( "
				+ "SELECT PersonID,COUNT(*) "
				+ "FROM ((SELECT * FROM Person_id,required_skills) MINUS (SELECT * FROM PersonHasSkill)) "
				+ "GROUP BY PersonID " + ") " + "SELECT PersonID,Missing_Skill_Count " + "FROM MissingSkillTable "
				+ "WHERE Missing_Skill_Count <= " + k + "ORDER BY Missing_Skill_Count ASC";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 23
	public ResultSet ListSkillWithPeopleCountInMissingKList(int jprofileCode, int k) throws SQLException {
		String str = "WITH required_skills(KSCode) AS " + "(SELECT KSCode " + "FROM  JProfileRequiresSkill "
				+ "WHERE JProfileCode = " + jprofileCode + "), " + "Person_ID(PersonID) AS " + "(SELECT PersonID  "
				+ "FROM Person), " + "MissingSkillTable(PersonID,Missing_Skill_Count) AS " + "( "
				+ "SELECT PersonID,COUNT(*) "
				+ "FROM ((SELECT * FROM Person_id,required_skills) MINUS (SELECT * FROM PersonHasSkill)) "
				+ "GROUP BY PersonID " + "), " + "Missing_K_List( PersonID ) AS (" + "SELECT PersonID "
				+ "FROM MissingSkillTable " + "WHERE Missing_Skill_Count <= " + k + ") "
				+ "SELECT KSCode Needed_Skill, COUNT(*) Num_People_In_Need "
				+ "FROM ((SELECT * FROM Person_id,required_skills) MINUS (SELECT * FROM PersonHasSkill)) NATURAL JOIN Missing_K_List "
				+ "GROUP BY KSCode " + "ORDER BY Num_People_In_Need DESC";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 24
	public ResultSet ListPeopleHeldJobsOfJProfile(int jprofileCode) throws SQLException {
		String str = "WITH special_jprofile_Job(JobCode) AS " + "( " + "SELECT JobCode  " + "FROM Job "
				+ "WHERE JProfileCode =  " + jprofileCode + ") " + "SELECT PersonID  "
				+ "FROM PastWork NATURAL JOIN special_jprofile_Job";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 25
	public ResultSet ListUnemployedPeopleHeldJobsOfJProfile(int jprofileCode) throws SQLException {
		String str = "WITH special_jprofile_Job(JobCode) AS " + "( " + "SELECT JobCode  " + "FROM Job "
				+ "WHERE JProfileCode = " + jprofileCode + "), " + "unemployed_person(PersonID) AS ( "
				+ "(SELECT PersonID FROM person) " + "MINUS " + "(SELECT PersonID FROM works) ) " + "SELECT PersonID  "
				+ "FROM PastWork NATURAL JOIN special_jprofile_Job NATURAL JOIN unemployed_person";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 26a
	public ResultSet FindBiggestEmployerInNumEmployee() throws SQLException {
		String str = "WITH companySizeTable(CompanyID, employeeNum) AS " + "(SELECT CompanyID, COUNT(*) "
				+ "FROM Job NATURAL JOIN Works " + "GROUP BY CompanyID) " + "SELECT CompanyID, employeeNum "
				+ "FROM companySizeTable " + "WHERE employeeNum = ( SELECT MAX( employeeNum ) FROM companySizeTable )";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 26b
	public ResultSet FindBiggestEmployerInLaborCost() throws SQLException {
		String str = "WITH companyLaborCostTable(CompanyID, laborCost) AS "
				+ "(SELECT CompanyID, SUM( CASE 	WHEN PayType = 'Salary' THEN PayRate " + "ELSE PayRate*1920 END ) "
				+ "FROM Job NATURAL JOIN Works " + "GROUP BY CompanyID) " + "SELECT CompanyID, laborCost "
				+ "FROM companyLaborCostTable "
				+ "WHERE laborCost = ( SELECT MAX( laborCost ) FROM companyLaborCostTable )";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 27a
	public ResultSet FindBiggestSectorInNumEmployee() throws SQLException {
		String str = "WITH SectorSizeTable(CompanySector, employeeNum) AS  " + "(SELECT CompanySector, COUNT(*) "
				+ "FROM Job NATURAL JOIN Works NATURAL JOIN Company " + "GROUP BY CompanySector) "
				+ "SELECT CompanySector, employeeNum " + "FROM SectorSizeTable "
				+ "WHERE employeeNum = ( SELECT MAX( employeeNum ) FROM SectorSizeTable )";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 27b
	public ResultSet FindBiggestSectorInLaborCost() throws SQLException {
		String str = "WITH SectorLaborCostTable(CompanySector, laborCost) AS "
				+ "(SELECT CompanySector, SUM( CASE 	WHEN PayType = 'Salary' THEN PayRate  "
				+ "ELSE PayRate*1920 END ) " + "FROM Job NATURAL JOIN Works NATURAL JOIN Company "
				+ "GROUP BY CompanySector) " + "SELECT CompanySector, laborCost " + "FROM SectorLaborCostTable "
				+ "WHERE laborCost = ( SELECT MAX( laborCost ) FROM SectorLaborCostTable )";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 28a
	public ResultSet FindRatioEarningInCreaseAndDecrease() throws SQLException {
		String str = "WITH PersonLastPastWork(PersonID, JobCode) AS "
				+ "(SELECT PersonID, JobCode FROM PastWork NATURAL JOIN " + "(SELECT PersonID, MAX(EndDate) AS EndDate "
				+ "FROM PastWork " + "GROUP BY PersonID)	), "
				+ "PersonLastPastWorkPayRate(PersonID, LastPastWorkPayRate) AS "
				+ "(SELECT PersonID, ( CASE 	WHEN PayType = 'Salary' THEN PayRate "
				+ "			ELSE PayRate*1920 END ) " + "FROM Job NATURAL JOIN PersonLastPastWork ), "
				+ "PersonCurrentMaxPayRate(PersonID, CurrentMaxPayRate)  AS "
				+ "(SELECT PersonID, MAX( CASE 	WHEN PayType = 'Salary' THEN PayRate  "
				+ "			ELSE PayRate*1920 END ) " + "FROM Job NATURAL JOIN Works " + "GROUP BY PersonID ) "
				+ "SELECT ROUND(NumPeopleEarningIncrease / NumPeopleEarningDecrease,2) AS Earning_Inc_Dec_Ratio "
				+ "FROM (SELECT COUNT(*) AS NumPeopleEarningDecrease "
				+ "FROM PersonLastPastWorkPayRate NATURAL JOIN PersonCurrentMaxPayRate "
				+ "WHERE CurrentMaxPayRate < LastPastWorkPayRate) , (SELECT COUNT(*) AS NumPeopleEarningIncrease "
				+ "FROM PersonLastPastWorkPayRate NATURAL JOIN PersonCurrentMaxPayRate "
				+ "WHERE CurrentMaxPayRate > LastPastWorkPayRate)";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 28b
	public ResultSet FindEarningImprovementRateInSector(String sector) throws SQLException {
		String str = "WITH PersonLastPastWork(PersonID, JobCode) AS "
				+ "(SELECT PersonID, JobCode FROM PastWork NATURAL JOIN " + "(SELECT PersonID, MAX(EndDate) AS EndDate "
				+ "FROM PastWork " + "GROUP BY PersonID)	), "
				+ "PersonLastPastWorkPayRate(PersonID, LastPastWorkPayRate) AS "
				+ "(SELECT PersonID, ( CASE 	WHEN PayType = 'Salary' THEN PayRate "
				+ "			ELSE PayRate*1920 END ) " + "FROM Job NATURAL JOIN PersonLastPastWork ), "
				+ "PersonCurrentPayRate(PersonID, JobCode, CurrentPayRate) AS "
				+ "(SELECT PersonID, JobCode, CASE 	WHEN PayType = 'Salary' THEN PayRate "
				+ "						ELSE PayRate*1920 END " + "FROM Job NATURAL JOIN Works ), "
				+ "PersonCurrentMaxPayRate(PersonID, JobCode, CurrentMaxPayRate)  AS "
				+ "(SELECT PersonID, JobCode,  CurrentMaxPayRate " + "FROM PersonCurrentPayRate NATURAL JOIN "
				+ "(SELECT PersonID, MAX( CASE 	WHEN PayType = 'Salary' THEN PayRate "
				+ "						ELSE PayRate*1920 END ) CurrentMaxPayRate " + "FROM Job NATURAL JOIN Works "
				+ "GROUP BY PersonID) " + "WHERE CurrentPayRate = CurrentMaxPayRate ), "
				+ "PersonPastAndCurrentEarning( PersonID, PastEarning, CurrentEarning ) AS "
				+ "( SELECT PersonID, LastPastWorkPayRate, CurrentMaxPayRate "
				+ "FROM PersonLastPastWorkPayRate NATURAL JOIN PersonCurrentMaxPayRate NATURAL JOIN Job NATURAL JOIN Company "
				+ "WHERE CompanySector = \'" + sector + "\' ) "
				+ "SELECT ROUND( AVG((CurrentEarning - PastEarning)/PastEarning) ,2)*100 AS Avg_Earn_Inc_Percentage "
				+ "FROM PersonPastAndCurrentEarning";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 29
	public ResultSet FindJobProfileWithHighestDemand() throws SQLException {
		String str = "WITH JoblessPerson(PersonID) AS " + "( SELECT PersonID "
				+ "FROM ( SELECT PersonID FROM Person ) MINUS ( SELECT PersonID FROM Works ) ), "
				+ "JProfileAndQualifiedPerson ( JProfileCode, PersonID ) AS  " + "(SELECT JProfileCode, PersonID "
				+ "FROM JobProfile JPr, JoblessPerson JP " + "WHERE NOT EXISTS  " + "((SELECT KSCode "
				+ "FROM JProfileRequiresSkill JPRS " + "WHERE JPRS.JProfileCode = JPr.JProfileCode)  " + "MINUS "
				+ "(SELECT KSCode " + "FROM PersonHasSkill PHS " + "WHERE PHS.PersonID = JP.PersonID))), "
				+ "JProfileAndNumQualifiedPeople ( JProfileCode, NumQualifiedPeople ) AS "
				+ "(SELECT JProfileCode, COUNT(*) " + "FROM JProfileAndQualifiedPerson " + "GROUP BY JProfileCode "
				+ "UNION " + "SELECT JProfileCode, 0 "
				+ "FROM ((SELECT JProfileCode FROM JobProfile) MINUS (SELECT JProfileCode FROM  JProfileAndQualifiedPerson ))), "
				+ "VacantJob ( JobCode, JProfileCode ) AS " + "(SELECT JobCode, JProfileCode "
				+ "FROM Job NATURAL JOIN ( (SELECT JobCode FROM Job) MINUS (SELECT JobCode FROM Works) ) ), "
				+ "JProfileAndNumVacantJob ( JProfileCode, NumVacantJob ) AS " + "(SELECT JProfileCode, COUNT(*) "
				+ "FROM VacantJob " + "GROUP BY JProfileCode), "
				+ "VacanciesAndQualifiedPeople ( JProfileCode, NumVacantJob, NumQualifiedPeople ) AS " + "(SELECT * "
				+ "FROM JProfileAndNumVacantJob NATURAL JOIN JProfileAndNumQualifiedPeople) "
				+ "SELECT JProfileCode, NumVacantJob, NumQualifiedPeople  " + "FROM VacanciesAndQualifiedPeople "
				+ "WHERE (NumVacantJob - NumQualifiedPeople) =  " + "( SELECT MAX( NumVacantJob - NumQualifiedPeople ) "
				+ "FROM VacanciesAndQualifiedPeople )";

		rs = stmt.executeQuery(str);
		return rs;
	}

	// query 30
	public ResultSet FindCourseHelpMostPeople() throws SQLException {
		String str = "WITH JoblessPerson(PersonID) AS " + "( SELECT PersonID "
				+ "FROM ( SELECT PersonID FROM Person ) MINUS ( SELECT PersonID FROM Works ) ), "
				+ "JProfileAndQualifiedPerson ( JProfileCode, PersonID ) AS  " + "(SELECT JProfileCode, PersonID "
				+ "FROM JobProfile JPr, JoblessPerson JP " + "WHERE NOT EXISTS  " + "((SELECT KSCode "
				+ "FROM JProfileRequiresSkill JPRS " + "WHERE JPRS.JProfileCode = JPr.JProfileCode)  " + "MINUS "
				+ "(SELECT KSCode " + "FROM PersonHasSkill PHS " + "WHERE PHS.PersonID = JP.PersonID))), "
				+ "JProfileAndNumQualifiedPeople ( JProfileCode, NumQualifiedPeople ) AS "
				+ "(SELECT JProfileCode, COUNT(*) " + "FROM JProfileAndQualifiedPerson " + "GROUP BY JProfileCode "
				+ "UNION " + "SELECT JProfileCode, 0 "
				+ "FROM ((SELECT JProfileCode FROM JobProfile) MINUS (SELECT JProfileCode FROM  JProfileAndQualifiedPerson ))), "
				+ "VacantJob ( JobCode, JProfileCode ) AS " + "(SELECT JobCode, JProfileCode "
				+ "FROM Job NATURAL JOIN ( (SELECT JobCode FROM Job) MINUS (SELECT JobCode FROM Works) ) ), "
				+ "JProfileAndNumVacantJob ( JProfileCode, NumVacantJob ) AS " + "(SELECT JProfileCode, COUNT(*) "
				+ "FROM VacantJob " + "GROUP BY JProfileCode), "
				+ "VacanciesAndQualifiedPeople ( JProfileCode, NumVacantJob, NumQualifiedPeople ) AS " + "(SELECT * "
				+ "FROM JProfileAndNumVacantJob NATURAL JOIN JProfileAndNumQualifiedPeople), "
				+ "JProfileWithHighestDemand AS " + "( SELECT JProfileCode, NumVacantJob, NumQualifiedPeople  "
				+ "FROM VacanciesAndQualifiedPeople " + "WHERE (NumVacantJob - NumQualifiedPeople) =  "
				+ "( SELECT MAX( NumVacantJob - NumQualifiedPeople ) " + "FROM VacanciesAndQualifiedPeople )), "
				+ "NeededSkill(KSCode) AS  " + "(SELECT KSCode " + "FROM JProfileRequiresSkill "
				+ "WHERE JProfileCode = (SELECT JProfileCode FROM JProfileWithHighestDemand)) "
				+ "SELECT CourseID,CourseTitle " + "FROM Course C " + "WHERE EXISTS  " + "((SELECT * "
				+ "FROM  NeededSkill) " + "INTERSECT " + "(SELECT KSCode " + "FROM CourseCoversSkill CCS "
				+ "WHERE CCS.CourseID = C.CourseID))";

		rs = stmt.executeQuery(str);
		return rs;
	}

	public ResultSet getTableCount(String tn) throws SQLException {

		String str = "SELECT count(*) FROM " + tn;

		rs = stmt.executeQuery(str);
		return rs;
	}
	
	
	public static void main(String[] args) throws SQLException {

		TableInfo ti = new TableInfo();
		//ListCompanyWorkers(301);
		//System.out.println(qr.ListCompanyWorkersBySalaryDesc(305));
		//System.out.println(ti.ListCompaniesLaborCostDesc());
		//ListJobsOfPerson(1003);
		//ListWorkersInProject(400);
		//ListPersonKnowledgeSkill(1056);
		//ListSkillGapFromJobs(1056);
		//ListRequiredSkillsOfJob(215);
		//ListMissingSkillsForJob(953,1001);
		//System.out.println(ti.FindCourseCoverSkillSet(new String[] {"527","539"}));
		//FindCoursesTeachPersonForJob(926,1026);
		//FindQuickestCourseForPerson(926,1026);
		//FindCourseSetsCoverSkillSet (); ?
		//FindCourseSetsTeachPersonForJob(903,1054); ?
		//FindCheapestCourseSetsPersonForJob(903,1054);
		//ListJobProfilesPersonQualify(1001);
		//FindJobHighestPayForPerson(1013);
		//ListPeopleQualifiedForJProfile(213);
		//ListPeopleMissingOneSkillForJProfile(208);
		//ListSkillWithPeopleCountInMissingOneList(208);
		//ListPeopleMissLeastSkillForJProfile(225);
		//MissingKListForJProfile(225,4);
		//ListSkillWithPeopleCountInMissingKList(225,3);
		//ListPeopleHeldJobsOfJProfile(233);
		//ListUnemployedPeopleHeldJobsOfJProfile(233);
		//FindBiggestEmployerInNumEmployee ();
		//FindBiggestEmployerInLaborCost();
		//FindBiggestSectorInNumEmployee ();
		//FindBiggestSectorInLaborCost();
		//FindRatioEarningInCreaseAndDecrease();
		//FindEarningImprovementRateInSector("Tourism");
		//FindJobProfileWithHighestDemand();
		//qr.FindCourseHelpMostPeople();
	}

}
