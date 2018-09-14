--1. List a company’s workers by names.
SELECT PersonID, PersonName
FROM Person NATURAL JOIN Works NATURAL JOIN Job
WHERE CompanyID = 301;


--2. List a company’s staff by salary in descending order.
SELECT PersonID, PayRate
FROM Job NATURAL JOIN Works
WHERE CompanyID = 300 AND PayType = 'Salary' 
ORDER BY PayRate DESC;

 
--3. List companies’ labor cost (total salaries and wage rates by 1920 hours) in descending order.
SELECT CompanyID, SUM( CASE 	WHEN PayType = 'Salary' THEN PayRate 
				ELSE PayRate*1920 END ) AS LaborCost
FROM Job NATURAL JOIN Works
GROUP BY CompanyID
ORDER BY LaborCost DESC;


--4. Find all the Jobs a person is currently holding.
SELECT JobCode
FROM Works
WHERE PersonID = 1056;

 
--5. List all the workers who are working for a specific project.
SELECT PersonID
FROM Works NATURAL JOIN JobInProject
WHERE ProjectID = 400;

 
--6. List a person’s knowledge/skills in a readable format.
SELECT KSCode,KSTitle,KSDescription,KSLevel
FROM PersonHasSkill NATURAL JOIN KnowledgeSkill
WHERE PersonID = 1056;

 
--7. List the skill gap of a worker between his/her Job(s) and his/her skills.
(SELECT KSCode
FROM JProfileREquiresSkill NATURAL JOIN Job NATURAL JOIN Works
WHERE PersonID = 1056)
MINUS
(SELECT KSCode
FROM PersonHasskill
WHERE PersonID = 1056);


--8. List the required knowledge/skills of a Job profile in a readable format.
SELECT KSCode,KSTitle,KSDescription,KSLevel
FROM KnowledgeSkill NATURAL JOIN JprofileRequiresSkill
WHERE JProfileCode = 215;


--9. List a person’s missing knowledge/skills for a specific Job in a readable format. 
SELECT *
FROM KnowledgeSkill NATURAL JOIN
((SELECT KSCode
FROM JProfileRequiresSkill NATURAL JOIN Job
WHERE JobCode = 953)
MINUS
(SELECT KSCode
FROM PersonHasSkill
WHERE PersonID = 1001));


--10. Find the courses each of which alone can cover a given skill set

-- Queries to define the given skill set

CREATE TABLE GivenSkillSet
(
KSCode NUMERIC(3,0) PRIMARY KEY
);

DELETE FROM GivenSkillSet;
INSERT INTO GivenSkillSet VALUES (536);
INSERT INTO GivenSkillSet VALUES (556);

-- The Actual Query

SELECT CourseID,CourseTitle
FROM Course C
WHERE NOT EXISTS 
((SELECT *
FROM  GivenSkillSet)
MINUS
(SELECT KSCode
FROM CourseCoversSkill CCS
WHERE CCS.CourseID = C.CourseID));


--11. List the courses (course id and title) that each alone teaches all the missing knowledge/skills for a person to pursue a specific Job.
WITH MissingKS(missingKS) AS 
((SELECT KSCode
FROM JProfileRequiresSkill NATURAL JOIN Job
WHERE JobCode = 926)
MINUS
(SELECT KSCode
FROM PersonHasSkill
WHERE PersonID = 1026))

SELECT CourseID,CourseTitle
FROM Course C
WHERE NOT EXISTS 
((SELECT *
FROM  MissingKS)
MINUS
(SELECT KSCode
FROM CourseCoversSkill CCS
WHERE CCS.CourseID = C.CourseID));


--12. Suppose the skill gap of a worker and the requirement of a desired Job can be covered by one course. Find the “quickest” solution for this worker. Show the course and the completing date.
WITH MissingKS(missingKS) AS 
((SELECT KSCode
FROM JProfileRequiresSkill NATURAL JOIN Job
WHERE JobCode = 926)
MINUS
(SELECT KSCode
FROM PersonHasSkill
WHERE PersonID = 1026)),

QualifiedCourses(C_ID) AS
(SELECT CourseID
FROM Course C 
WHERE NOT EXISTS 
((SELECT *
FROM  MissingKS)
MINUS
(SELECT KSCode
FROM CourseCoversSkill CCS
WHERE CCS.CourseID = C.CourseID))),

