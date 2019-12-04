package cs174a;
import cs174a.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class ClientScreen {
    ClientScreen(App app, JFrame mainFrame ) {
        // Create Frame
        // JFrame frame = new JFrame("Welcome to the Client Portal!");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(1024, 720);
        JPanel panel = new JPanel();

        // Define new buttons
        JButton wireButton = new JButton("Wire");
        JButton purchaseButton = new JButton("Purchase");
        JButton withdrawButton = new JButton("Withdraw");
        JButton topupButton = new JButton("Top Up");
        JButton depositButton = new JButton("Deposit");
        JButton transferButton = new JButton("Transfer");
        JButton payfriendButton = new JButton("Pay Friend");
        JButton collectButton = new JButton("Collect");
	JButton goBackButton = new JButton("Go Back");	


        int height = 720 / 8;
        int left_x = 200;
        int right_x = 600;
        int padding = 150;

        // Create the bounds of each button... x,y,width,height
        wireButton.setBounds(left_x, height, 200, 100);
        purchaseButton.setBounds(left_x, height + padding, 200, 100);
        withdrawButton.setBounds(left_x, height + padding*2, 200, 100);
        topupButton.setBounds(left_x, height + padding*3, 200, 100);

        depositButton.setBounds(right_x, height, 200, 100);
        transferButton.setBounds(right_x, height + padding, 200, 100);
        payfriendButton.setBounds(right_x, height + padding*2, 200, 100);
        collectButton.setBounds(right_x, height + padding*3, 200, 100);

	wireButton.addActionListener((ActionListener) new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e){
			mainFrame.getContentPane().removeAll();
			mainFrame.repaint();
			AdvancedOperation so = new AdvancedOperation(app, 8, mainFrame);
		}
	});

        purchaseButton.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.getContentPane().removeAll();
    		mainFrame.repaint();
		SimpleOperation so = new SimpleOperation(app, 2, mainFrame);
            }
        });

        withdrawButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
	    SimpleOperation so = new SimpleOperation(app, 3, mainFrame);
          }
        });

        topupButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
	    AdvancedOperation so = new AdvancedOperation(app, 4, mainFrame);
          }
        });

        depositButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
	    SimpleOperation so = new SimpleOperation(app, 1, mainFrame);
          }
        });

        transferButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
	    AdvancedOperation so = new AdvancedOperation(app, 6, mainFrame);
          }
        });

        payfriendButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
	    AdvancedOperation so = new AdvancedOperation(app, 7, mainFrame);
          }
        });

        collectButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
	    AdvancedOperation so = new AdvancedOperation(app, 5, mainFrame);
          }
        });

        goBackButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
	    StartScreen ss  = new StartScreen(app);
          }
        });
        //Add buttons to the GUI
        // frame.add(purchaseButton);
        // frame.add(withdrawButton);
        // frame.add(wireButton);
        // frame.add(topupButton);
        // frame.add(depositButton);
        // frame.add(transferButton);
        // frame.add(payfriendButton);
        // frame.add(collectButton);
        panel.add(purchaseButton);
        panel.add(withdrawButton);
        panel.add(wireButton);
        panel.add(topupButton);
        panel.add(depositButton);
        panel.add(transferButton);
        panel.add(payfriendButton);
        panel.add(collectButton);
	panel.add(goBackButton);

        mainFrame.add(panel);
        mainFrame.show();
        // frame.setLayout(null);
        // frame.setVisible(true);

    }

}
