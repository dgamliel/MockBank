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

class GeneralReport extends JFrame implements ActionListener{
  JPanel panel;
  JLabel cid_label, message;
  JTextField cid_text;
  JButton submit, goBack;
  App app;
  JFrame mainFrame;
  int selector;

    GeneralReport(JFrame mainFrame, App app, int selector) {
	this.app = app;
    this.mainFrame = mainFrame;
    this.selector = selector;
        // User Label
        cid_label = new JLabel();
        cid_label.setText("Customer ID :");
        cid_text = new JTextField();

        // Submit
	//goBack = new JButton("Go Back");
        submit = new JButton("SUBMIT");

        panel = new JPanel(new GridLayout(2, 1));

        panel.add(cid_label);
        panel.add(cid_text);

        message = new JLabel();
        panel.add(message);
        panel.add(submit);
	//panel.add(goBack);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the listeners to components..
        submit.addActionListener(this);
        add(panel, BorderLayout.CENTER);
        setTitle("Please Login Here !");
        setSize(300, 100);
        // setVisible(true);
        mainFrame.add(panel);
        mainFrame.show();

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cidValue = cid_text.getText();
        cidValue = cidValue.trim();


        if (selector == 1)
        {
            app.GenerateMonthlyStatement(cidValue);
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
            BankTellerScreen bts = new BankTellerScreen(mainFrame, this.app);
        }

        if (selector == 2)
        {
            app.GenerateCustomerReport(cidValue);
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
            BankTellerScreen bts = new BankTellerScreen(mainFrame, this.app);
        }
        
    }
}