QualifiedSection(Courseid, CompleteDate) AS
(SELECT Courseid, CompleteDate
FROM Section S JOIN QualifiedCourses ON S.CourseID = C_ID)

SELECT Courseid, CompleteDate
FROM QualifiedSection
WHERE CompleteDate = (SELECT MIN(CompleteDate) FROM QualifiedSection)


-- 13. If query #10 returns nothing, then find the course sets with the minimum number of courses that their combination 
--covers the given skill set.  The considered course sets will not include more than three courses.  

-- Preparation for the query, this preparation will supply for query 14 and 15 as well
-- Create the table CourseSet and CourseSet_Skill
-- The query can be done without creating these tables
-- Alternatives are using With clause or Create View
-- For the time being, creating these tables is a resort to minimize querying time
-- Please run this only ONCE
CREATE TABLE CourseSet
(
CourseSetID NUMERIC(5,0) PRIMARY KEY,
CID1 NUMERIC(3,0),
CID2 NUMERIC(3,0),
CID3 NUMERIC(3,0),
C_Count NUMERIC(1,0),
CSetPrice NUMERIC(8,2)
);

CREATE TABLE CourseSet_Skill
(
CourseSetID NUMERIC(5,0),
KSCode NUMERIC(3,0)
);

INSERT INTO CourseSet
(SELECT RowNum+20000, C1.CourseID, C2.CourseID,null,2,(C1.CoursePrice+C2.CoursePrice)
FROM Course C1, Course C2
WHERE C1.CourseID < C2.CourseID)
UNION
(SELECT RowNum+30000, C11.CourseID,C12.CourseID,C13.CourseID,3,(C11.CoursePrice+C12.CoursePrice+C13.CoursePrice)
FROM Course C11,Course C12, Course C13
WHERE C11.CourseID < C12.CourseID AND C12.CourseID < C13.CourseID );

INSERT INTO CourseSet_Skill
SELECT CourseSetID,KSCode
FROM CourseSet JOIN CourseCoversSkill ON CourseID = CID1
UNION
SELECT CourseSetID,KSCode
FROM CourseSet JOIN CourseCoversSkill ON CourseID = CID2
UNION
SELECT CourseSetID,KSCode
FROM CourseSet JOIN CourseCoversSkill ON CourseID = CID3;

-- Queries to define the given skill set

CREATE TABLE GivenSkillSet
(
KSCode NUMERIC(3,0) PRIMARY KEY
);

DELETE FROM GivenSkillSet;
INSERT INTO GivenSkillSet VALUES (536);
INSERT INTO GivenSkillSet VALUES (556);
INSERT INTO GivenSkillSet VALUES (549);


-- The Actual Query

WITH QualifiedCourseSet AS
(
SELECT *
FROM CourseSet CS
WHERE NOT EXISTS
  (
  SELECT * FROM GivenSkillSet
  MINUS
  SELECT KSCode FROM CourseSet_Skill CSS WHERE CS.CourseSetID = CSS.CourseSetID
  )
)
SELECT CID1 CourseID1,CID2 CourseID2,CID3 CourseID3
FROM QualifiedCourseSet
WHERE C_Count = (SELECT MIN(C_Count) FROM QualifiedCourseSet );

--14 List the course sets that their combinations cover all the missing knowledge/skills for a person to pursue a specific Job. The considered course sets will not include more than three courses
-- Please run the preparation script from query 13 to set up the needed tables for this query --
WITH MissingSkills(KSCode) AS
((SELECT KSCode
FROM JProfileRequiresSkill
WHERE JProfileCode = (SELECT JProfileCode FROM Job WHERE JobCode = 903))
MINUS
(SELECT KSCode FROM PersonHasSkill WHERE PersonID = 1054)),

QualifiedCourseSet AS
(
SELECT *
FROM CourseSet CS
WHERE NOT EXISTS
  (
  SELECT * FROM MissingSkills
  MINUS
  SELECT KSCode FROM CourseSet_Skill CSS WHERE CS.CourseSetID = CSS.CourseSetID
  )
)

SELECT CID1 CourseID1,CID2 CourseID2,CID3 CourseID3
FROM QualifiedCourseSet;


