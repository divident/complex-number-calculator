import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.script.ScriptException;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

public class Application {

	private JFrame frame;
	private TreeMain treeMain;
	private EvalScript evalScript;
	private JTextPane pnScript;
	private JTextField textExpr;
	private JTextPane textResult;
	private JTextPane textVar;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
		frame.setTitle("Complex nubmer calculator");
		treeMain = new TreeMain();
		try {
			evalScript = new EvalScript(treeMain.getEval().variables, treeMain);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 606, 402);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnEvaluate = new JButton("Evaluate");
		btnEvaluate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					evalScript.eval(pnScript.getText());
				} catch (ScriptException e1) {
					JOptionPane.showMessageDialog(frame,
						    "Script is not valid " + e1.getMessage(),
						    "Script warning",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnEvaluate.setBounds(10, 303, 89, 23);
		frame.getContentPane().add(btnEvaluate);
		
		JLabel lblScript = new JLabel("Script");
		lblScript.setBounds(10, 21, 46, 14);
		frame.getContentPane().add(lblScript);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 41, 252, 251);
		frame.getContentPane().add(scrollPane);
		
		pnScript = new JTextPane();
		scrollPane.setViewportView(pnScript);
		
		JLabel lblExpression = new JLabel("Expression");
		lblExpression.setBounds(285, 21, 89, 14);
		frame.getContentPane().add(lblExpression);
		
		textVar = new JTextPane();
		textVar.setBounds(285, 97, 265, 23);
		frame.getContentPane().add(textVar);
		
		JLabel lblVariables = new JLabel("Variables");
		lblVariables.setBounds(285, 75, 61, 14);
		frame.getContentPane().add(lblVariables);
		
		textExpr = new JTextField();
		textExpr.setBounds(285, 44, 269, 20);
		frame.getContentPane().add(textExpr);
		textExpr.setColumns(10);
		
		textResult = new JTextPane();
		textResult.setBounds(285, 148, 265, 39);
		textResult.setEditable(false);
		frame.getContentPane().add(textResult);
		
		JLabel lblResult = new JLabel("Result");
		lblResult.setBounds(285, 131, 46, 14);
		frame.getContentPane().add(lblResult);
		
		JButton btnCalculate = new JButton("Calculate");
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!textVar.getText().isEmpty()) {
					getVariables(textVar.getText());
				}
				textResult.setText(treeMain.eval(textExpr.getText()));
			}
		});
		btnCalculate.setBounds(461, 303, 89, 23);
		frame.getContentPane().add(btnCalculate);
	}
	
	private void getVariables(String input) {
		//Variables format a=(2, 1); b=(0, 1)
		String[] complexVar = input.split(";");
		for(String var : complexVar) {
			String[] varNames = var.split("=");
			if(varNames.length != 2) 
				throw new IllegalArgumentException("Niepoprawny format zmiennej");
			String name = varNames[0].trim();
			String[] number = varNames[1].split(",");
			if(number.length != 2) throw new IllegalArgumentException("Niepoprawny format zmiennej");
			Double realPart = Double.parseDouble(number[0].substring(1));
			Double imgPart = Double.parseDouble(number[1].trim().substring(0, number[1].length()-2));
			treeMain.getEval().variables.put(name, new ComplexNumber(realPart, imgPart));
		}
	}
}
