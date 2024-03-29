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

class ClientLogin extends JFrame implements ActionListener{
  JPanel panel;
  JLabel user_label, password_label, message;
  JTextField userName_text;
  JPasswordField password_text;
  JButton submit, goBack;
  App app;
  JFrame mainFrame;

    ClientLogin(JFrame mainFrame, App app) {
	this.app = app;
    this.mainFrame = mainFrame;
        // User Label
        user_label = new JLabel();
        user_label.setText("User Name :");
        userName_text = new JTextField();

        // Password

        password_label = new JLabel();
        password_label.setText("Password :");
        password_text = new JPasswordField();

        // Submit

	//goBack = new JButton("Go Back");
        submit = new JButton("SUBMIT");

        panel = new JPanel(new GridLayout(3, 1));

        panel.add(user_label);
        panel.add(userName_text);
        panel.add(password_label);
        panel.add(password_text);

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
        String userName = userName_text.getText();
        String password = password_text.getText();

        userName = userName.trim();
        int tid = Integer.parseInt(userName);
        password = password.trim();
        int pin = Integer.parseInt(password);


				boolean isVerify = app.VerifyPin(tid, pin);
        //GOING TO KEEP THIS UNTIL DB CAN VERIFY PIN ITSELF
        if (isVerify == true)
        {
            message.setText(" Hello " + userName + "");
            System.out.println("Conditional checking pin");
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
            ClientScreen cs = new ClientScreen(this.app, mainFrame);
        }
        else
        {
            message.setText(" Invalid user.. ");
        }
    }
}
