package guiCareerManage;

import java.awt.EventQueue;

import javax.swing.text.DefaultCaret;

import dbConnection.DbConnection;
import dbConnection.TableInfo;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class QueryDriver extends JFrame {
	JFrame qrFrame;
	private JPanel contentPane;
	private JTextField userInputTextField;
	private String[] parameters;
	static String Username;
	static String Password;
	static CareerServices cs;
	static Connection conn;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QueryDriver frame = new QueryDriver(conn);
					Username = (String)JOptionPane.showInputDialog(
				        	frame.qrFrame,
				            "Enter Username",
				            "GET USERNAME",
				            JOptionPane.QUESTION_MESSAGE);
					if (Username == null || Username.isEmpty()) {
			            return;
			     }
				else{
					Password = (String)JOptionPane.showInputDialog(
				        	frame.qrFrame,
				            "Enter Password",
				            "GET PASSWORD",
				            JOptionPane.QUESTION_MESSAGE);
					if (Password == null || Password.isEmpty()) {
			            return;
			     }
				}
					conn = DbConnection.getConnection(Username, Password);
					if(conn != null){
						frame.qrFrame.setVisible(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public QueryDriver(Connection conn) throws SQLException {
		
		
	        
		this.setTitle("Query Processing GUI");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(350, 150, 699, 232);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JTextArea queryDescription = new JTextArea();
		queryDescription.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		queryDescription.setBackground(Color.WHITE);
		queryDescription.setWrapStyleWord(true);
		queryDescription.setText("<<< Please select a Query");
		queryDescription.setLineWrap(true);
		queryDescription.setRows(4);
		queryDescription.setEditable(false);
		queryDescription.setBounds(165, 43, 508, 122);
		contentPane.add(queryDescription);
		
		
		userInputTextField = new JTextField();
		
		userInputTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				userInputTextField.setText("");
			}
		});
		userInputTextField.setText("input here...");
		userInputTextField.setBounds(41, 93, 96, 23);
		contentPane.add(userInputTextField);
		userInputTextField.setColumns(10);
		//caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		
		
		JComboBox querySelectBox = new JComboBox();
		querySelectBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				String selectedQuery = (String) ((JComboBox)e.getSource()).getSelectedItem();

				switch ( selectedQuery)
				{
					case "Query 1":
						queryDescription.setText("List a company’s workers by names."
								+ "\nINPUT: <Company ID>."
								+ "\n( CompanyID ranges from 300 to 340 ) ");
						break;
					case "Query 2":
						queryDescription.setText("List a company’s staff by salary in descending order."
								+ "\nINPUT: <Company ID>."
								+ "\n( CompanyID ranges from 300 to 340 ) ");
						break;
					case "Query 3":
						queryDescription.setText("List companies’ labor cost (total salaries "
								+ "and wage rates by 1920 hours)\n in descending order."
								+ "\nINPUT: NONE");
						break;
					case "Query 4":
						queryDescription.setText("Find all the Jobs a person is currently holding."
								+ "\nINPUT: <Person ID> the Person ID to be queried"
								+ "\n( PersonID ranges from 1000 to 1099");
						break;
					case "Query 5":
						queryDescription.setText("List all the workers who are working for a specific project."
								+ "\nINPUT: <Project ID>"
								+ "\nThe ID of the Project to be queried: 400-469");
						break;
					case "Query 6":
						queryDescription.setText("List a person’s knowledge/skills in a readable format."
								+ "\nINPUT: <Person ID>"
								+ "\nThe ID of the Person to be queried: 1000-1099");
						break;
					case "Query 7":
						queryDescription.setText("List the skill gap of a worker between his/her Job(s) and his/her skills."
								+ "\nINPUT: <Person ID>"
								+ "\n1000-1099");
						break;
					case "Query 8":
						queryDescription.setText("List the required knowledge/skills of a Job profile in a readable format."
								+ "\nINPUT: <Job Profile Code>"
								+ "200-240 ");
						break;
					case "Query 9":
						queryDescription.setText("List a person’s missing knowledge/skills for a specific Job in a readable format."
								+ "\nINPUT: <Job Code>,<Person ID>"
								+ "          900-999  , 1000-1099");
						break;
					case "Query 10":
						queryDescription.setText("Find the courses each of which alone can cover a given skill set"
								+ "\nINPUT: <KSCode>,<KSCode>,..."
								+ "\ncomma-separated list of the skills needed: 500-599");
						break;
					case "Query 11":
						queryDescription.setText("List the courses (course id and title) that each alone teaches all the missing knowledge/skills for a person to pursue a specific Job."
								+ "\nINPUT: <Job Code>,<Person ID>"
								+ "          900-999  , 1000-1099");
						break;
					case "Query 12":
						queryDescription.setText("Suppose the skill gap of a worker and the requirement of a desired Job can be covered by one course. Find the “quickest” solution for this worker. "
								+ "Show the course and the completing date."
								+ "\nINPUT: <Job Code>,<Person ID>"
								+ "          900-999  , 1000-1099");
						break;
					case "Query 13":
						queryDescription.setText("Find the course sets with the minimum number of courses that their combination "
								+ "covers a given skill set. "
								+ "\nINPUT: <KSCode>,<KSCode>,<KSCode>,..."
								+ "\na comma-separated list of needed skills: 500 to 599");
						break;
					case "Query 14":
						queryDescription.setText("List the course sets that their combinations cover all the missing knowledge/skills for a person to pursue a specific Job. "
								+ "\nINPUT: <Job Code>,<Person ID>"
								+ "          900-999  , 1000-1099");
						break;
					case "Query 15":
						queryDescription.setText("Find the cheapest course choices to make up one’s skill gap by showing the courses to take and the total cost."
								+ "\nINPUT: <Job Code>,<Person ID>"
								+ "          900-999  , 1000-1099");
						break;
					case "Query 16":
						queryDescription.setText("List all the Job profiles that a person is qualified."
								+ "\nINPUT: <PersonID> "
								+ "\n1000 - 1099"); 
						break;
					case "Query 17":
						queryDescription.setText("Find the Job with the highest pay rate for a person according to his/her skill qualification."
								+ "\nINPUT: <Person ID> : 1000 - 1099");
						break;
					case "Query 18":
						queryDescription.setText("List all the names along with the emails of the people who are qualified for a Job profile."
								+ "\nINPUT: <JobProfile Code> 200-240 ");
						break;
					case "Query 19":
						queryDescription.setText("Make a “missing-one” list that lists people who miss only one skill for a specified Job profile. "
								+ "\nINPUT: <JobProfile Code> 200-240 ");
						break;
					case "Query 20":
						queryDescription.setText("List the skillID and the number of people in the missing-one list for a given Job profile in the ascending order of the people counts."
								+ "\nINPUT: <JobProfile Code> 200-240 ");
						break;
					case "Query 21":
						queryDescription.setText("Suppose there is a new Job profile that has nobody qualified.  List the people who miss the least number of skills and report the “least number”."
								+ "\nINPUT: <JobProfile Code> 200-240 ");
						break;
					case "Query 22":
						queryDescription.setText("For a specified Job profile and a given small number k, make a “missing-k” list that lists the people’s IDs and the number of missing skills for the people who miss only up to k skills in the ascending order of missing skills."
								+ "\nINPUT: <JobProfile Code> <k number> : { 200-240} , {1,2,3...}");
						break;
					case "Query 23":
						queryDescription.setText("Given a Job profile and its corresponding missing-k list specified in Question 22."
								+ "  Find every skill that is needed by at least one person in the given missing-k list."
								+ "  List each skillID and the number of people who need it in thedescending order of the people counts."
								+ "\nINPUT: <JobProfile Code> <k number> : { 200-240} , {1,2,3...}");
						break;
					case "Query 24":
						queryDescription.setText("In a local or national crisis, we need to find all the people who once held a Job of the special Job-profile identifier."
								+ "\nINPUT: <JobProfile Code> 200-240 ");
						break;
					case "Query 25":
						queryDescription.setText("Find all the unemployed people who once held a Job of the given Job-profile identifier."
								+ "\nINPUT: <JobProfile Code> 200-240 ");
						break;
					case "Query 26a":
						queryDescription.setText("Find out the biggest employer in terms of number of employees"
								+ "\nINPUT: NONE");
						break;
					case "Query 26b":
						queryDescription.setText("Find out the biggest employer in terms of the total amount of salaries and wages paid to employees."
								+ "\nINPUT: NONE");
						break;
					case "Query 27a":
						queryDescription.setText("Find out the biggest sector in terms of number of employees"
								+ "\nINPUT: NONE");
						break;
					case "Query 27b":
						queryDescription.setText("Find out the biggest sector in terms of the total amount of salaries and wages paid to employees."
								+ "\nINPUT: NONE");
						break;
					case "Query 28a":
						queryDescription.setText("Find out the ratio between the people whose earnings increase and those whose earning decrease"
								+ "\nINPUT: NONE");
						break;
					case "Query 28b":
						queryDescription.setText("Find the average rate of earning improvement for the workers in a specific business sector."
								+ "\nINPUT: <sector>: the sector to be queried: "
								+ "\n{Food Industry,Real Estate,Entertainment,"
								+ "Technology,Tourism,E-commerce,Hospitality,Electronics}");
						break;
					case "Query 29":
						queryDescription.setText("Find the job profiles that have the most openings due to lack of qualified workers."
								+ "\nINPUT: NONE");
						break;
					case "Query 30":
						queryDescription.setText("Find the courses that can help most jobless people find a job "
								+ "by training them toward the job profiles that have themost openings due to lack of qualified workers. "
								+ "\nINPUT: NONE");
						break;
				}
			}
		});
		
		
		JButton runQueryButton = new JButton("Run Query");
		runQueryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( querySelectBox.getSelectedIndex() != -1)
				{
					
					parameters = userInputTextField.getText().split(",");
							
					String selectedQuery = (String)querySelectBox.getSelectedItem();
					/*if(conn == null){
					conn = DbConnection.getConnection(cs.getuName(),cs.getPwd());
					}*/
					TableInfo ti = null;
					ResultSet rss = null;
					try {
						 ti = new TableInfo(cs.getSqlconn());
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					switch ( selectedQuery)
					{
						case "Query 1":
							// pass in compID
							try 
							{
								rss = ti.ListCompanyWorkers(Integer.parseInt(parameters[0]));
								TableView tv = new TableView(rss,conn,"ListCompanyWorkers");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 2":
							// pass in compID
							try 
							{
								rss = ti.ListCompanyWorkersBySalaryDesc(Integer.parseInt(parameters[0]));
								TableView tv = new TableView(rss,conn,"ListCompanyWorkersBySalaryDesc");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 3":
							// no parameters needed
							try 
							{
								rss = ti.ListCompaniesLaborCostDesc();
								TableView tv = new TableView(rss,conn,"ListCompaniesLaborCostDesc");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 4":
							// pass in PersonID
							try 
							{
								rss = ti.ListJobsOfPerson(Integer.parseInt(parameters[0]));
								TableView tv = new TableView(rss,conn,"ListJobsOfPerson");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 5":
							// pass in ProjectID
							try 
							{
								rss = ti.ListWorkersInProject(Integer.parseInt(parameters[0]));
								TableView tv = new TableView(rss,conn,"ListWorkersInProject");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 6":
							// pass in PersonID
							try 
							{
								rss = ti.ListPersonKnowledgeSkill( Integer.parseInt(parameters[0]) );
								TableView tv = new TableView(rss,conn,"ListPersonKnowledgeSkill");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 7":
							// pass in PersonID
							try 
							{
								rss = ti.ListSkillGapFromJobs( Integer.parseInt(parameters[0]));
								TableView tv = new TableView(rss,conn,"ListSkillGapFromJobs");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 8":
							// pass in JProfileCode
							try 
							{
								rss = ti.ListRequiredSkillsOfJob( Integer.parseInt(parameters[0]));
								TableView tv = new TableView(rss,conn,"ListRequiredSkillsOfJob");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 9":
							// pass in Jobcode, Person ID
							try 
							{
								rss = ti.ListMissingSkillsForJob( Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]));
								TableView tv = new TableView(rss,conn,"ListMissingSkillsForJob");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 10":
							// pass in array of given skill set
							try 
							{
								rss = ti.FindCourseCoverSkillSet( parameters);
								TableView tv = new TableView(rss,conn,"FindCourseCoverSkillSet");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 11":
							// pass in Jobcode, Person ID
							try 
							{
								rss = ti.FindCoursesTeachPersonForJob( Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]));
								TableView tv = new TableView(rss,conn,"FindCoursesTeachPersonForJob");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 12":
							// pass in Jobcode, Person ID
							try 
							{
								rss = ti.FindQuickestCourseForPerson( Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]));
								TableView tv = new TableView(rss,conn,"FindQuickestCourseForPerson");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 13":
							// pass in array of given skill set
							try 
							{
								rss = ti.FindCourseSetsCoverSkillSet( parameters);
								TableView tv = new TableView(rss,conn,"FindCourseSetsCoverSkillSet");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 14":
							// pass in Jobcode, Person ID
							try 
							{
								rss = ti.FindCourseSetsTeachPersonForJob( Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]));
								TableView tv = new TableView(rss,conn,"FindCourseSetsTeachPersonForJob");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 15":
							// pass in Jobcode, Person ID
							try 
							{
								rss = ti.FindCheapestCourseSetsPersonForJob( Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]));
								TableView tv = new TableView(rss,conn,"FindCheapestCourseSetsPersonForJob");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 16":
							// pass in Person ID
							try 
							{
								rss = ti.ListJobProfilesPersonQualify( Integer.parseInt(parameters[0]));
								TableView tv = new TableView(rss,conn,"ListJobProfilesPersonQualify");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 17":
							// pass in Person ID
							try 
							{
								rss = ti.FindJobHighestPayForPerson( Integer.parseInt(parameters[0]));
								TableView tv = new TableView(rss,conn,"FindJobHighestPayForPerson");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 18":
							// pass in JProfileCode
							try 
							{
								rss = ti.ListPeopleQualifiedForJProfile( Integer.parseInt(parameters[0]));
								TableView tv = new TableView(rss,conn,"ListPeopleQualifiedForJProfile");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 19":
							// pass in JProfileCode
							try 
							{
								rss = ti.ListPeopleMissingOneSkillForJProfile( Integer.parseInt(parameters[0]));
								TableView tv = new TableView(rss,conn,"ListPeopleMissingOneSkillForJProfile");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 20":
							// pass in JProfileCode
							try 
							{
								rss = ti.ListSkillWithPeopleCountInMissingOneList( Integer.parseInt(parameters[0]));
								TableView tv = new TableView(rss,conn,"ListSkillWithPeopleCountInMissingOneList");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 21":
							// pass in JProfileCode
							try 
							{
								rss = ti.ListPeopleMissLeastSkillForJProfile( Integer.parseInt(parameters[0]));
								TableView tv = new TableView(rss,conn,"ListPeopleMissLeastSkillForJProfile");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 22":
							// pass in JProfileCode, K number
							try 
							{
								rss = ti.MissingKListForJProfile( Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]));
								TableView tv = new TableView(rss,conn,"MissingKListForJProfile");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 23":
							// pass in JProfileCode, K number
							try 
							{
								rss = ti.ListSkillWithPeopleCountInMissingKList( Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]));
								TableView tv = new TableView(rss,conn,"ListSkillWithPeopleCountInMissingKList");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 24":
							// pass in JProfileCode
							try 
							{
								rss = ti.ListPeopleHeldJobsOfJProfile( Integer.parseInt(parameters[0]));
								TableView tv = new TableView(rss,conn,"ListPeopleHeldJobsOfJProfile");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 25":
							// pass in JProfileCode
							try 
							{
								rss = ti.ListUnemployedPeopleHeldJobsOfJProfile( Integer.parseInt(parameters[0]));
								TableView tv = new TableView(rss,conn,"ListUnemployedPeopleHeldJobsOfJProfile");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 26a":
							// no parameters required
							try 
							{
								rss = ti.FindBiggestEmployerInNumEmployee();
								TableView tv = new TableView(rss,conn,"FindBiggestEmployerInNumEmployee");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 26b":
							// no parameters required
							try 
							{
								rss = ti.FindBiggestEmployerInLaborCost();
								TableView tv = new TableView(rss,conn,"FindBiggestEmployerInLaborCost");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 27a":
							// no parameters required
							try 
							{
								rss = ti.FindBiggestSectorInNumEmployee();
								TableView tv = new TableView(rss,conn,"FindBiggestSectorInNumEmployee");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 27b":
							// no parameters required
							try 
							{
								rss = ti.FindBiggestSectorInLaborCost();
								TableView tv = new TableView(rss,conn,"FindBiggestSectorInLaborCost");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 28a":
							// no parameters required
							try 
							{
								rss = ti.FindRatioEarningInCreaseAndDecrease();
								TableView tv = new TableView(rss,conn,"FindRatioEarningInCreaseAndDecrease");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 28b":
							// pass in the sector
							try 
							{
								rss = ti.FindEarningImprovementRateInSector(parameters[0]);
								TableView tv = new TableView(rss,conn,"FindEarningImprovementRateInSector");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 29":
							// no parameters required
							try 
							{
								rss = ti.FindJobProfileWithHighestDemand();
								TableView tv = new TableView(rss,conn,"FindJobProfileWithHighestDemand");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
						case "Query 30":
							// no parameters required
							try 
							{
								rss = ti.FindCourseHelpMostPeople();
								TableView tv = new TableView(rss,conn,"FindCourseHelpMostPeople");
								tv.tableviewframe.setVisible(true);
							} 
							catch (SQLException ex) { ex.printStackTrace(); }
							break;
					}
				}
			}
		});
		
		
		runQueryButton.setBounds(41, 142, 96, 23);
		contentPane.add(runQueryButton);
		querySelectBox.setModel(new DefaultComboBoxModel(new String[] {"Query 1", "Query 2", "Query 3", "Query 4", "Query 5", "Query 6", "Query 7", "Query 8", "Query 9", "Query 10", "Query 11", "Query 12", "Query 13", "Query 14", "Query 15", "Query 16", "Query 17", "Query 18", "Query 19", "Query 20", "Query 21", "Query 22", "Query 23", "Query 24", "Query 25", "Query 26a","Query 26b","Query 27a","Query 27b", "Query 28a","Query 28b", "Query 29", "Query 30"}));
		querySelectBox.setBounds(41, 45, 96, 20);
		contentPane.add(querySelectBox);
		
		
		
		
		
		
		
		
		
		
	}
}
