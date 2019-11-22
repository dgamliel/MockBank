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
  JButton submit, cancel;

    ClientLogin() {
        // User Label
        user_label = new JLabel();
        user_label.setText("User Name :");
        userName_text = new JTextField();

        // Password

        password_label = new JLabel();
        password_label.setText("Password :");
        password_text = new JPasswordField();

        // Submit

        submit = new JButton("SUBMIT");

        panel = new JPanel(new GridLayout(3, 1));

        panel.add(user_label);
        panel.add(userName_text);
        panel.add(password_label);
        panel.add(password_text);

        message = new JLabel();
        panel.add(message);
        panel.add(submit);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the listeners to components..
        submit.addActionListener(this);
        add(panel, BorderLayout.CENTER);
        setTitle("Please Login Here !");
        setSize(300, 100);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String userName = userName_text.getText();
        String password = password_text.getText();
        if (userName.trim().equals("admin") && password.trim().equals("admin"))
        {
            message.setText(" Hello " + userName + "");
            ClientScreen cs = new ClientScreen();
        }
        else
        {
            message.setText(" Invalid user.. ");
        }
        // Client c = new Client();
        // if (c.verify(taxId, pin)){
        //   ClientScreen cs = new ClientScreen();
        // }
        //
        // else {
        //   message.setText("AAAAAAA");
        // }
        // ClientScreen cs = new ClientScreen();

    }

}


//
//
// class ClientLogin {
//
//     ClientLogin() {
//         // Create Frame
//         JFrame frame = new JFrame("Welcome to the Client Login!");
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         frame.setSize(1024, 720);
//
//         // Define new buttons
//         JButton submitButton = new JButton("Submit");
//         JButton gobackButton = new JButton("Go Back");
//
//         JTextField textField = new JTextField("Enter Tax ID", 20);
//         JLabel taxidLabel = new JLabel("Enter Tax ID");
//
//
//         int button_height = 720 / 2;
//         int text_height = 720/4;
//         int left_x = 200;
//         int right_x = 600;
//         int padding = 150;
//
//         // Create the bounds of each button... x,y,width,height
//         taxidLabel.setBounds(200, 100, 200, 100);
//         submitButton.setBounds(left_x, button_height, 200, 100);
//         gobackButton.setBounds(right_x, button_height, 200, 100);
//
//
//
// 				submitButton.addActionListener((ActionListener) new ActionListener() {
// 					@Override
// 					public void actionPerformed(ActionEvent e){
// 						frame.getContentPane().removeAll();
// 						frame.repaint();
//             System.out.println("Before text field");
//             String text = textField.getText();
//             System.out.println("After text field");
//             ClientScreen CS = new ClientScreen();
// 					}
// 				});
//
//         //
//         // public void close() {
//         //   this.setVisible(false);
//         //   this.dispose();
//         // }
//
//
//         gobackButton.addActionListener((ActionListener) new ActionListener() {
// 					@Override
// 					public void actionPerformed(ActionEvent e){
// 						frame.getContentPane().removeAll();
// 						frame.repaint();
//             // ClientScreen cs = new ClientScreen();
//             // ClientLogin.close();
//             //   this.setVisible(false);
//             //   this.dispose();
//             startScreen s = new startScreen();
// 					}
// 				});
//
//
//
//         //Add buttons to the GUI
//         frame.add(submitButton);
//         frame.add(textField);
//         frame.add(gobackButton);
//         frame.add(taxidLabel);
//
//
//
//         // frame.setLayout(null);
//         frame.setVisible(true);
//
//     }
//
// }

// public static void main(String[] args) {
//     new ClientLogin();
// }


class Clientlogingui{

    public static void main(String args[]){
      ClientLogin CL = new ClientLogin();
    }
}
