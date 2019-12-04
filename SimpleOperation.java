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
public class SimpleOperation extends JFrame implements ActionListener{
  JPanel panel;
  JLabel amount_label, account_label, message, customerName_label;
  JTextField account_text, amount_text, customerName_text;
  JButton submit, cancel;
  App app;
  int selector;
  JFrame mainFrame;

    SimpleOperation(App app, int selector, JFrame mainFrame) {
	this.app = app;
	this.selector = selector;
        this.mainFrame = mainFrame;
        // User Label
        amount_label = new JLabel();
        amount_label.setText("Ammount :");
        amount_text = new JTextField();

        // Password

        account_label = new JLabel();
        account_label.setText("Account :");
        account_text = new JTextField();

	customerName_label = new JLabel();
        customerName_label.setText("Customer's Name :");
        customerName_text = new JTextField();
	

        // Submit

        submit = new JButton("SUBMIT");

        panel = new JPanel(new GridLayout(4, 1));

	panel.add(customerName_label);
        panel.add(customerName_text);
        panel.add(amount_label);
        panel.add(amount_text);
        panel.add(account_label);
        panel.add(account_text);



        message = new JLabel();
        panel.add(message);
        panel.add(submit);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the listeners to components..
        submit.addActionListener(this);
        add(panel, BorderLayout.CENTER);
        setTitle("Select desired values !");
        setSize(300, 100);
        // setVisible(true);

        mainFrame.add(panel);
        mainFrame.show();

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String amountValue = amount_text.getText();
        String accountValue  = account_text.getText();
	String CustomerNameValue = customerName_text.getText();

        amountValue = amountValue.trim();
        double amount = Double.parseDouble(amountValue);
        accountValue = accountValue.trim();
        int account = Integer.parseInt(accountValue);
	CustomerNameValue = CustomerNameValue.trim();

	//BASICALLY DO SWITCH STATEMENTS WITH IF CONDITIONALS TO SEE WHAT TYPE OF TRANSACTION WE DO WHEN WE SEND IT TO APP
	//HARD CODING DATE VARIABLES RN
	//WILL NEED TO CALL CURRENT DATE FUNCTION
	int day = 1;
	int month = 1;
	int year = 2020;
	// String returnValue;

	//DEPOSIT TRANSACTION
	if(this.selector == 1)
	{
		app.ClientDeposit(month, day, year, CustomerNameValue, amount, account);
		// System.out.println(returnValue);
		mainFrame.getContentPane().removeAll();
                mainFrame.repaint();
                ClientScreen cs = new ClientScreen(this.app, mainFrame);
	}

	//PURCHASE TRANSACTION
        if(this.selector == 2)
        {
                app.ClientPurchase(month, day, year, CustomerNameValue, amount, account);
                //System.out.println(returnValue);
		mainFrame.getContentPane().removeAll();
                mainFrame.repaint();
                ClientScreen cs = new ClientScreen( this.app, mainFrame);
        }

	//WITHDRAW FUNCTION
        if(this.selector == 3)
        {
                app.ClientWithdraw(month, day, year, CustomerNameValue, amount, account);
    		//System.out.println(returnValue);
		mainFrame.getContentPane().removeAll();
                mainFrame.repaint();
                ClientScreen cs = new ClientScreen(this.app, mainFrame);
                //message.setText(" Invalid input.. ");
        }

}
}
