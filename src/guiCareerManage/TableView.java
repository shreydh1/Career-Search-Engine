package guiCareerManage;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import dbConnection.TableInfo;
import java.awt.Dimension;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.MatteBorder;
import java.awt.Color;

public class TableView {

	JFrame tableviewframe;
	private JTable table;
	static ResultSet rset = null;
	static Connection conn;
	TableInfo ti ;
	static String setTitle;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					JFrame tableviewframe = new JFrame();
					tableviewframe.setVisible(true);
					//tableviewframe.setLocationRelativeTo(null);
					tableviewframe.setTitle("just testing");
					tableviewframe.setBounds(215, 100, 928, 488);
					tableviewframe.setSize(928, 488);
					tableviewframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					tableviewframe.getContentPane().setLayout(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 */
	public TableView(ResultSet rs, Connection conn, String Title) throws SQLException {
		this.setTitle = Title;
		initialize();
		
		ti = new TableInfo(conn);
		TableModel tableModel = new DefaultTableModel(ti.resultSet2Vector(rs), ti.getTitlesAsVector(rs));
		table.setModel(tableModel);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		tableviewframe = new JFrame();
		tableviewframe.setTitle(setTitle);
		tableviewframe.setBounds(217, 100, 928, 488);
		tableviewframe.setSize(928, 488);
		tableviewframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tableviewframe.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		tableviewframe.getContentPane().add(scrollPane);
		scrollPane.setBounds(12, 13, 886, 415);
		{
			TableModel resultTableModel = new DefaultTableModel(
					new String[][] { { "", "" }, { "", "" } },
					new String[] { "", "" });
				table = new JTable();
				table.setPreferredScrollableViewportSize(new Dimension(450, 10000));
				scrollPane.setViewportView(table);
				table.setModel(resultTableModel);
				table.setAutoscrolls(true);
		}
		
	}

}
