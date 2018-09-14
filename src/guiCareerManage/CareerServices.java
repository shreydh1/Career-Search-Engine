package guiCareerManage;

import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import dbConnection.DbConnection;
import dbConnection.TableInfo;
import pojoClass.*;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import java.awt.TextArea;

public class CareerServices {
	
	static String uName;
	static String pwd;
	JFrame frmCareerManagementService;
	TextArea queryDisplay;
	boolean result;
	private JButton tpsPerform;
	private JButton jpsPerform;
	private JButton jssPerform;
	static Connection sqlconn;

	public static Connection getSqlconn() {
		return sqlconn;
	}

	public static void setSqlconn(Connection sqlconn) {
		CareerServices.sqlconn = sqlconn;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws SQLException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CareerServices window = new CareerServices();
					uName = (String)JOptionPane.showInputDialog(
				        	"Enter Username",
				            "");
					
					if (uName == null || uName.isEmpty()) {
			            return;
			     }
				else{
					pwd = (String)JOptionPane.showInputDialog(
				        	"Enter Password",
				            "");
					if (pwd == null || pwd.isEmpty()) {
			            return;
			     }
					sqlconn = DbConnection.getConnection(uName, pwd);
				}
					if(sqlconn != null){
						window.frmCareerManagementService.setLocationRelativeTo(null);
						window.frmCareerManagementService.setVisible(true);
					}
					else
					{
						JOptionPane.showMessageDialog(window.frmCareerManagementService, "invalid username or Password");
					}
					
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CareerServices() {
		initialize();
		disableButtons();
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCareerManagementService = new JFrame();
		frmCareerManagementService.setTitle("Career Management Service");
		frmCareerManagementService.setBounds(100, 100, 946, 697);
		frmCareerManagementService.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCareerManagementService.getContentPane().setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 916, 26);
		frmCareerManagementService.getContentPane().add(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmViewTables = new JMenuItem("View tables");
		mntmViewTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET TABLE NAME", "Enter Table name to be viewed");
				try {
					//Connection conn = DbConnection.getConnection(uName,pwd);
					TableInfo ti = new TableInfo(sqlconn);
					ResultSet rss = ti.getTable(id);
					TableView tv = new TableView(rss,sqlconn,id);
					tv.tableviewframe.setVisible(true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnFile.add(mntmViewTables);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		JMenuItem mntmViewTableRow = new JMenuItem("View table row counts");
		mntmViewTableRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String id = getDetails("GET TABLE NAME", "Enter Table name \n whose count to be viewed");
				try {
					
					//Connection conn = DbConnection.getConnection(uName,pwd);
					TableInfo ti = new TableInfo(sqlconn);
					ResultSet rss = ti.getTableCount(id);
					TableView tv = new TableView(rss,sqlconn,id);
					tv.tableviewframe.setVisible(true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnFile.add(mntmViewTableRow);
		
		JMenuItem mntmQuerydriver = new JMenuItem("QueryDriver");
		mntmQuerydriver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					QueryDriver qr = new QueryDriver(sqlconn);
					qr.setVisible(true);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		JMenuItem mntmViewTableOn = new JMenuItem("Seach for key in tables");
		mntmViewTableOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET TABLE NAME", "Enter Table name, primary key\n whose details to be viewed"
						+ "\n only mainstream tables");
				if((id != null) && (id.length() > 0))
				{
					String[] ids=id.split(",",2);
					if(ids.length == 2){
						
						try 
						{
							//Connection conn = DbConnection.getConnection(uName,pwd);
							TableInfo ti = new TableInfo(sqlconn);
							ResultSet rss = ti.getTable(ids[0], Integer.parseInt(ids[1]));
							String title = "Person "+ids[1];
							TableView tv = new TableView(rss,sqlconn,title);
							tv.tableviewframe.setVisible(true);
						} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						}
					}
					else{queryDisplay.setText("Enter all inputs");}
				}
				else{return;}
			}
		});
		mnFile.add(mntmViewTableOn);
		mnFile.add(mntmQuerydriver);
		mnFile.add(mntmExit);
		
		JMenu mnOperations = new JMenu("Operations");
		menuBar.add(mnOperations);
		
		JMenu mnPerson = new JMenu("Person");
		mnOperations.add(mnPerson);
		
		JMenuItem mntmAddPerson = new JMenuItem("Add Person");
		mntmAddPerson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println(jssList.getSelectedIndex());
				AddPerson ap = new AddPerson();
		        ap.addPerson.setVisible(true);
			}
		});
		mnPerson.add(mntmAddPerson);
		
		JMenuItem mntmAddPersonSkill = new JMenuItem("Add Person Skill");
		mntmAddPersonSkill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET personhasskill details", "Enter details in the format \n <personid>,<kscode>");
				if((id != null) && (id.length() > 0))
				{
					person ap = new person();
					String[] ids = id.split(",", 2);
					if(ids.length == 2){
						System.out.println(ids[0]);
						System.out.println(ids[1]);
						result = ap.addPersonHasSkill(Integer.parseInt(ids[0]), Integer.parseInt(ids[1]));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into PERSONHASSKILL table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnPerson.add(mntmAddPersonSkill);
		
		JMenuItem mntmAddPersonCertificate = new JMenuItem("Add Person Certificate");
		mntmAddPersonCertificate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET CERTIFICATE details", "Enter details in the format \n <personid>,<certcode>");
				if((id != null) && (id.length() > 0))
				{
					person ap = new person();
					String[] ids = id.split(",", 2);
					if(ids.length == 2){
						System.out.println(ids[0]);
						System.out.println(ids[1]);
						result = ap.addPersonOwnsCertificate(Integer.parseInt(ids[0]), Integer.parseInt(ids[1]));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into PERSONOWNSCERTIFICATE table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnPerson.add(mntmAddPersonCertificate);
		
		JMenuItem mntmAddPersonContact = new JMenuItem("Add Person Contact");
		mntmAddPersonContact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET Contact details", "Enter details in the format \n <Phonenumber>,<personid>,<PhoneType>");
				if((id != null) && (id.length() > 0))
				{
					person ap = new person();
					String[] ids = id.split(",", 3);
					if(ids.length == 3){
						System.out.println(ids[0]);
						System.out.println(ids[1]);
						System.out.println(ids[2]);
						result = ap.addPersonPhoneDetails(ids[0], Integer.parseInt(ids[1]), ids[2]);
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into PERSONPHONE table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnPerson.add(mntmAddPersonContact);
		
		JMenuItem mntmAddPersonSection = new JMenuItem("Add Person Section Details");
		mntmAddPersonSection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String id = getDetails("GET SECTION details", "Enter details in the format \n <personid>,<sectionid>,<courseid>,\n<year>,<completedate(dd-mm-2015)>");
				if((id != null) && (id.length() > 0))
				{
					person ap = new person();
					String[] ids = id.split(",", 5);
					if(ids.length == 5){
						result = ap.addPersonTakesSection(Integer.parseInt(ids[0]), Integer.parseInt(ids[1]), Integer.parseInt(ids[2])
								, ids[3], ids[4]);
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into PERSONTAKESSECTION table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnPerson.add(mntmAddPersonSection);
		
		JMenuItem mntmAddWorkHistory = new JMenuItem("Add Work History");
		mntmAddWorkHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET Previous work details", "Enter details in the format \n <personid>,<jobCode>,\n<Startdate(dd-mm-yyyy)>,<EndDate(dd-mm-yyyy)>");
				if((id != null) && (id.length() > 0))
				{
					person ap = new person();
					String[] ids = id.split(",", 4);
					if(ids.length == 4){
						System.out.println(ids[0]);
						System.out.println(ids[1]);
						result = ap.addPastWork(Integer.parseInt(ids[0]), Integer.parseInt(ids[1]), ids[2], ids[3]);
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into PASTWORK table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		
		JMenuItem mntmAddCurrentWork = new JMenuItem("Add Current Work");
		mntmAddCurrentWork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET current work details", "Enter details in the format \n <personid>,<jobCode>");
				if((id != null) && (id.length() > 0))
				{
					person ap = new person();
					String[] ids = id.split(",");
					if(ids.length == 2){
						System.out.println(ids[0]);
						System.out.println(ids[1]);
						result = ap.addWorks(Integer.parseInt(ids[0]), Integer.parseInt(ids[1]));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into PASTWORK table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
			}
		});
		mnPerson.add(mntmAddCurrentWork);
		mnPerson.add(mntmAddWorkHistory);
		
		JMenu mnCompany = new JMenu("Company");
		mnOperations.add(mnCompany);
		
		JMenuItem mntmAddNewCompany = new JMenuItem("Add New Company ");
		mntmAddNewCompany.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET COMPANY details", "Enter details in the format \n companyId, companyName, companyAddress, companyStreet, \n"
						+ "companyCity, companyZipCode, companySector, companyWebsite");
				if((id != null) && (id.length() > 0))
				{
					company co = new company();
					String[] ids = id.split(",", 8);
					if(ids.length == 8){
						result = co.addCompanyDetails(Integer.parseInt(ids[0]),ids[1],ids[2],ids[3],ids[4],Integer.parseInt(ids[5]),ids[6],ids[7]);
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into COMPANY table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnCompany.add(mntmAddNewCompany);
		
		JMenuItem mntmAddNewProject = new JMenuItem("Add New Project of Company");
		mntmAddNewProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET COMPANY LAUNCHING PROJECT details", "Enter details in the format \n companyId, projectId");
				if((id != null) && (id.length() > 0))
				{
					company co = new company();
					String[] ids = id.split(",", 2);
					if(ids.length == 2){
						result = co.addCompanyLaunchesProject(Integer.parseInt(ids[0]),Integer.parseInt(ids[1]));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into COMPANYLAUNCHESPROJECT table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnCompany.add(mntmAddNewProject);
		
		JMenu mnJob = new JMenu("Job");
		mnOperations.add(mnJob);
		
		JMenuItem mntmAddNewJob = new JMenuItem("Add New Job");
		mntmAddNewJob.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET JOB details", "Enter details in the format \n jobCode, jobType, payRate, \n"
						+ "payType, companyId, jProfileCode");
				if((id != null) && (id.length() > 0))
				{
					job jo = new job();
					String[] ids = id.split(",", 6);
					if(ids.length == 6){
						result = jo.addJobDetails(Integer.parseInt(ids[0]),ids[1],Integer.parseInt(ids[2]),ids[3],Integer.parseInt(ids[4]),
								Integer.parseInt(ids[5]));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into JOB table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnJob.add(mntmAddNewJob);
		
		JMenuItem mntmAddJobIn = new JMenuItem("Add Job in Project");
		mntmAddJobIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET JOB in Project details", "Enter details in the format \n jobcode , projectID");
				if((id != null) && (id.length() > 0))
				{
					job jo = new job();
					String[] ids = id.split(",", 2);
					if(ids.length == 2){
						result = jo.addJobInProject(Integer.parseInt(ids[0]), Integer.parseInt(ids[1]));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into JOBINPROJECT table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnJob.add(mntmAddJobIn);
		
		JMenu mnJobprofile = new JMenu("Jobprofile");
		mnOperations.add(mnJobprofile);
		
		JMenuItem mntmAddNewJob_1 = new JMenuItem("Add New Job Profile");
		mntmAddNewJob_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET JOBPROFILE details", "Enter details in the format \n jProfileCode, jProfileTitle, jProfileDescription");
				if((id != null) && (id.length() > 0))
				{
					jobProfile jp = new jobProfile();
					String[] ids = id.split(",", 3);
					if(ids.length == 3){
						result = jp.addJobProfileDetails(Integer.parseInt(ids[0]),ids[1],ids[2]);
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into JOBPROFILE table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnJobprofile.add(mntmAddNewJob_1);
		
		JMenuItem mntmJobProfileRequires = new JMenuItem("Job Profile Requires Certificate");
		mntmJobProfileRequires.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET JOBPROFILE's required certificate details", "Enter details in the format \n jProfileCode, certCode");
				if((id != null) && (id.length() > 0))
				{
					jobProfile jp = new jobProfile();
					String[] ids = id.split(",", 2);
					if(ids.length == 2){
						result = jp.jProfileRequiresCertificate(Integer.parseInt(ids[0]),Integer.parseInt(ids[1]));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into JOBPROFILEREQUIRES CERTIFICATE table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnJobprofile.add(mntmJobProfileRequires);
		
		JMenuItem mntmJobProfileRequires_1 = new JMenuItem("Job Profile Requires Skill");
		mntmJobProfileRequires_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET JObprofile's required skill details", "Enter details in the format \n jprofileCode, ksCode>");
				if((id != null) && (id.length() > 0))
				{
					jobProfile jp = new jobProfile();
					String[] ids = id.split(",", 2);
					if(ids.length == 2){
						result = jp.jProfileRequiresSkill(Integer.parseInt(ids[0]),Integer.parseInt(ids[1]));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into JPROFILEREQUIRESSKILL table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnJobprofile.add(mntmJobProfileRequires_1);
		
		JMenu mnProject = new JMenu("Project");
		mnOperations.add(mnProject);
		
		JMenuItem mntmAddNewProject_1 = new JMenuItem("Add New Project");
		mntmAddNewProject_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET PROJECT details", "Enter details in the format \n projectId, projectTitle, projectDirector, \n"
						+ "budgetCode, projectStartDate(dd-mm-yyyy), projectEndDate(dd-mm-yyyy)");
				if((id != null) && (id.length() > 0))
				{
					project p1 = new project();
					String[] ids = id.split(",", 6);
					if(ids.length == 6){
						result = p1.AddProjectDetails(Integer.parseInt(ids[0]),ids[1],ids[2],ids[3],ids[4],ids[5]);
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into PROJECT table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnProject.add(mntmAddNewProject_1);
		
		JMenuItem mntmDeleteExistingProject = new JMenuItem("Delete Existing Project");
		mntmDeleteExistingProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET Project ID", "Enter projectID to be deleted");
				if((id != null) && (id.length() > 0))
				{
					project ap = new project();
					String[] ids = id.split(",", 1);
					if(ids.length == 1){
						result = ap.deleteProjectDetails(Integer.parseInt(id));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records deleted from PROJECT table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnProject.add(mntmDeleteExistingProject);
		
		JMenu mnCourse = new JMenu("Course");
		mnOperations.add(mnCourse);
		
		JMenuItem mntmAddNewCourse = new JMenuItem("Add New Course");
		mntmAddNewCourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET COURSE details", "Enter details in the format \n courseId, courseTitle, courseLevel, \n"
						+ "courseDescription, courseStatus, coursePrice");
				if((id != null) && (id.length() > 0))
				{
					course co = new course();
					String[] ids = id.split(",", 6);
					if(ids.length == 6){
						result = co.AddCourseDetails(Integer.parseInt(ids[0]),ids[1],ids[2],ids[3],ids[4],Double.parseDouble(ids[5]));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into COURSE table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnCourse.add(mntmAddNewCourse);
		
		JMenuItem mntmAddSkillTo = new JMenuItem("Add Skill to Course");
		mntmAddSkillTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET CoursecoversSkill details", "Enter details in the format \n <COURse ID>,<kscode>");
				if((id != null) && (id.length() > 0))
				{
					course co = new course();
					String[] ids = id.split(",", 2);
					if(ids.length == 2){
						result = co.AddSkillToCourse(Integer.parseInt(ids[0]), Integer.parseInt(ids[1]));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into COURSECOVERSSKILL table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnCourse.add(mntmAddSkillTo);
		
		JMenuItem mntmDeleteExistingCourse = new JMenuItem("Delete Existing Course");
		mntmDeleteExistingCourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET COURSEID", "Enter COURSEID to be deleted>");
				if((id != null) && (id.length() > 0))
				{
					course co = new course();
					String[] ids = id.split(",", 1);
					if(ids.length == 1){
						result = co.deleteCourseDetails(Integer.parseInt(ids[0]));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records deleted from COURSE table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnCourse.add(mntmDeleteExistingCourse);
		
		JMenu mnSection = new JMenu("Section");
		mnOperations.add(mnSection);
		
		JMenuItem mntmAddNewSection = new JMenuItem("Add New Section");
		mntmAddNewSection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET personhasskill details", "Enter details in the format \n sectionId, courseId, year, completeDate, \n"
						+ "offeredBy, format, sectionPrice");
				if((id != null) && (id.length() > 0))
				{
					section s1 = new section();
					String[] ids = id.split(",", 7);
					if(ids.length == 7){
						result = s1.addSectionDetails(Integer.parseInt(ids[0]),Integer.parseInt(ids[1]),ids[2],ids[3],ids[4],ids[5],Double.parseDouble(ids[6]));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into SECTION table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnSection.add(mntmAddNewSection);
		
		JMenu mnCertificate = new JMenu("Certificate");
		mnOperations.add(mnCertificate);
		
		JMenuItem mntmAddNewCertificate = new JMenuItem("Add New Certificate");
		mntmAddNewCertificate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET CERTIFICATE details", "Enter details in the format \n certCode, certTitle, certDescription, \n"
						+ "expDate, issuedBy, certToolCode");
				if((id != null) && (id.length() > 0))
				{
					certificate c1 = new certificate();
					String[] ids = id.split(",", 6);
					if(ids.length == 6){
						result = c1.AddCertificateDetails(Integer.parseInt(ids[0]),ids[1],ids[2],ids[3],ids[4],Integer.parseInt(ids[5]));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into CERTIFICATE table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnCertificate.add(mntmAddNewCertificate);
		
		JMenuItem mntmDeleteCertificate = new JMenuItem("Delete Certificate");
		mntmDeleteCertificate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET CERTIFICATE ID", "Enter Certificate code to be deleted");
				if((id != null) && (id.length() > 0))
				{
					certificate c1 = new certificate();
					String[] ids = id.split(",", 1);
					if(ids.length == 1){
						result = c1.deleteCertificateDetails(Integer.parseInt(ids[0]));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records Deleted from CERTIFICATE table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnCertificate.add(mntmDeleteCertificate);
		
		JMenu mnKnowledgeSkill = new JMenu("Knowledge Skill");
		mnOperations.add(mnKnowledgeSkill);
		
		JMenuItem mntmAddKnowledgeSkill = new JMenuItem("Add Knowledge Skill ");
		mntmAddKnowledgeSkill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET KNOWLEDGESKILL details", "Enter details in the format \n ksCode, ksTitle, ksDescription, ksLevel");
				if((id != null) && (id.length() > 0))
				{
					knowledgeSkill ks = new knowledgeSkill();
					String[] ids = id.split(",", 4);
					if(ids.length == 4){
						result = ks.addKnowledgeSkillDetails(Integer.parseInt(ids[0]),ids[1],ids[2],ids[3]);
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records inserted into KNOWLEDGESKILL table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnKnowledgeSkill.add(mntmAddKnowledgeSkill);
		
		JMenuItem mntmDeleteExistingKnowledge = new JMenuItem("Delete Existing Knowledge Skill");
		mntmDeleteExistingKnowledge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET KSCODE", "Enter KSCODE to be deleted");
				if((id != null) && (id.length() > 0))
				{
					knowledgeSkill ks =new knowledgeSkill();
					String[] ids = id.split(",", 1);
					if(ids.length == 1){
						result = ks.deleteKnowledgeSkillDetails(Integer.parseInt(ids[0]));
						if (result == true){
							queryDisplay.setText("Query Failed");
						}
						else{
							queryDisplay.setText("Records deleted from KnowledgeSkill table");
						}
					}
					else{
						queryDisplay.setText("Enter exact values do not enter more or less values");
						return;
					}
					
				}
				else{
					return;
				}
							
			}
		});
		mnKnowledgeSkill.add(mntmDeleteExistingKnowledge);
		
		JMenu mnMiscServices = new JMenu("Misc Services");
		menuBar.add(mnMiscServices);
		
		JMenu mnFindBiggestEmployer = new JMenu("Find Biggest Employer");
		mnMiscServices.add(mnFindBiggestEmployer);
		
		JMenuItem mntmInNumberOf = new JMenuItem("In number of Employees");
		mntmInNumberOf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Connection conn = DbConnection.getConnection(uName,pwd);
					TableInfo ti = new TableInfo(sqlconn);
					ResultSet rss = ti.FindBiggestEmployerInNumEmployee();
					String title = "Employer having more workers";
					TableView tv = new TableView(rss,sqlconn,title);
					tv.tableviewframe.setVisible(true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnFindBiggestEmployer.add(mntmInNumberOf);
		
		JMenuItem mntmInLaborCost = new JMenuItem("In Labor Cost");
		mntmInLaborCost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Connection conn = DbConnection.getConnection(uName,pwd);
					TableInfo ti = new TableInfo(sqlconn);
					ResultSet rss = ti.FindBiggestEmployerInLaborCost();
					String title = "Employer having Highest Laborcost";
					TableView tv = new TableView(rss,sqlconn,title);
					tv.tableviewframe.setVisible(true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnFindBiggestEmployer.add(mntmInLaborCost);
		
		JMenuItem mntmRatioOfEarning = new JMenuItem("Ratio of Earning Increase/Decrease");
		mntmRatioOfEarning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Connection conn = DbConnection.getConnection(uName,pwd);
					TableInfo ti = new TableInfo(sqlconn);
					ResultSet rss = ti.FindRatioEarningInCreaseAndDecrease();
					String title = "Earning increase / Decrease Ratio";
					TableView tv = new TableView(rss,sqlconn,title);
					tv.tableviewframe.setVisible(true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		JMenu mnFindBiggestSector = new JMenu("Find Biggest Sector");
		mnMiscServices.add(mnFindBiggestSector);
		
		JMenuItem mntmInEmployeeCount = new JMenuItem("In Employee Count");
		mntmInEmployeeCount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Connection conn = DbConnection.getConnection(uName,pwd);
					TableInfo ti = new TableInfo(sqlconn);
					ResultSet rss = ti.FindBiggestSectorInNumEmployee();
					String title = "Biggest Sector in employee Count";
					TableView tv = new TableView(rss,sqlconn,title);
					tv.tableviewframe.setVisible(true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnFindBiggestSector.add(mntmInEmployeeCount);
		
		JMenuItem mntmBiggestSectorIn = new JMenuItem("In labor Cost");
		mnFindBiggestSector.add(mntmBiggestSectorIn);
		mntmBiggestSectorIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Connection conn = DbConnection.getConnection(uName,pwd);
					TableInfo ti = new TableInfo(sqlconn);
					ResultSet rss = ti.FindBiggestSectorInLaborCost();
					String title = "Biggest Sector in Number of workers";
					TableView tv = new TableView(rss,sqlconn,title);
					tv.tableviewframe.setVisible(true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnMiscServices.add(mntmRatioOfEarning);
		
		JMenuItem mntmEarningImprovementRate = new JMenuItem("Earning Improvement Rate in Sector");
		mntmEarningImprovementRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = getDetails("GET SECTOR Name", "Enter Sector name \n to Find it's improvement rate");
				if((id != null) && (id.length() > 0))
				{
					try {
					//Connection conn = DbConnection.getConnection(uName,pwd);
					TableInfo ti = new TableInfo(sqlconn);
					ResultSet rss = ti.FindEarningImprovementRateInSector(id);
					String title = id + "'s Improvement rate";
					TableView tv = new TableView(rss,sqlconn,title);
					tv.tableviewframe.setVisible(true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				}
				else{return;}
			}
		});
		mnMiscServices.add(mntmEarningImprovementRate);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				queryDisplay.setText("Author: Hung Le,Saranyan Senthivel. 12-01-2015\n"
						+ "This a Model Career Guidance software, Services are all based on \n"
						+ "the queries which we wrote\n"
						+ "\nDomain of Tables \n"
						+ "+PersonID 	1000-1099\n"
						+ "CompanyID 	300-329\n"
						+ "JProfileCode 	200-239\n"
						+ "JobCode 	900-999\n"
						+ "ProjectID 	400-469\n"
						+ "KSCode 	500-559\n"
						+ "CourseID 	600-639\n"
						+ "SectionID 	101\n"
						+ "certCode 	800-849\n"
						);
			}
		});
		mnHelp.add(mntmAbout);
		
		JMenuItem mntmReadme = new JMenuItem("Readme");
		mntmReadme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				queryDisplay.setText("This will guide you through the software\n"
						+ "Below you can find the datatypes of the tables\n"
						+ "Person(PersonID 	INTEGER,PersonName 	VARCHAR,PersonAddress 	INTEGER,PersonStreet 	VARCHAR(50),PersonCity 	VARCHAR(50),PersonZipcode 	NUMERIC(5,0),Email		VARCHAR(30),Gender 		VARCHAR(20)\n"
						+ "Phone(PhoneNumber 	VARCHAR(20),PersonID 	INTEGER,PhoneType 	VARCHAR(15)\n"
						+ "Company(CompanyID 		INTEGER ,CompanyName 		VARCHAR(30),CompanyAddress 	INTEGER,CompanyStreet 		VARCHAR(50),CompanyCity 		VARCHAR(50),CompanyZipcode 	NUMERIC(5,0),CompanySector 	VARCHAR(50),CompanyWebsite 	VARCHAR(50)\n"
						+ "JobProfile(JProfileCode 		INTEGER,JProfileTitle 		VARCHAR(30),JProfileDescription 	VARCHAR(75)\n"
						+ "Job (JobCode 	INTEGER,JobType 	VARCHAR(20),PayRate 	NUMERIC(8,2),PayType 	VARCHAR(10),CompanyID 	INTEGER,JProfileCode 	INTEGER\n"
						+ "Project(ProjectID 	 INTEGER,ProjectTitle 	 VARCHAR(50),ProjectDirector  VARCHAR(30),BudgetCode 	 VARCHAR(10),ProjectStartDate DATE,ProjectEndDate  DATE\n"
						+ "KnowledgeSkill(KSCode 	INTEGER PRIMARY KEY,KSTitle 	VARCHAR(50),KSDescription 	VARCHAR(100),KSLevel 	VARCHAR(20)\n"
						+ "Course(CourseID 	INTEGER,CourseTitle 	VARCHAR(40),CourseLevel 	VARCHAR(20),CourseDescription VARCHAR(100),CourseStatus 	VARCHAR(10),CoursePrice 	NUMERIC(8,2)\n"
						+ "Section(SectionID 	INTEGER,CourseID 	INTEGER,Year	 	NUMERIC(4,0),CompleteDate 	DATE,OfferedBy 	VARCHAR(30),Format 		VARCHAR(20),SectionPrice 	NUMERIC(8,2)\n"
						+ "Certificate(CertCode 	INTEGER,CertTitle 	VARCHAR(50),CertDescription VARCHAR(100),ExpDate 	DATE,IssuedBy 	VARCHAR(50),CertToolCode 	INTEGER)\n"
						+ "Works(PersonID 	INTEGER,JobCode 	INTEGER\n"
						+ "PastWork(PersonID 	INTEGER,JobCode  	INTEGER,StartDate 	DATE,EndDate 	DATE,\n"
						+ "JobInProject(JobCode 	INTEGER,ProjectID 	INTEGER,)\n"
						+ "CompanyLaunchesProject(CompanyID 	INTEGER,ProjectID 	INTEGER,\n"
						+ "PersonHasSkill(PersonID  	INTEGER,KSCode 	INTEGER,\n"
						+ "JProfileRequiresSkill(JProfileCode 	INTEGER,KSCode 	INTEGER,\n"
						+ "PersonTakesSection(PersonID 	INTEGER,SectionID 	INTEGER,CourseID 	INTEGER,Year	 	NUMERIC(4,0),CompleteDate 	DATE,)\n"
						+ "CourseCoversSkill(CourseID INTEGER,KSCode INTEGER,\n"
						+ "PersonOwnsCertificate(PersonID INTEGER,CertCode INTEGER,)\n"
						+ "JProfileRequiresCertificate(JProfileCode INTEGER,CertCode INTEGER)");
			}
		});
		mnHelp.add(mntmReadme);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Services", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 39, 906, 611);
		frmCareerManagementService.getContentPane().add(panel);
		panel.setLayout(null);
		
		JList jssList = new JList();
		jssList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				jssEnable(true);
			}
		});
		jssList.setBounds(12, 24, 284, 310);
		panel.add(jssList);
		jssList.setBorder(new TitledBorder(null, "Job Seeker Services", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		jssList.setVisibleRowCount(15);
		jssList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jssList.setModel(new AbstractListModel() {
			String[] values = new String[] {"List Jobs of Person", "List Person's Knowledge Skill", "List Skill Gap From Job", "List Required Skills For Job"
					, "List Missing Skills For Job", "List Job Profiles Person Qualify","List Job Profile with highest demand","Find Job Highest Pay for Person's Qualification"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		
		jssPerform = new JButton("Perform Service");
		jssPerform.setBounds(28, 347, 183, 25);
		panel.add(jssPerform);
		jssPerform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jssList.isSelectedIndex(0)){
					String id = getDetails("GET Person ID", "Enter PersonID whose job to be listed");
					if((id != null) && (id.length() > 0))
					{
						try {
						int input = Integer.parseInt(id);
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.ListJobsOfPerson(input);
						String title = input + "'s Jobs";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else{
						return;
					}
				}
				if (jssList.isSelectedIndex(1)){
					String id = getDetails("GET Person ID", "Enter PersonID whose skills to be listed");
					if((id != null) && (id.length() > 0))
					{try {
						int input = Integer.parseInt(id);
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.ListPersonKnowledgeSkill(input);
						String title = input + "'s Skills";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else
					{
						return;
					}
				}
				if (jssList.isSelectedIndex(2)){
					String id = getDetails("GET Person ID", "Enter PersonID skill gap to be found");
					if((id != null) && (id.length() > 0))
						{
						try {
							int input =Integer.parseInt(id);
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.ListSkillGapFromJobs(input);
						String title = input + "'s skillgap";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
						}
					else
					{
						return;
					}
				}
				if (jssList.isSelectedIndex(3)){
					String id = getDetails("GET JOBPROFILECODE", "Enter JobProfileCode \n to list the skills required");
					if((id != null) && (id.length() > 0))
					{
						try {
							int input = Integer.parseInt(id);
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.ListRequiredSkillsOfJob(input);
						String title = input + "'s required skills";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else{return;}
				}
				if (jssList.isSelectedIndex(4)){
					String id = getDetails("ENTER JOBCODE,PERSONID", "Enter JobCode and Person ID \nin the form: <JobCode>,<PersonID>");
					if((id != null) && (id.length() > 0))
					{
						try {
						String[] ids = id.split(",");	
						int JobCode = Integer.parseInt(ids[0]);
						int PerID = Integer.parseInt(ids[1]);
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.ListMissingSkillsForJob(JobCode,PerID);
						String title = "missing skills of "+PerID+" to "+JobCode;
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else{return;}
				}
				if (jssList.isSelectedIndex(5)){
					String id = getDetails("GET PERSONID", "Enter PersonID \n to list the jobs a person is qualified");
					if((id != null) && (id.length() > 0))
					{
						try {
							int input = Integer.parseInt(id);
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.ListJobProfilesPersonQualify(input);
						String title = input + "is qualified for";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else{return;}
				}
				if (jssList.isSelectedIndex(6)){
					try {
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.FindJobProfileWithHighestDemand();
						TableView tv = new TableView(rss,sqlconn,"Job Profile With Highest Demand");
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				if (jssList.isSelectedIndex(7)){
					String id = getDetails("ENTER PERSONID", 
							"Enter PERSONID \n to get the highest pay");
					if((id != null) && (id.length() > 0))
					{
						try {
							int input = Integer.parseInt(id);
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.FindJobHighestPayForPerson(input);
						String title = input + "'s Highest pay";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else{return;}
				}
				disableButtons();
			}
		});
		
		JButton btnCloseApplication = new JButton("Close Application");
		btnCloseApplication.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnCloseApplication.setBounds(749, 549, 145, 49);
		panel.add(btnCloseApplication);
		
		JList jpsList = new JList();
		jpsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				jpsEnable();
			}
		});
		jpsList.setVisibleRowCount(15);
		jpsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jpsList.setBorder(new TitledBorder(null, "Job Provider Services", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		jpsList.setBounds(305, 26, 283, 302);
		panel.add(jpsList);
		jpsList.setModel(new AbstractListModel() {
			String[] values = new String[] {"List Company Workers", "List company Workers with salary", "List Company Labor Cost(descending Order)"
					, "List Workers in Project", "List people qualified for Job Profile", "List People Missing One Skill List for JProfile"
					, "List People Held Jobs of Jobs of JProfile", "List people missing least skill for JProfile", "Missing K list for JProfile"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		jpsPerform = new JButton("Perform Service");
		jpsPerform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jpsList.isSelectedIndex(0)){
					String id = getDetails("ENTER COMPANYID", "Enter CompanyID \n to list the workers");
					if((id != null) && (id.length() > 0))
					{
						try {
							int input = Integer.parseInt(id);
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.ListCompanyWorkers(input);
						String title = input + "'s Workers";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else{return;}
				}
				if (jpsList.isSelectedIndex(1)){
					String id = getDetails("ENTER COMPANYID", 
							"Enter CompanyID \n to list the workers along with salary");
					if((id != null) && (id.length() > 0))
					{
						try {
							int input = Integer.parseInt(id);
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.ListCompanyWorkersBySalaryDesc(input);
						String title = input + "'s Workers with salary";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else{return;}
				}
				if (jpsList.isSelectedIndex(2)){
					
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti;
						try {
							ti = new TableInfo(sqlconn);
							ResultSet rss = ti.ListCompaniesLaborCostDesc();
							TableView tv = new TableView(rss,sqlconn,"Labor cost of companies");
							tv.tableviewframe.setVisible(true);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					
				}
				if (jpsList.isSelectedIndex(3)){
					String id = getDetails("ENTER PROJECTID", 
							"Enter ProjectID \n to list the workers");
					if((id != null) && (id.length() > 0))
					{
						try {
							int input = Integer.parseInt(id);
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.ListWorkersInProject(input);
						String title = input + "'s Workers";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else{return;}
				}
				if (jpsList.isSelectedIndex(4)){
					String id = getDetails("ENTER JOBPROFILECODE", 
							"Enter JobProfile code\n to find qualified persons");
					if((id != null) && (id.length() > 0))
					{
						try {
							int input = Integer.parseInt(id);
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.ListPeopleQualifiedForJProfile(input);
						String title = input + "'s Qualified persons";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else{return;}
				}
				
				if (jpsList.isSelectedIndex(5)){
					String id = getDetails("ENTER JOBPROFILECODE", 
							"Enter JobProfile code\n to find skill with people count \n missing only one skill");
					if((id != null) && (id.length() > 0))
					{
						try {
							int input = Integer.parseInt(id);
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.ListSkillWithPeopleCountInMissingOneList(input);
						String title = input + "'s missing one list";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else{return;}
				}
				if (jpsList.isSelectedIndex(6)){
					String id = getDetails("ENTER JOBPROFILECODE", 
							"Enter JobProfile code\n to find people held job of this jobprofile");
					if((id != null) && (id.length() > 0))
					{
						try {
							int input = Integer.parseInt(id);
						Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.ListPeopleHeldJobsOfJProfile(input);
						String title = input + "'s Past workers";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else{return;}
				}
				if (jpsList.isSelectedIndex(7)){
					String id = getDetails("GET JOBPROFILECODE", "Enter JPROFILECODE To find "
							+ "\nthe least missing skill");
					if((id != null) && (id.length() > 0))
					{try {
						int input = Integer.parseInt(id);
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.ListPeopleMissLeastSkillForJProfile(input);
						String title = "Missing least skill list of " + id;
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else
					{
						return;
					}
			}
			if (jpsList.isSelectedIndex(8)){
				String id = getDetails("Enter JOBPROFILECODE and K",
						"ENTER input in the format <jprofilecode>,\n<k> integer like 1,2,3... \n to find Missing K list ");
				if((id != null) && (id.length() > 0))
				{
					String[] ids=id.split(",",2);
					if(ids.length == 2){
						
						try 
						{
							//Connection conn = DbConnection.getConnection(uName,pwd);
							TableInfo ti = new TableInfo(sqlconn);
							ResultSet rss = ti.MissingKListForJProfile(Integer.parseInt(ids[0]), Integer.parseInt(ids[1]));
							String title = "Missing " + ids[1] + " of jobprofile" + ids[0];
							TableView tv = new TableView(rss,sqlconn,title);
							tv.tableviewframe.setVisible(true);
						} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						}
					}
					else{queryDisplay.setText("Enter all inputs");}
				}
				else{return;}
				
			}
				disableButtons();
			}
		});
		jpsPerform.setBounds(334, 347, 183, 25);
		panel.add(jpsPerform);
		
		JList tpsList = new JList();
		tpsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tpsEnable();
			}
		});
		tpsList.setVisibleRowCount(15);
		tpsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tpsList.setBorder(new TitledBorder(null, "Training Provider Service", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		tpsList.setBounds(611, 26, 283, 302);
		panel.add(tpsList);
		tpsList.setModel(new AbstractListModel() {
			String[] values = new String[] {"Find Course covers skill", "Find Course teach Person For job", "Find Quickest Course for Person", "Find Courseset Covers skill set", "Find Courseset teach person for job", "Find cheapest courset person for job", "List skill with people count in Missing one list", "List Skill with people count in missing K List", "Find Course Help Most people"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		tpsPerform = new JButton("Perform Service");
		tpsPerform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tpsList.isSelectedIndex(0)){
					String id = getDetails("Enter Skillsets", 
							"ENTER Skill set in the format <skillset1>,<skillset2>,...");
					if((id != null) && (id.length() > 0))
					{
						String[] ids = id.split(",");
						//System.out.println(ids[0]+ids[1]);
						try {
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.FindCourseCoverSkillSet(ids);
						String title = "Skillsets " + id + " are covered by";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else{return;}
				}
				if (tpsList.isSelectedIndex(1)){
					String id = getDetails("Enter JOBCODE and PERSONID",
							"ENTER input in the format <jobcode>,<personID> \n to find relevant Course");
					if((id != null) && (id.length() > 0))
					{
						String[] ids=id.split(",",2);
						if(ids.length == 2){
							
							try 
							{
								//Connection conn = DbConnection.getConnection(uName,pwd);
								TableInfo ti = new TableInfo(sqlconn);
								ResultSet rss = ti.FindCoursesTeachPersonForJob(Integer.parseInt(ids[0]), Integer.parseInt(ids[1]));
								String title = "relevant course for " + ids[1] + " for job "+ids[0];
								TableView tv = new TableView(rss,sqlconn,title);
								tv.tableviewframe.setVisible(true);
							} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							}
						}
						else{queryDisplay.setText("Enter all inputs");}
					}
					else{return;}
				}
				if (tpsList.isSelectedIndex(2)){
					String id = getDetails("Enter JOBCODE and PERSONID",
							"ENTER input in the format <jobcode>,<personID> \n to find Quickest Course");
					if((id != null) && (id.length() > 0))
					{
						String[] ids=id.split(",",2);
						if(ids.length == 2){
							
							try 
							{
								//Connection conn = DbConnection.getConnection(uName,pwd);
								TableInfo ti = new TableInfo(sqlconn);
								ResultSet rss = ti.FindQuickestCourseForPerson(Integer.parseInt(ids[0]),Integer.parseInt(ids[1]));
								String title ="Quickest course for " + ids[1] + " for job " + ids[0];
								TableView tv = new TableView(rss,sqlconn,title);
								tv.tableviewframe.setVisible(true);
							} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							}
						}
						else{queryDisplay.setText("Enter all inputs");}
					}
					else{return;}
				}
				if (tpsList.isSelectedIndex(3)){
					String id = getDetails("Enter SKILLSETS", 
							"ENTER Skill set in the format <skillset1>,<skillset2>,...");
					if((id != null) && (id.length() > 0))
					{
						String[] ids = id.split(",");
						//System.out.println(ids[0]+ids[1]);
						try {
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.FindCourseSetsCoverSkillSet(ids);
						String title = "Skillsets " + id + " are covered by";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else{return;}
				}
				if (tpsList.isSelectedIndex(4)){
					String id = getDetails("Enter JOBCODE and PERSONID",
							"ENTER input in the format <jobcode>,<personID> \n to find relevant Course set");
					if((id != null) && (id.length() > 0))
					{
						String[] ids=id.split(",",2);
						if(ids.length == 2){
							
							try 
							{
								//Connection conn = DbConnection.getConnection(uName,pwd);
								TableInfo ti = new TableInfo(sqlconn);
								ResultSet rss = ti.FindCourseSetsTeachPersonForJob(Integer.parseInt(ids[0]), Integer.parseInt(ids[1]));
								String title = "Relevant courset for "+ids[1]+" for "+ids[0];
								TableView tv = new TableView(rss,sqlconn,title);
								tv.tableviewframe.setVisible(true);
							} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							}
						}
						else{queryDisplay.setText("Enter all inputs");}
					}
					else{return;}
				}
				if (tpsList.isSelectedIndex(5)){
					String id = getDetails("Enter JOBCODE and PERSONID",
							"ENTER input in the format <jobcode>,<personID> \n to find cheapest and "
							+ "\n relevant Course set");
					if((id != null) && (id.length() > 0))
					{
						String[] ids=id.split(",",2);
						if(ids.length == 2){
							
							try 
							{
								//Connection conn = DbConnection.getConnection(uName,pwd);
								TableInfo ti = new TableInfo(sqlconn);
								ResultSet rss = ti.FindCheapestCourseSetsPersonForJob(Integer.parseInt(ids[0]), Integer.parseInt(ids[1]));
								String title = "Cheapest & Relevant courset for "+ids[1]+" for "+ids[0];
								TableView tv = new TableView(rss,sqlconn,title);
								tv.tableviewframe.setVisible(true);
							} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							}
						}
						else{queryDisplay.setText("Enter all inputs");}
					}
					else{return;}
				}
				if (tpsList.isSelectedIndex(6)){
					String id = getDetails("ENTER JOBPROFILECODE", 
							"Enter JobProfile code\n to list skills in Missing One List with people count");
					if((id != null) && (id.length() > 0))
					{
						try {
							int input = Integer.parseInt(id);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.ListSkillWithPeopleCountInMissingOneList(input);
						String title = input + "'s Missing skills with People Count";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					else{return;}
				}
				if (tpsList.isSelectedIndex(7)){
					String id = getDetails("Enter JOBPROFILECODE and K",
							"ENTER input in the format <jprofilecode>,\n<k> integer like 1,2,3... \n to find Missing K-list with people count ");
					if((id != null) && (id.length() > 0))
					{
						String[] ids=id.split(",",2);
						if(ids.length == 2){
							
							try 
							{
								//Connection conn = DbConnection.getConnection(uName,pwd);
								TableInfo ti = new TableInfo(sqlconn);
								ResultSet rss = ti.ListSkillWithPeopleCountInMissingKList(Integer.parseInt(ids[0]), Integer.parseInt(ids[1]));
								String title = "Missing " + ids[1] + " of jobprofile" + ids[0] + " With people count";
								TableView tv = new TableView(rss,sqlconn,title);
								tv.tableviewframe.setVisible(true);
							} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							}
						}
						else{queryDisplay.setText("Enter all inputs");}
					}
					else{return;}
					
				}
				if(tpsList.isSelectedIndex(8)){
					try {
						//Connection conn = DbConnection.getConnection(uName,pwd);
						TableInfo ti = new TableInfo(sqlconn);
						ResultSet rss = ti.FindCourseHelpMostPeople();
						String title = "Most helpful course in current Scenario";
						TableView tv = new TableView(rss,sqlconn,title);
						tv.tableviewframe.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				disableButtons();
			}
		});
		tpsPerform.setBounds(650, 347, 183, 25);
		panel.add(tpsPerform);
		
		queryDisplay = new TextArea();
		queryDisplay.setBounds(12, 400, 862, 113);
		panel.add(queryDisplay);
		queryDisplay.setEditable(false);
		
		
	}
	
	private String getDetails(String Title, String Message) {
        String s = (String)JOptionPane.showInputDialog(
        	frmCareerManagementService,
            Message,
            Title,
            JOptionPane.QUESTION_MESSAGE);
        return s;
    }
	
	public void disableButtons(){
		jpsPerform.setEnabled(false);
		jssPerform.setEnabled(false);
		tpsPerform.setEnabled(false);
	}
	public void jpsEnable(){
		jpsPerform.setEnabled(true);
		jssPerform.setEnabled(false);
		tpsPerform.setEnabled(false);
	}
	public void jssEnable(Boolean choice){
		jpsPerform.setEnabled(false);
		jssPerform.setEnabled(true);
		tpsPerform.setEnabled(false);
	}
	public void tpsEnable(){
		jpsPerform.setEnabled(false);
		jssPerform.setEnabled(false);
		tpsPerform.setEnabled(true);
	}
}
