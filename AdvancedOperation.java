//Advanced Operatiojn means do some transaction where you have an amount and multiple AIDs


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
public class AdvancedOperation extends JFrame implements ActionListener{
  JPanel panel;
  JLabel amount_label, Fromaccount_label, Toaccount_label, message, customerName_label;
  JTextField Fromaccount_text, Toaccount_text ,amount_text, customerName_text;
  JButton submit, cancel;
  App app;
  int selector;
  JFrame mainFrame;

    AdvancedOperation(App app, int selector, JFrame mainFrame) {
	this.app = app;
	this.selector = selector;
        this.mainFrame = mainFrame;
        // User Label
        amount_label = new JLabel();
        amount_label.setText("Ammount :");
        amount_text = new JTextField();

        // Password

        Fromaccount_label = new JLabel();
        Fromaccount_label.setText("From Account :");
        Fromaccount_text = new JTextField();

	Toaccount_label = new JLabel();
        Toaccount_label.setText("To Account :");
        Toaccount_text = new JTextField();

	customerName_label = new JLabel();
        customerName_label.setText("Customer's Name :");
        customerName_text = new JTextField();


        // Submit

        submit = new JButton("SUBMIT");

        panel = new JPanel(new GridLayout(5, 1));

	panel.add(customerName_label);
        panel.add(customerName_text);
        panel.add(amount_label);
        panel.add(amount_text);
        panel.add(Fromaccount_label);
	panel.add(Toaccount_label);
        panel.add(Toaccount_text);
	panel.add(Fromaccount_text);
	


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
	String FromaccountValue = Fromaccount_text.getText();
        String ToaccountValue  = Toaccount_text.getText();
	String CustomerNameValue = customerName_text.getText();

        amountValue = amountValue.trim();
        double amount = Double.parseDouble(amountValue);
        ToaccountValue = ToaccountValue.trim();
        int Toaccount = Integer.parseInt(ToaccountValue);
	FromaccountValue = FromaccountValue.trim();
        int Fromaccount = Integer.parseInt(FromaccountValue);
	CustomerNameValue = CustomerNameValue.trim();

	//BASICALLY DO SWITCH STATEMENTS WITH IF CONDITIONALS TO SEE WHAT TYPE OF TRANSACTION WE DO WHEN WE SEND IT TO APP
	//HARD CODING DATE VARIABLES RN
	//WILL NEED TO CALL CURRENT DATE FUNCTION
	int day = 1;
	int month = 1;
	int year = 2020;
	// String returnValue;

	//TOPUP TRANSACTION
	if(this.selector == 4)
	{
		app.ClientTopup(month, day, year, CustomerNameValue, amount, Fromaccount, Toaccount);
		// System.out.println(returnValue);
		mainFrame.getContentPane().removeAll();
                mainFrame.repaint();
                ClientScreen cs = new ClientScreen(this.app,  mainFrame);
	}

	//COLLECTS TRANSACTION
        if(this.selector == 5)
        {
                app.ClientCollects(month, day, year, CustomerNameValue, amount, Fromaccount, Toaccount);
                //System.out.println(returnValue);
		mainFrame.getContentPane().removeAll();
                mainFrame.repaint();
                ClientScreen cs = new ClientScreen(this.app,  mainFrame);
        }

	//ClientTransfer FUNCTION
        if(this.selector == 6)
        {
                app.ClientTransfer(month, day, year, CustomerNameValue, amount, Fromaccount, Toaccount);
                //System.out.println(returnValue);
		mainFrame.getContentPane().removeAll();
                mainFrame.repaint();
                ClientScreen cs = new ClientScreen(this.app,  mainFrame);
        }

	//ClientPayfriend FUNCTION
        if(this.selector == 7)
        {
                app.ClientPayfriend(month, day, year, CustomerNameValue, amount, Fromaccount, Toaccount);
                //System.out.println(returnValue);
		mainFrame.getContentPane().removeAll();
                mainFrame.repaint();
                ClientScreen cs = new ClientScreen(this.app,  mainFrame);
        }

	//WIRE FUNCTION
        if(this.selector == 8)
        {
                app.ClientWire(month, day, year, CustomerNameValue, amount, Fromaccount, Toaccount);
                //System.out.println(returnValue);
		mainFrame.getContentPane().removeAll();
                mainFrame.repaint();
                ClientScreen cs = new ClientScreen(this.app,  mainFrame);
        }

}
}

