CREATE TABLE Person(
PersonID 	INTEGER PRIMARY KEY,
PersonName 	VARCHAR(20),
PersonAddress 	INTEGER,
PersonStreet 	VARCHAR(50),
PersonCity 	VARCHAR(50),
PersonZipcode 	NUMERIC(5,0),
Email		VARCHAR(30),
Gender 		VARCHAR(20));

CREATE TABLE Phone(
PhoneNumber 	VARCHAR(20) PRIMARY KEY,
PersonID 	INTEGER,
PhoneType 	VARCHAR(15),
FOREIGN KEY (PersonID) REFERENCES Person);

CREATE TABLE Company(
CompanyID 		INTEGER PRIMARY KEY,
CompanyName 		VARCHAR(30),
CompanyAddress 	INTEGER,
CompanyStreet 		VARCHAR(50),
CompanyCity 		VARCHAR(50),
CompanyZipcode 	NUMERIC(5,0),
CompanySector 	VARCHAR(50),
CompanyWebsite 	VARCHAR(50) );

CREATE TABLE JobProfile(
JProfileCode 		INTEGER PRIMARY KEY,
JProfileTitle 		VARCHAR(30),
JProfileDescription 	VARCHAR(75));

CREATE TABLE Job (
JobCode 	INTEGER PRIMARY KEY,
JobType 	VARCHAR(20),
PayRate 	NUMERIC(8,2),
PayType 	VARCHAR(10),
CompanyID 	INTEGER,
JProfileCode 	INTEGER,
FOREIGN KEY (CompanyID) 	REFERENCES Company,
FOREIGN KEY (JProfileCode)	REFERENCES JobProfile );

CREATE TABLE Project(
ProjectID 	 INTEGER PRIMARY KEY,
ProjectTitle 	 VARCHAR(50),
ProjectDirector  VARCHAR(30),
BudgetCode 	 VARCHAR(10),
ProjectStartDate DATE,
ProjectEndDate  DATE );

CREATE TABLE KnowledgeSkill(
KSCode 	INTEGER PRIMARY KEY,
KSTitle 	VARCHAR(50),
KSDescription 	VARCHAR(100),
KSLevel 	VARCHAR(20));

CREATE TABLE Course(
CourseID 	INTEGER PRIMARY KEY,
CourseTitle 	VARCHAR(40),
CourseLevel 	VARCHAR(20),
CourseDescription VARCHAR(100),
CourseStatus 	VARCHAR(10),
CoursePrice 	NUMERIC(8,2));

CREATE TABLE Section(
SectionID 	INTEGER,
CourseID 	INTEGER,
Year	 	NUMERIC(4,0),
CompleteDate 	DATE,
OfferedBy 	VARCHAR(30),
Format 		VARCHAR(20),
SectionPrice 	NUMERIC(8,2),
PRIMARY KEY (SectionID,CourseID,Year,CompleteDate),
FOREIGN KEY (CourseID) REFERENCES Course );

CREATE TABLE Certificate(
CertCode 	INTEGER PRIMARY KEY,
CertTitle 	VARCHAR(50),
CertDescription VARCHAR(100),
ExpDate 	DATE,
IssuedBy 	VARCHAR(50),
CertToolCode 	INTEGER) ;

CREATE TABLE Works(
PersonID 	INTEGER,
JobCode 	INTEGER,
PRIMARY KEY  	(PersonID,JobCode),
FOREIGN KEY   	(PersonID) 	REFERENCES Person,
FOREIGN KEY   	(JobCode) 	REFERENCES Job);

CREATE TABLE PastWork(
PersonID 	INTEGER,
JobCode  	INTEGER,
StartDate 	DATE,
EndDate 	DATE,
PRIMARY KEY (PersonID,JobCode,StartDate),
FOREIGN KEY (PersonID) REFERENCES Person,
FOREIGN KEY (JobCode) REFERENCES Job(JobCode));

CREATE TABLE JobInProject(
JobCode 	INTEGER,
ProjectID 	INTEGER,
PRIMARY KEY (JobCode,ProjectID),
FOREIGN KEY (JobCode) REFERENCES Job,
FOREIGN KEY (ProjectID) REFERENCES Project);

CREATE TABLE CompanyLaunchesProject(
CompanyID 	INTEGER,
ProjectID 	INTEGER,
PRIMARY KEY (CompanyID,ProjectID),
FOREIGN KEY(CompanyID) REFERENCES Company,
FOREIGN KEY(ProjectID) REFERENCES Project);

CREATE TABLE PersonHasSkill(
PersonID  	INTEGER,
KSCode 	INTEGER,
PRIMARY KEY (PersonID,KSCode),
FOREIGN KEY (PersonID) REFERENCES Person,
FOREIGN KEY (KSCode) REFERENCES KnowledgeSkill);

CREATE TABLE JProfileRequiresSkill(
JProfileCode 	INTEGER,
KSCode 	INTEGER,
PRIMARY KEY (JProfileCode,KSCode),
FOREIGN KEY (JProfileCode) REFERENCES JobProfile,
FOREIGN KEY (KSCode) REFERENCES KnowledgeSkill);

CREATE TABLE PersonTakesSection(
PersonID 	INTEGER,
SectionID 	INTEGER,
CourseID 	INTEGER,
Year	 	NUMERIC(4,0),
CompleteDate 	DATE,
PRIMARY KEY (PersonID,SectionID),
FOREIGN KEY (PersonID) REFERENCES Person,
FOREIGN KEY (SectionID,CourseID,Year,CompleteDate) REFERENCES Section);

CREATE TABLE CourseCoversSkill(
CourseID INTEGER,
KSCode INTEGER,
PRIMARY KEY (CourseID,KSCode),
FOREIGN KEY (CourseID) REFERENCES Course,
FOREIGN KEY (KSCode) REFERENCES KnowledgeSkill);

CREATE TABLE PersonOwnsCertificate(
PersonID INTEGER,
CertCode INTEGER,
PRIMARY KEY (PersonID,CertCode),
FOREIGN KEY (PersonID) REFERENCES Person,
FOREIGN KEY (CertCode) REFERENCES Certificate);

CREATE TABLE JProfileRequiresCertificate(
JProfileCode INTEGER,
CertCode INTEGER,
PRIMARY KEY ( JProfileCode,CertCode),
FOREIGN KEY (JProfileCode) REFERENCES JobProfile,
FOREIGN KEY (CertCode) REFERENCES Certificate);

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
KSCode NUMERIC(3,0),
PRIMARY KEY (CourseSetID,KSCode)
);

CREATE TABLE GivenSkillSet
(
KSCode NUMERIC(3,0) PRIMARY KEY
);

CREATE SEQUENCE PTS_seq
START WITH 1
INCREMENT BY 1
MAXVALUE 99
NOCYCLE;

CREATE VIEW CoursesAloneCoversSkillSet AS
(SELECT CourseID,CourseTitle
FROM Course C
WHERE NOT EXISTS 
((SELECT *
FROM  GivenSkillSet)
MINUS
(SELECT KSCode
FROM CourseCoversSkill CCS
WHERE CCS.CourseID = C.CourseID)))