-- 15 Find the cheapest course choices to make up one’s skill gap by showing the courses to take and the total cost. The considered course sets will not include more than three courses.
-- Please run the preparation script from query 13 to set up the needed tables for this query --
WITH MissingSkills(KSCode) AS 
(
(
SELECT KSCode
FROM JProfileRequiresSkill
WHERE JProfileCode = (SELECT JProfileCode FROM Job WHERE JobCode = 926))

MINUS
(SELECT KSCode FROM PersonHasSkill WHERE PersonID = 1054)
),
QualifiedCourseSet AS
(
SELECT *
FROM CourseSet CS
WHERE NOT EXISTS
( SELECT * FROM MissingSkills
MINUS
SELECT KSCode FROM CourseSet_Skill CSS WHERE CS.CourseSetID = CSS.CourseSetID)
)
  
SELECT CID1 CourseID1,CID2 CourseID2,CID3 CourseID3,CSetPrice MinPrice
FROM QualifiedCourseSet
WHERE CSetPrice =(SELECT MIN(CSetPrice) FROM QualifiedCourseSet);


--16. List all the Job profiles that a person is qualified. 
SELECT JprofileCode
FROM JobProfile JP
WHERE NOT EXISTS 
((SELECT KSCode
FROM  JProfileRequiresSkill JRS
WHERE JRS.JProfileCode = JP.JProfileCode )
MINUS
(SELECT KSCode
FROM PersonHasSkill
WHERE PersonID = 1001));


--17. Find the Job with the highest pay rate for a person according to his/her skill qualification.
WITH Pay_Rate_Table(Job_id,pay_rate) AS
(
  SELECT JobCode, ( CASE WHEN PayType ='Salary' THEN PayRate ELSE PayRate*1920 END)
  FROM Job NATURAL JOIN 
  (
    SELECT JprofileCode
    FROM JobProfile JP
    WHERE NOT EXISTS 
    ((SELECT KSCode
    FROM  JProfileRequiresSkill JRS
    WHERE JRS.JProfileCode = JP.JProfileCode )
    MINUS
    (SELECT KSCode
    FROM PersonHasSkill
    WHERE PersonID = 1013))
  )
)
SELECT *
FROM Pay_Rate_Table
WHERE pay_rate = (  SELECT MAX(pay_rate)
                    FROM Pay_Rate_Table);


--18. List all the names along with the emails of the people who are qualified for a Job profile. 
SELECT PersonName,Email
FROM Person P
WHERE NOT EXISTS
((SELECT KSCode
FROM  JProfileRequiresSkill
WHERE JProfileCode =213 )
MINUS
(SELECT KSCode
FROM PersonHasSkill PHS
WHERE PHS.PersonID = P.PersonID));



--19. When a company cannot find any qualified person for a Job, a secondary solution is to find a person who is almost 
--    qualified to the Job.  Make a “missing-one” list that lists people who miss only one skill for a specified Job profile. 
WITH required_skills(KSCode) AS
(SELECT KSCode
 FROM  JProfileRequiresSkill
 WHERE JProfileCode = 208 )

SELECT PersonID
FROM  Person P
WHERE 1 = ( SELECT COUNT(*) 		
 			FROM	
 			((SELECT *
 			FROM required_skills)
 			MINUS
 			(SELECT KSCode
 			FROM PersonHasSkill PHS
 			WHERE P.PersonID = PHS.PersonID))
 		  );


-- 20. List the skillID and the number of people in the missing-one list for a given Job profile in the ascending order of the 
-- people counts.
WITH 
required_skills(KSCode) AS
(SELECT KSCode
 FROM  JProfileRequiresSkill
 WHERE JProfileCode = 208 ),
 
MissingOneList(PersonID) AS 
(
SELECT PersonID
FROM  Person P
WHERE 1 = ( SELECT COUNT(*) 		
		FROM	
		((SELECT *
		FROM required_skills)
		MINUS
		(SELECT KSCode
		FROM PersonHasSkill PHS
		WHERE P.PersonID = PHS.PersonID))
	  )
),
Person_ID(PersonID) AS 
(SELECT PersonID 
 FROM Person)
  
SELECT KSCode, COUNT(*) AS num_of_people
FROM ((SELECT * FROM Person_ID,required_skills) MINUS (SELECT * FROM PersonHasSkill)) NATURAL JOIN MissingOneList
GROUP BY KSCode
ORDER BY num_of_people;


--21. Suppose there is a new Job profile that has nobody qualified.  List the people who miss the least number of skills 
--    and report the “least number”. 
WITH required_skills(KSCode) AS
(SELECT KSCode
 FROM  JProfileRequiresSkill
 WHERE JProfileCode = 225 ),

