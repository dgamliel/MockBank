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

class SetInterestRate extends JFrame implements ActionListener{
  JPanel panel;
  JLabel AccType_label, InterestRate_label, message;
  JTextField AccType_text, InterestRate_text;
  JButton submit, goBack;
  App app;
  JFrame mainFrame;

    SetInterestRate(JFrame mainFrame, App app) {
	this.app = app;
    this.mainFrame = mainFrame;
        // day
        AccType_label = new JLabel();
        AccType_label.setText("Select Account Type :");
        AccType_text = new JTextField();

        // month
        InterestRate_label = new JLabel();
        InterestRate_label.setText("Select Interest Rate :");
        InterestRate_text = new JTextField();

        // Submit

	//goBack = new JButton("Go Back");
        submit = new JButton("SUBMIT");

        panel = new JPanel(new GridLayout(4, 1));

        panel.add(AccType_label);
        panel.add(AccType_text);
        panel.add(InterestRate_label);
        panel.add(InterestRate_text);

        message = new JLabel();
        panel.add(message);
        panel.add(submit);
	//panel.add(goBack);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the listeners to components..
        submit.addActionListener(this);
        add(panel, BorderLayout.CENTER);
        setTitle("Please Select Date Here !");
        setSize(300, 100);
        // setVisible(true);
        mainFrame.add(panel);
        mainFrame.show();

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String AccTypeValue = AccType_text.getText();
        String IntRateValue = InterestRate_text.getText();


        AccTypeValue = AccTypeValue.trim();
        IntRateValue = IntRateValue.trim();
       
        double InterestRate = Double.parseDouble(IntRateValue);
        app.setInterestRate(AccTypeValue, InterestRate);
        // message.setText(" Hello " + day + "");
        // System.out.println("Conditional checking pin");
        mainFrame.getContentPane().removeAll();
        mainFrame.repaint();
        BankTellerScreen BS = new BankTellerScreen(mainFrame, this.app);
       
    }
}
