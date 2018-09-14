--Create triggers on Courset and CourseSet_Skill tables
--update them accordingly as the master tables are updated

--Trigger1
create or replace 
trigger BEFORE_UPDATE_ON_COURSE 
BEFORE INSERT OR DELETE ON COURSE 
BEGIN
  delete from courseset;
END;

--trigger2:
create or replace 
trigger AFTER_UPDATE_ON_COURSE AFTER INSERT OR DELETE ON COURSE 
BEGIN
  INSERT INTO CourseSet
  (SELECT RowNum+20000, C1.CourseID, C2.CourseID,null,2,(C1.CoursePrice+C2.CoursePrice)
  FROM Course C1, Course C2
  WHERE C1.CourseID < C2.CourseID)
  UNION
  (SELECT RowNum+30000, C11.CourseID,C12.CourseID,C13.CourseID,3,(C11.CoursePrice+C12.CoursePrice+C13.CoursePrice)
  FROM Course C11,Course C12, Course C13
  WHERE C11.CourseID < C12.CourseID AND C12.CourseID < C13.CourseID );
END;

--Trigger3
create or replace 
trigger BEFORE_UPDATE_ON_COURSE_SKILL 
BEFORE INSERT OR DELETE ON COURSECOVERSSKILL 
BEGIN
  delete from courseset_skill;
END;

--Trigger4

create or replace 
trigger AFTER_UPDATE_ON_COURSE_SKILL AFTER INSERT OR DELETE ON COURSECOVERSSKILL 
BEGIN
 	INSERT INTO CourseSet_Skill
	SELECT CourseSetID,KSCode
	FROM CourseSet JOIN CourseCoversSkill ON CourseID = CID1
	UNION
	SELECT CourseSetID,KSCode
	FROM CourseSet JOIN CourseCoversSkill ON CourseID = CID2
	UNION
	SELECT CourseSetID,KSCode
	FROM CourseSet JOIN CourseCoversSkill ON CourseID = CID3;
END;

--Deletion Scripts
DROP TRIGGER BEFORE_UPDATE_ON_COURSE ;
DROP TRIGGER AFTER_UPDATE_ON_COURSE ;
DROP TRIGGER BEFORE_UPDATE_ON_COURSE_SKILL ;
DROP TRIGGER AFTER_UPDATE_ON_COURSE_SKILL ;
