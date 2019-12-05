package cs174a;
import cs174a.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//SImple operation is where you have an amount and ONE aid
//SimpleOperation
public class AccountManagement extends JFrame implements ActionListener{
  JPanel panel;
  JLabel aid_label, balance_label, bname_label, accType_label, owners_label, message, linked_label, cid_label;
  JTextField aid_text, balance_text ,bname_text, accType_text, owners_text, linked_text, cid_text;
  JButton submit, cancel;
  App app;
  int selector;
  JFrame mainFrame;

    AccountManagement(App app, int selector, JFrame mainFrame) {
	this.app = app;
	this.selector = selector;
        this.mainFrame = mainFrame;

        aid_label = new JLabel();
        aid_label.setText("Account ID :");
        aid_text = new JTextField();

        balance_label = new JLabel();
        balance_label.setText("Balance :");
        balance_text = new JTextField();

	bname_label = new JLabel();
        bname_label.setText("Branch Name :");
        bname_text = new JTextField();

	accType_label = new JLabel();
        accType_label.setText("Account Type :");
        accType_text = new JTextField();

	owners_label = new JLabel();
        owners_label.setText("Owners :");
        owners_text = new JTextField();

        linked_label = new JLabel();
        linked_label.setText("LInked Account :");
        linked_text = new JTextField();


        // Submit
        submit = new JButton("SUBMIT");

        panel = new JPanel(new GridLayout(8, 1));

	panel.add(aid_label);
        panel.add(aid_text);
        panel.add(balance_label);
        panel.add(balance_text);
        panel.add(bname_label);
        panel.add(bname_text);
        panel.add(accType_label);
        panel.add(accType_text);
	panel.add(owners_label);
        panel.add(owners_text);
        panel.add(linked_label);
        panel.add(linked_text);
	
        message = new JLabel();
        panel.add(message);
        panel.add(submit);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the listeners to components..
        submit.addActionListener(this);
        add(panel, BorderLayout.CENTER);
        setTitle("Select desired values !");
        setSize(500, 200);
        // setVisible(true);
        mainFrame.add(panel);
	mainFrame.show();

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String aidValue = aid_text.getText();
	String balanceValue = balance_text.getText();
        String bnameValue  = bname_text.getText();
	String accTypeValue = accType_text.getText();
	String ownersValue = owners_text.getText();
        String linkedValue = linked_text.getText();

        String aid = aidValue.trim();
        String linked = linkedValue.trim();
        //int aid = Integer.parseInt(aidValue);
        balanceValue = balanceValue.trim();
        double balance = Double.parseDouble(balanceValue);
	bnameValue = bnameValue.trim();
        //int bname = Integer.parseInt(avgValue);
	accTypeValue = accTypeValue.trim();
	ownersValue = ownersValue.trim();

	//BASICALLY DO SWITCH STATEMENTS WITH IF CONDITIONALS TO SEE WHAT TYPE OF TRANSACTION WE DO WHEN WE SEND IT TO APP
	//HARD CODING DATE VARIABLES RN
	//WILL NEED TO CALL CURRENT DATE FUNCTION
	int day = 1;
	int month = 1;
	int year = 2020;
	// String returnValue;

	//CreateAccount TRANSACTION
	if(this.selector == 1)
	{
		app.CreateAccount(aid, balance, bnameValue, accTypeValue, ownersValue, linked);
		// System.out.println(returnValue);
		mainFrame.getContentPane().removeAll();
		mainFrame.repaint();
		ClientScreen cs = new ClientScreen( this.app, mainFrame);
	}

	//ClosedAccount TRANSACTION
	if(this.selector == 2)
	{
		app.CloseAccount(aid, balance, bnameValue, accTypeValue, ownersValue, linked);
		// System.out.println(returnValue);
		mainFrame.getContentPane().removeAll();
		mainFrame.repaint();
		ClientScreen cs = new ClientScreen( this.app, mainFrame);
	}

	//DeleteAccount FUNCTION
	if(this.selector == 3)
	{
		app.DeleteAccount(aid, balance, bnameValue, accTypeValue, ownersValue, linked);
		// System.out.println(returnValue);
		mainFrame.getContentPane().removeAll();
		mainFrame.repaint();
		ClientScreen cs = new ClientScreen(  this.app, mainFrame);
	}

		}
	}
