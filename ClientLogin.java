import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class ClientLogin {

    ClientLogin() {
        // Create Frame
        JFrame frame = new JFrame("Welcome to the Client Login!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 720);

        // Define new buttons
        JButton submitButton = new JButton("Submit");
        JButton gobackButton = new JButton("Go Back");

        JTextField textField = new JTextField("Enter Tax ID", 20);
        JLabel taxidLabel = new JLabel("Enter Tax ID");


        int button_height = 720 / 2;
        int text_height = 720/4;
        int left_x = 200;
        int right_x = 600;
        int padding = 150;

        // Create the bounds of each button... x,y,width,height
        taxidLabel.setBounds(200, 100, 200, 100);
        submitButton.setBounds(left_x, button_height, 200, 100);
        gobackButton.setBounds(right_x, button_height, 200, 100);



				submitButton.addActionListener((ActionListener) new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e){
						frame.getContentPane().removeAll();
						frame.repaint();
            System.out.println("Before text field");
            String text = textField.getText();
            System.out.println("After text field");
            ClientScreen CS = new ClientScreen();
					}
				});

        //
        // public void close() {
        //   this.setVisible(false);
        //   this.dispose();
        // }


        gobackButton.addActionListener((ActionListener) new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e){
						frame.getContentPane().removeAll();
						frame.repaint();
            // ClientScreen cs = new ClientScreen();
            // ClientLogin.close();
            //   this.setVisible(false);
            //   this.dispose();
            startScreen s = new startScreen();
					}
				});



        //Add buttons to the GUI
        frame.add(submitButton);
        frame.add(textField);
        frame.add(gobackButton);
        frame.add(taxidLabel);



        // frame.setLayout(null);
        frame.setVisible(true);

    }

}

class Clientlogingui{

    public static void main(String args[]){
      ClientLogin CL = new ClientLogin();
    }
}