Person_ID(PersonID) AS 
(SELECT PersonID 
 FROM Person),

MissingSkillTable(PersonID,Missing_Skill_Count) AS
(
SELECT PersonID,COUNT(*)
FROM ((SELECT * FROM Person_id,required_skills) MINUS (SELECT * FROM PersonHasSkill))
GROUP BY PersonID
)

SELECT PersonID,Missing_Skill_Count AS Least_Missing_Skill_Count
FROM MissingSkillTable
WHERE Missing_Skill_Count = ( SELECT MIN(Missing_Skill_Count) FROM MissingSkillTable );



--22.For a specified Job profile and a given small number k, make a “missing-k” list that lists the people’s IDs and the 
--   number of missing skills for the people who miss only up to k skills in the ascending order of missing skills. 
WITH required_skills(KSCode) AS
(SELECT KSCode
 FROM  JProfileRequiresSkill
 WHERE JProfileCode = 225 ),

Person_ID(PersonID) AS 
(SELECT PersonID 
 FROM Person),

MissingSkillTable(PersonID,Missing_Skill_Count) AS
(
SELECT PersonID,COUNT(*)
FROM ((SELECT * FROM Person_ID,required_skills) MINUS (SELECT * FROM PersonHasSkill))
GROUP BY PersonID
)

SELECT PersonID,Missing_Skill_Count
FROM MissingSkillTable
WHERE Missing_Skill_Count <= 4
ORDER BY Missing_Skill_Count ASC;


--23. Given a Job profile and its corresponding missing-k list specified in Question 22.  Find every skill that is needed 
--by at least one person in the given missing-k list.  List each skillID and the number of people who need it in the
--descending order of the people counts. 
WITH 
required_skills(KSCode) AS
(SELECT KSCode
 FROM  JProfileRequiresSkill
 WHERE JProfileCode = 225 ),

Person_ID(PersonID) AS 
(SELECT PersonID 
 FROM Person),

MissingSkillTable(PersonID,Missing_Skill_Count) AS
(
SELECT PersonID,COUNT(*)
FROM ((SELECT * FROM Person_id,required_skills) MINUS (SELECT * FROM PersonHasSkill))
GROUP BY PersonID
),
Missing_K_List( PersonID ) AS
(
SELECT PersonID
FROM MissingSkillTable
WHERE Missing_Skill_Count <= 3 -- Change this value to 4 or 5 to see the differences
)

SELECT KSCode Needed_Skill, COUNT(*) Num_People_In_Need
FROM ((SELECT * FROM Person_id,required_skills) MINUS (SELECT * FROM PersonHasSkill)) NATURAL JOIN Missing_K_List
GROUP BY KSCode
ORDER BY Num_People_In_Need DESC;


--24. In a local or national crisis, we need to find all the people who once held a Job of the special Job-profile identifier.
-- let jprofile 230 be special Job
WITH special_jprofile_Job(JobCode) AS
(
SELECT JobCode 
FROM Job
WHERE JProfileCode = 233
)

SELECT DISTINCT PersonID 
FROM PastWork NATURAL JOIN special_jprofile_Job;


-- Approach 2: 
SELECT DISTINCT PersonID 
FROM PastWork NATURAL JOIN Job
WHERE JProfileCode = 233;

-- approach 1 is beneficial in complexity, approach 2's query is shorter


--25. Find all the unemployed people who once held a Job of the given Job-profile identifier.
WITH special_jprofile_Job(JobCode) AS
(
SELECT JobCode 
FROM Job
WHERE JProfileCode = 233
),
unemployed_person(PersonID) AS (
  (SELECT PersonID FROM person)
MINUS
  (SELECT PersonID FROM works) )

SELECT PersonID 
FROM PastWork NATURAL JOIN special_jprofile_Job NATURAL JOIN unemployed_person;

--26. Find out the biggest employer in terms of number of employees or the total amount of salaries and wages paid to 
--employees.

--26a. in terms of number of employees
WITH companySizeTable(CompanyID, employeeNum) AS 
(SELECT CompanyID, COUNT(*)
FROM Job NATURAL JOIN Works
GROUP BY CompanyID)

SELECT CompanyID, employeeNum
FROM companySizeTable
WHERE employeeNum = ( SELECT MAX( employeeNum ) FROM companySizeTable );

