import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class mainpage {
	
	SQLoperations manage;
	JButton submit;
	String[] questionsArray, option1Array, option2Array, option3Array, option4Array;
	static DefaultTableModel model;
	static DefaultTableModel questionTableModel; // Added for displaying questions and options with counts
	String cd;
	
	int i=0, h=0;
	String[] queStr = new String[50];
	String[] op1Str = new String[50];
	String[] op2Str = new String[50];
	String[] op3Str = new String[50];
	String[] op4Str = new String[50];
	int id;
	public void mainPageView(int id) throws SQLException {
		this.id=id;
		questionsArray = new String[100];
		option1Array = new String[100]; 
		option2Array = new String[100];
		option3Array = new String[100];
		option4Array = new String[100];
		
		manage = new SQLoperations();
		
		JFrame frame = new JFrame();
		frame.setSize(800, 600);
		frame.setLayout(null);
		frame.setLocationRelativeTo(null);
		
		/*Add Survey */
		JPanel addPanel = new JPanel();
		addPanel.setBounds(250, 0, 550, 600);
		addPanel.setLayout(null);
		
		JLabel question = new JLabel("Question : ");
		question.setBounds(50, 100, 100, 20);
		addPanel.add(question);
		JTextField questionField = new JTextField();
		questionField.setBounds(50, 125, 450, 30);
		addPanel.add(questionField);
		
		JLabel option1 = new JLabel("Option 1 : ");
		option1.setBounds(50, 165, 100, 20);
		addPanel.add(option1);
		JTextField option1Field = new JTextField();
		option1Field.setBounds(50, 190, 200, 30);
		addPanel.add(option1Field);
		
		JLabel option2 = new JLabel("Option 2 : ");
		option2.setBounds(50, 230, 100, 20);
		addPanel.add(option2);
		JTextField option2Field = new JTextField();
		option2Field.setBounds(50, 255, 200, 30);
		addPanel.add(option2Field);
		
		JLabel option3 = new JLabel("Option 3 : ");
		option3.setBounds(50, 295, 100, 20);
		addPanel.add(option3);
		JTextField option3Field = new JTextField();
		option3Field.setBounds(50, 320, 200, 30);
		addPanel.add(option3Field);
		
		JLabel option4 = new JLabel("Option 4 : ");
		option4.setBounds(50, 360, 100, 20);
		addPanel.add(option4);
		JTextField option4Field = new JTextField();
		option4Field.setBounds(50, 385, 200, 30);
		addPanel.add(option4Field);
		
		JLabel start = new JLabel("CREATE A SURVEY");
		start.setBounds(0, 10, 550, 50);
		start.setHorizontalAlignment(JLabel.CENTER);
		start.setFont(new Font("Times New Roman", Font.BOLD, 40));
		addPanel.add(start);
		
		JButton next = new JButton("ADD QUESTION");
		next.setBounds(50, 440, 450, 35);
		addPanel.add(next);
		next.addActionListener(new ActionListener( ) {
			@Override
			public void actionPerformed(ActionEvent e) {
				String qn = questionField.getText();
				String op1 = option1Field.getText();
				String op2 = option2Field.getText();;
				String op3 = option3Field.getText();;
				String op4 = option4Field.getText();;
				if(qn.equals("") || op1.equals("") || op2.equals("") || op3.equals("") || op4.equals("")) {
					JOptionPane.showMessageDialog(frame, "Please Fill All Options.", "Warning Message", JOptionPane.WARNING_MESSAGE);
				}
				else {
					questionField.setText("");
					option1Field.setText("");
					option2Field.setText("");
					option3Field.setText("");
					option4Field.setText("");
					queStr[i] = qn;
					op1Str[i] = op1;
					op2Str[i] = op2;
					op3Str[i] = op3;
					op4Str[i] = op4;
					i++;
					submit.setEnabled(true);


					clearDisplayedQuestionAndOptions();


				}
			}
		});
		
		submit = new JButton("SUBMIT");
		submit.setBounds(50, 490, 200, 50);
		submit.setEnabled(false);
		addPanel.add(submit);
		submit.addActionListener(new ActionListener( ) {
			@Override
			public void actionPerformed(ActionEvent e) {
				String code = stringGenerator();
            

                String qn = questionField.getText();
                String op1 = option1Field.getText();
                String op2 = option2Field.getText();
                String op3 = option3Field.getText();
                String op4 = option4Field.getText();
				if(!(qn.equals("") && op1.equals("") && op2.equals("") && op3.equals("") && op4.equals(""))) {
					JOptionPane.showMessageDialog(frame, "Last details are not added. If not required, Please clear the fields.", "Warning Message", JOptionPane.WARNING_MESSAGE);
				}
				else {
					if(i==0) {
						JOptionPane.showMessageDialog(frame, "No Questions Added", "Warning Message", JOptionPane.WARNING_MESSAGE);
					}
					else {
						try {
							manage.userQuestionAdd(id, code);
							for(int j=0; j<i; j++) {
								manage.newQuestion(code, queStr[j], op1Str[j], op2Str[j], op3Str[j], op4Str[j]);
							}
								JOptionPane.showMessageDialog(frame, "Survey Added. Unique Code : " + code, "Congradulations", JOptionPane.PLAIN_MESSAGE); 
						}
						catch (SQLException e1) {
								e1.printStackTrace();
						}
					}
				}
				submit.setEnabled(false);

				        clearDisplayedQuestionAndOptions();

			}
		});
		
		JButton cancel = new JButton("CANCEL");
		cancel.setBounds(300, 490, 200, 50);
		addPanel.add(cancel);
		cancel.addActionListener(new ActionListener( ) {
			@Override
			public void actionPerformed(ActionEvent e) {
				questionField.setText("");
				option1Field.setText("");
				option2Field.setText("");
				option3Field.setText("");
				option4Field.setText("");
				i=0;
			}
		});
		
		frame.add(addPanel);
		
		/*View all survey */
		JPanel viewPanel = new JPanel();
		viewPanel.setBounds(250, 0, 550, 600);
		viewPanel.setLayout(null);
		JLabel searchLabel = new JLabel("Search : ");
		searchLabel.setBounds(100, 20, 100, 50);
		viewPanel.add(searchLabel);
		
		JTextField search = new JTextField();
		search.setBounds(160, 30, 290, 30);
		viewPanel.add(search);
		search.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				tableupdate(search.getText());
			}

			@Override
			public void keyTyped(KeyEvent e) {
				//To avoid errors.
			}

			@Override
			public void keyPressed(KeyEvent e) {
				//To avoid errors.
			}
		});
		
		JTable table=new JTable(){
			public boolean isCellEditable(int row,int column){
				return false;
			}
		};
		model = (DefaultTableModel)table.getModel();
		table.setBackground(Color.decode("#BE398D"));
		model.addColumn("Your Surveys");
		tableupdate("");
		JScrollPane scPane=new JScrollPane(table);
		scPane.setBounds(100, 70, 350, 225);
		viewPanel.add(scPane);

		// Initialize questionTableModel for displaying questions, options, and counts
		JTable questionTable =new JTable(){
			public boolean isCellEditable(int row,int column){
				return false;
			}
		};

		questionTableModel = (DefaultTableModel)questionTable.getModel();
		questionTableModel.addColumn("Label");
		questionTableModel.addColumn("Question/Options");
		questionTableModel.addColumn("Count");


		JScrollPane questionScrollPane = new JScrollPane(questionTable);
		questionScrollPane.setBounds(100, 360, 350, 150);
		viewPanel.add(questionScrollPane);
		
		JButton delete = new JButton("DELETE");
		delete.setBounds(210, 300, 130, 50);
		delete.setEnabled(false);
		viewPanel.add(delete);
		
		JButton viewPrev = new JButton("PREVIOUS");
		viewPrev.setBounds(100, 300, 100, 50);
		viewPrev.setEnabled(false);
		viewPanel.add(viewPrev);

		viewPrev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (h > 0) {
					h--;
					displayQuestionAndOptions(h);
				} else {
					JOptionPane.showMessageDialog(frame, "No previous questions available.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		JButton viewNext = new JButton("NEXT");
		viewNext.setBounds(350, 300, 100, 50);
		viewNext.setEnabled(false);
		viewPanel.add(viewNext);

		viewNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				h++;
				if (h < questionsArray.length  && questionsArray[h] != null) {
					displayQuestionAndOptions(h);
				} else {
					JOptionPane.showMessageDialog(frame, "No more questions available.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					manage.removeSurvey(cd);

					// Clear the displayed questions and options
					clearDisplayedQuestionAndOptions();

					tableupdate(search.getText());

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		});

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				h = 0;
				delete.setEnabled(true);
				viewNext.setEnabled(true);
				viewPrev.setEnabled(true);
				int row = table.getSelectedRow();
				cd = String.valueOf(model.getValueAt(row, 0));

				// Retrieve questions and options for the selected quiz code
				try {
					ResultSet rst1 = manage.getQuestions(cd);



					for (int x = 0; rst1.next(); x++) {
						questionsArray[x] = rst1.getString("question");
						option1Array[x] = rst1.getString("option1");
						option2Array[x] = rst1.getString("option2");
						option3Array[x] = rst1.getString("option3");
						option4Array[x] = rst1.getString("option4");
					}

					// Display the first question and options
					displayQuestionAndOptions(h);

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		frame.add(viewPanel);
		
		JPanel optionPanel = new JPanel();
		optionPanel.setBounds(0, 0, 250, 600);
		optionPanel.setBackground( Color.decode("#BE398D"));
		optionPanel.setLayout(null);
		frame.add(optionPanel);
		
		JButton addSurvey = new JButton("ADD SURVEY");
		addSurvey.setBounds(50, 113, 150, 50);
		optionPanel.add(addSurvey);
		addSurvey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewPanel.setVisible(false);
				addPanel.setVisible(true);
			}
		});
		
		JButton viewSurvey = new JButton("VIEW SURVEYS");
		viewSurvey.setBounds(50, 276, 150, 50);
		optionPanel.add(viewSurvey);
		viewSurvey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableupdate(search.getText());
				viewPanel.setVisible(true);
				addPanel.setVisible(false);
			}
		});
		
		JButton logout = new JButton("LOGOUT");
		logout.setBounds(50, 440, 150, 50);
		optionPanel.add(logout);
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		
		viewPanel.setVisible(false);
		
		frame.setVisible(true);
	}
	// Add this method to clear the displayed question and options
