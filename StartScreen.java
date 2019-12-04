package cs174a;
import cs174a.*;
//import cs174a.BankTellerScreen;
//import cs174a.ClientScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class StartScreen {
    StartScreen(App app) {
        // Create Frame
        JFrame mainFrame = new JFrame();
        JPanel panel = new JPanel();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1024, 720);

        // Define new buttons
        JButton tellerButton = new JButton("Teller");
        JButton clientButton = new JButton("Client");
        JButton exitButton = new JButton("Exit Program");

        int height = 720 / 3;

        // int left_x = 1024 / 3 - 165; // Makes it look nice
        // int right_x = 1024 - (1024 / 4); // Makes it look nice lol
        // int far_right = 1024 - (1024/ 9);

        int left_x = 200;
        int middle_x = 400;
        int right_x = 600;

        // Create the bounds of each button... x,y,width,height
        tellerButton.setBounds(left_x, height, 200, 200);
        clientButton.setBounds(middle_x, height, 200, 200);
        exitButton.setBounds(right_x, height, 200, 200);

		tellerButton.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				mainFrame.getContentPane().removeAll();
				mainFrame.repaint();
                BankTellerScreen BT = new BankTellerScreen(mainFrame, app);
            // cs.setVisible(true);
					}
				});

        //Set grid to get pin
        clientButton.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.getContentPane().removeAll();
                mainFrame.repaint();
                ClientLogin CL = new ClientLogin(mainFrame, app);
							  // ClientScreen CS = new ClientScreen();

            }
        });

        exitButton.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.getContentPane().removeAll();
                System.out.println("Ending program");
                System.exit(0);

            }
        });

        //Add buttons to the GUI
        // frame.add(tellerButton);
        // frame.add(clientButton);
        // frame.add(exitButton);

        panel.add(tellerButton);
        panel.add(clientButton);
        panel.add(exitButton);
        mainFrame.add(panel);
        mainFrame.show();

        // frame.setLayout(null);
        // frame.setVisible(true);

    }

}