--26b. in terms of total amount of pay
WITH companyLaborCostTable(CompanyID, laborCost) AS 
(SELECT CompanyID, SUM( CASE 	WHEN PayType = 'Salary' THEN PayRate 
						ELSE PayRate*1920 END )
FROM Job NATURAL JOIN Works
GROUP BY CompanyID)

SELECT CompanyID, laborCost
FROM companyLaborCostTable
WHERE laborCost = ( SELECT MAX( laborCost ) FROM companyLaborCostTable );


--27. Find out the job distribution among business sectors; find out the biggest sector in terms of number of employees 
--or the total amount of salaries and wages paid to employees.  (required for graduate students only)

--27a. in terms of number of employees
WITH SectorSizeTable(CompanySector, employeeNum) AS 
(SELECT CompanySector, COUNT(*)
FROM Job NATURAL JOIN Works NATURAL JOIN Company
GROUP BY CompanySector)

SELECT CompanySector, employeeNum
FROM SectorSizeTable
WHERE employeeNum = ( SELECT MAX( employeeNum ) FROM SectorSizeTable );

--27b. in terms of total amount of pay
WITH SectorLaborCostTable(CompanySector, laborCost) AS 
(SELECT CompanySector, SUM( CASE 	WHEN PayType = 'Salary' THEN PayRate 
						ELSE PayRate*1920 END )
FROM Job NATURAL JOIN Works NATURAL JOIN Company
GROUP BY CompanySector)

SELECT CompanySector, laborCost
FROM SectorLaborCostTable
WHERE laborCost = ( SELECT MAX( laborCost ) FROM SectorLaborCostTable );


-- 28. Find out the ratio between the people whose earnings increase and those whose earning decrease; find the average 
--rate of earning improvement for the workers in a specific business sector.  (required for graduate students only)

-- READ ME :
-- a person is said to have EARNING INCREASE when their CURRENT EARNING is greater than their PAST EARNING
-- the OPPOSITE holds for EARNING DECREASE
-- a person might have more than one past work, their PAST EARNING is defined as the PayRate of the MOST RECENT past work
-- a person might have more than one current work, their CURRENT EARNING is defined as the PayRate of the job with the HIGHEST PayRate


-- 28a. Find out the ratio between the people whose earnings increase and those whose earning decrease

-- Define a temporary view that maps a person with their most recent past work
WITH PersonLastPastWork(PersonID, JobCode) AS
(SELECT PersonID, JobCode FROM PastWork NATURAL JOIN
		(SELECT PersonID, MAX(EndDate) AS EndDate
		FROM PastWork
		GROUP BY PersonID)	),
		
-- Define a temporary view that maps a person with their most recent past work's PayRate
PersonLastPastWorkPayRate(PersonID, LastPastWorkPayRate) AS
(SELECT PersonID, ( CASE 	WHEN PayType = 'Salary' THEN PayRate 
						ELSE PayRate*1920 END )
FROM Job NATURAL JOIN PersonLastPastWork ),

-- Define a temporary view that maps a person with their current work's highest PayRate
PersonCurrentMaxPayRate(PersonID, CurrentMaxPayRate)  AS
(SELECT PersonID, MAX( CASE 	WHEN PayType = 'Salary' THEN PayRate 
						ELSE PayRate*1920 END )
FROM Job NATURAL JOIN Works
GROUP BY PersonID
)

SELECT ROUND(NumPeopleEarningIncrease / NumPeopleEarningDecrease,2) AS Earning_Inc_Dec_Ratio
FROM (SELECT COUNT(*) AS NumPeopleEarningDecrease
FROM PersonLastPastWorkPayRate NATURAL JOIN PersonCurrentMaxPayRate
WHERE CurrentMaxPayRate < LastPastWorkPayRate) , (SELECT COUNT(*) AS NumPeopleEarningIncrease
FROM PersonLastPastWorkPayRate NATURAL JOIN PersonCurrentMaxPayRate
WHERE CurrentMaxPayRate > LastPastWorkPayRate);


--28b.find the average rate of earning improvement for the workers in a specific business sector.

-- The rate of earning improvement is defined as the amount of earning increase from a person current's work to his past work, expressed in percentage.