private void clearDisplayedQuestionAndOptions() {
    Arrays.fill(questionsArray, null);
    Arrays.fill(option1Array, null);
    Arrays.fill(option2Array, null);
    Arrays.fill(option3Array, null);
    Arrays.fill(option4Array, null);
    h = 0; // Reset the index for displayed questions and options
    displayQuestionAndOptions(h);
}

	
	private void displayQuestionAndOptions(int index) {
		questionTableModel.setRowCount(0); // Clear previous data
		System.out.println(index);

		// Display the question
		questionTableModel.addRow(new Object[] { "Question:", questionsArray[index] });
		

		// Display options and their counts
		try {
			questionTableModel.addRow(new Object[] { "Option 1:", option1Array[index], manage.getCount(cd, index, 1) });
			questionTableModel.addRow(new Object[] { "Option 2:", option2Array[index], manage.getCount(cd, index, 2) });
			questionTableModel.addRow(new Object[] { "Option 3:", option3Array[index], manage.getCount(cd, index, 3) });
			questionTableModel.addRow(new Object[] { "Option 4:", option4Array[index], manage.getCount(cd, index, 4) });
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String stringGenerator() {
		  String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			  String sb = "";
			  for (int i = 0; i < 5; i++) {
			   int index = (int)(AlphaNumericString.length() * Math.random());
			   sb += (AlphaNumericString.charAt(index));
			  }
		return sb;
	}
	
	public void tableupdate(String str) {
		try {
			SQLoperations man = new SQLoperations();
			ResultSet res = man.surveys(id, str);
			int rowCount = model.getRowCount();
			int i;
			for (i = rowCount - 1; i >= 0; i--)
			    model.removeRow(i);
			for(i=0; res.next(); i++) {
				model.addRow(new Object[0]);
		        model.setValueAt(res.getString("quizcode"), i, 0);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
