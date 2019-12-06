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

class SetDate extends JFrame implements ActionListener{
  JPanel panel;
  JLabel day_label, month_label, year_label, message;
  JTextField day_text, month_text, year_text;
  JButton submit, goBack;
  App app;
  JFrame mainFrame;

    SetDate(JFrame mainFrame, App app) {
	this.app = app;
    this.mainFrame = mainFrame;
        // day
        day_label = new JLabel();
        day_label.setText("Select Day :");
        day_text = new JTextField();

        // month
        month_label = new JLabel();
        month_label.setText("Select Month :");
        month_text = new JTextField();

        // month
        year_label = new JLabel();
        year_label.setText("Select Year :");
        year_text = new JTextField();

        // Submit

	//goBack = new JButton("Go Back");
        submit = new JButton("SUBMIT");

        panel = new JPanel(new GridLayout(4, 1));

        panel.add(day_label);
        panel.add(day_text);
        panel.add(month_label);
        panel.add(month_text);
        panel.add(year_label);
        panel.add(year_text);

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
        String dayValue = day_text.getText();
        String monthValue = month_text.getText();
        String yearValue = year_text.getText();

        dayValue = dayValue.trim();
        monthValue = monthValue.trim();
        yearValue = yearValue.trim();
        int day = Integer.parseInt(dayValue);
        int month = Integer.parseInt(monthValue);
        int year = Integer.parseInt(yearValue);

        app.setDate(year, month, day);
        // message.setText(" Hello " + day + "");
        // System.out.println("Conditional checking pin");
        mainFrame.getContentPane().removeAll();
        mainFrame.repaint();
        BankTellerScreen BS = new BankTellerScreen(mainFrame, this.app);
       
    }
}