-- Define a temporary view that maps a person with their most recent past work
WITH PersonLastPastWork(PersonID, JobCode) AS
(
SELECT PersonID, JobCode FROM PastWork NATURAL JOIN
		(SELECT PersonID, MAX(EndDate) AS EndDate
		FROM PastWork
		GROUP BY PersonID)		
),

-- Define a temporary view that maps a person with their most recent past work's PayRate
PersonLastPastWorkPayRate(PersonID, LastPastWorkPayRate) AS
(
SELECT PersonID, ( CASE 	WHEN PayType = 'Salary' THEN PayRate 
						ELSE PayRate*1920 END )
FROM Job NATURAL JOIN PersonLastPastWork		
),

-- Define a temporary view that maps a person with their current job and its PayRate
PersonCurrentPayRate(PersonID, JobCode, CurrentPayRate) AS
(
SELECT PersonID, JobCode, CASE 	WHEN PayType = 'Salary' THEN PayRate 
						ELSE PayRate*1920 END
FROM Job NATURAL JOIN Works
),

-- Define a temporary view that maps a person with the current job with the highest PayRate and its PayRate
PersonCurrentMaxPayRate(PersonID, JobCode, CurrentMaxPayRate)  AS
(SELECT PersonID, JobCode,  CurrentMaxPayRate
FROM PersonCurrentPayRate NATURAL JOIN
(SELECT PersonID, MAX( CASE 	WHEN PayType = 'Salary' THEN PayRate 
						ELSE PayRate*1920 END ) CurrentMaxPayRate
FROM Job NATURAL JOIN Works
GROUP BY PersonID)
WHERE CurrentPayRate = CurrentMaxPayRate ),

-- Define a temporary view that maps a person with their past earning and current earning, as defined above
PersonPastAndCurrentEarning( PersonID, PastEarning, CurrentEarning ) AS
(
SELECT PersonID, LastPastWorkPayRate, CurrentMaxPayRate
FROM PersonLastPastWorkPayRate NATURAL JOIN PersonCurrentMaxPayRate NATURAL JOIN Job NATURAL JOIN Company
WHERE CompanySector = 'Tourism'
)

-- Calculate and return the avarage earning increase rate in percentage (%)
SELECT ROUND( AVG((CurrentEarning - PastEarning)/PastEarning) ,2)*100 AS Avg_Earn_Increase_Percentage
FROM PersonPastAndCurrentEarning


--29. Find the job profiles that have the most openings due to lack of qualified workers.  If there are many opening jobs 
--of a job profile but at the same time there are many qualified jobless people.  Then training cannot help fill up this
--type of job.  What we want to find is such a job profile that has the largest difference between vacancies (the
--unfilled jobs of this job profile) and the number of jobless people who are qualified for this job profile.  

-- define a temporary view that contains only unemployed people
WITH JoblessPerson(PersonID) AS
(
SELECT PersonID
FROM ( SELECT PersonID FROM Person ) MINUS ( SELECT PersonID FROM Works ) 
),

-- define a temporary view that maps a JobProfile with the person who qualify for it
JProfileAndQualifiedPerson ( JProfileCode, PersonID ) AS 
(SELECT JProfileCode, PersonID
FROM JobProfile JPr, JoblessPerson JP
WHERE NOT EXISTS 
((SELECT KSCode
FROM JProfileRequiresSkill JPRS
WHERE JPRS.JProfileCode = JPr.JProfileCode) 
MINUS
(SELECT KSCode
FROM PersonHasSkill PHS
WHERE PHS.PersonID = JP.PersonID))),

-- define a temporary view that maps a JobProfile with the number of people who qualify for it
JProfileAndNumQualifiedPeople ( JProfileCode, NumQualifiedPeople ) AS
(SELECT JProfileCode, COUNT(*)
FROM JProfileAndQualifiedPerson
GROUP BY JProfileCode
UNION
SELECT JProfileCode, 0
FROM ((SELECT JProfileCode FROM JobProfile) MINUS (SELECT JProfileCode FROM  JProfileAndQualifiedPerson ))),

-- define a temporary view that maps a JobProfile with a Job which is described by it and is vacant
VacantJob ( JobCode, JProfileCode ) AS
(SELECT JobCode, JProfileCode
FROM Job NATURAL JOIN ( (SELECT JobCode FROM Job) MINUS (SELECT JobCode FROM Works) ) ),

