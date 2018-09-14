package guiCareerManage;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import pojoClass.person;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.TextArea;

public class AddPerson {

	JFrame addPerson;
	private JTextField personId;
	private JTextField personAddress;
	private JTextField personCity;
	private JTextField personEmail;
	private JTextField personStreet;
	private JTextField personName;
	private JTextField personZipCode;
	private JTextField personGender;
	private JLabel errorLabel ;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddPerson window = new AddPerson();
					window.addPerson.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AddPerson() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		addPerson = new JFrame();
		addPerson.setTitle("Add Person Form");
		addPerson.setBounds(100, 100, 454, 311);
		addPerson.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addPerson.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Enter Person Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 13, 412, 238);
		addPerson.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblPersonid = new JLabel("ID");
		lblPersonid.setBounds(11, 40, 56, 16);
		panel.add(lblPersonid);
		
		personId = new JTextField();
		personId.setBounds(79, 37, 116, 22);
		panel.add(personId);
		personId.setColumns(10);
		
		JLabel lblName = new JLabel("Address");
		lblName.setBounds(11, 73, 56, 16);
		panel.add(lblName);
		
		personAddress = new JTextField();
		personAddress.setColumns(10);
		personAddress.setBounds(79, 70, 116, 22);
		panel.add(personAddress);
		
		JLabel lblAddress = new JLabel("City");
		lblAddress.setBounds(11, 112, 56, 16);
		panel.add(lblAddress);
		
		personCity = new JTextField();
		personCity.setColumns(10);
		personCity.setBounds(79, 109, 116, 22);
		panel.add(personCity);
		
		JLabel lblStreet = new JLabel("Email");
		lblStreet.setBounds(11, 144, 56, 16);
		panel.add(lblStreet);
		
		personEmail = new JTextField();
		personEmail.setColumns(10);
		personEmail.setBounds(79, 141, 116, 22);
		panel.add(personEmail);
		
		JLabel lblStreet_1 = new JLabel("Street");
		lblStreet_1.setBounds(207, 76, 56, 16);
		panel.add(lblStreet_1);
		
		personStreet = new JTextField();
		personStreet.setColumns(10);
		personStreet.setBounds(275, 73, 116, 22);
		panel.add(personStreet);
		
		JLabel lblName_1 = new JLabel("Name");
		lblName_1.setBounds(207, 43, 56, 16);
		panel.add(lblName_1);
		
		personName = new JTextField();
		personName.setColumns(10);
		personName.setBounds(275, 40, 116, 22);
		panel.add(personName);
		
		personZipCode = new JTextField();
		personZipCode.setColumns(10);
		personZipCode.setBounds(275, 112, 116, 22);
		panel.add(personZipCode);
		
		JLabel lblZipcode = new JLabel("Zipcode");
		lblZipcode.setBounds(207, 115, 56, 16);
		panel.add(lblZipcode);
		
		personGender = new JTextField();
		personGender.setColumns(10);
		personGender.setBounds(275, 144, 116, 22);
		panel.add(personGender);
		
		JLabel lblGender = new JLabel("Gender");
		lblGender.setBounds(207, 147, 56, 16);
		panel.add(lblGender);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				person p1 = new person();
				int id = Integer.parseInt(personId.getText());
				String name = personName.getText();
				String address = personAddress.getText();
				String street = personStreet.getText();
				String city = personCity.getText();
				int zip = Integer.parseInt(personZipCode.getText());
				String email = personEmail.getText();
				String gender = personGender.getText();		
				/*1100, "Saranyan", "1610", "Robert E lee", "New Orleans", 70122, "ssenthiv@uno.edu", "Male"*/
				boolean check = p1.addPersonDetails(id, name, address, street, city, zip, email, gender);
				if (check == false)
				{
					addPerson.dispose();
				}
				else
				{
					errorLabel.setText("Insert Failed - Enter Values properly");
					personId.setText("");
					personName.setText("");
					personAddress.setText("");
					personStreet.setText("");
					personCity.setText("");
					personZipCode.setText("");
					personEmail.setText("");
					personGender.setText("");
				}
			}
		});
		btnUpdate.setBounds(155, 176, 97, 25);
		panel.add(btnUpdate);
		
		errorLabel = new JLabel("");
		errorLabel.setBounds(26, 209, 343, 16);
		panel.add(errorLabel);
	}
}