-- define a temporary view that maps a JobProfile with the number of vacant Jobs described by it
JProfileAndNumVacantJob ( JProfileCode, NumVacantJob ) AS
(SELECT JProfileCode, COUNT(*)
FROM VacantJob
GROUP BY JProfileCode),

-- define a temporary view that maps a JobProfile with its number of vacant jobs and number of qualified people
VacanciesAndQualifiedPeople ( JProfileCode, NumVacantJob, NumQualifiedPeople ) AS
(SELECT *
FROM JProfileAndNumVacantJob NATURAL JOIN JProfileAndNumQualifiedPeople)

-- select the JobProfile that has the greatest difference between the number of vacancies and the number of qualified people
SELECT JProfileCode, NumVacantJob, NumQualifiedPeople 
FROM VacanciesAndQualifiedPeople
WHERE (NumVacantJob - NumQualifiedPeople) = 
( SELECT MAX( NumVacantJob - NumQualifiedPeople )
FROM VacanciesAndQualifiedPeople )

--30. Find the courses that can help most jobless people find a job by training them toward the job profiles that have the
--most openings due to lack of qualified workers. 

-- define a temporary view that contains only unemployed people
WITH JoblessPerson(PersonID) AS
(
SELECT PersonID
FROM ( SELECT PersonID FROM Person ) MINUS ( SELECT PersonID FROM Works ) 
),

-- define a temporary view that maps a JobProfile with the person who qualify for it
JProfileAndQualifiedPerson ( JProfileCode, PersonID ) AS 
(SELECT JProfileCode, PersonID
FROM JobProfile JPr, JoblessPerson JP
WHERE NOT EXISTS 
((SELECT KSCode
FROM JProfileRequiresSkill JPRS
WHERE JPRS.JProfileCode = JPr.JProfileCode) 
MINUS
(SELECT KSCode
FROM PersonHasSkill PHS
WHERE PHS.PersonID = JP.PersonID))),

-- define a temporary view that maps a JobProfile with the number of people who qualify for it
JProfileAndNumQualifiedPeople ( JProfileCode, NumQualifiedPeople ) AS
(SELECT JProfileCode, COUNT(*)
FROM JProfileAndQualifiedPerson
GROUP BY JProfileCode
UNION
SELECT JProfileCode, 0
FROM ((SELECT JProfileCode FROM JobProfile) MINUS (SELECT JProfileCode FROM  JProfileAndQualifiedPerson ))),

-- define a temporary view that maps a JobProfile with a Job which is described by it and is vacant
VacantJob ( JobCode, JProfileCode ) AS
(SELECT JobCode, JProfileCode

FROM Job NATURAL JOIN ( (SELECT JobCode FROM Job) MINUS (SELECT JobCode FROM Works) ) ),

-- define a temporary view that maps a JobProfile with the number of vacant Jobs described by it
JProfileAndNumVacantJob ( JProfileCode, NumVacantJob ) AS
(SELECT JProfileCode, COUNT(*)
FROM VacantJob
GROUP BY JProfileCode),

-- define a temporary view that maps a JobProfile with its number of vacant jobs and number of qualified people
VacanciesAndQualifiedPeople ( JProfileCode, NumVacantJob, NumQualifiedPeople ) AS
(SELECT *
FROM JProfileAndNumVacantJob NATURAL JOIN JProfileAndNumQualifiedPeople),

-- define a temporary view that contains the JobProfile that has the greatest difference between the number of vacancies and the number of qualified people
-- in other words, the JobProfile with highest demand of qualified people
JProfileWithHighestDemand AS
(
SELECT JProfileCode, NumVacantJob, NumQualifiedPeople 
FROM VacanciesAndQualifiedPeople
WHERE (NumVacantJob - NumQualifiedPeople) = 
( SELECT MAX( NumVacantJob - NumQualifiedPeople )
FROM VacanciesAndQualifiedPeople )
),

-- define a temporary view that contains the set of skills required by said JobProfile
NeededSkill(KSCode) AS 
(SELECT KSCode
FROM JProfileRequiresSkill
WHERE JProfileCode = (SELECT JProfileCode FROM JProfileWithHighestDemand))

-- select the courses that each teach one or more skills required, thus training people toward the said JobProfile
SELECT CourseID,CourseTitle
FROM Course C
WHERE EXISTS 
((SELECT *
FROM  NeededSkill)
INTERSECT
(SELECT KSCode
FROM CourseCoversSkill CCS
WHERE CCS.CourseID = C.CourseID));

