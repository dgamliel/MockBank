package cs174a;
import cs174a.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class BankTellerScreen {
    BankTellerScreen(JFrame mainFrame, App app) {
        // Create Frame
        // JFrame frame = new JFrame("Welcome to the Bank Teller Portal!");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(1024, 720);
        JPanel panel = new JPanel();

        // Define new buttons
        JButton monthlystatementButton = new JButton("Monthly Statement");
        JButton closedaccountButton = new JButton("Closed Account");
        JButton dterButton = new JButton("DTER");
        JButton customerreportButton = new JButton("Customer Report");
        JButton addinterestButton = new JButton("Add Interest");
        JButton createaccountButton = new JButton("Create Account");
        JButton deleteaccountButton = new JButton("Delete Account");
        JButton deletetransactionButton = new JButton("Delete Transaction");
	JButton goBackButton = new JButton("Go Back");


        int height = 720 / 8;
        int left_x = 200;
        int right_x = 600;
        int padding = 150;

        // Create the bounds of each button... x,y,width,height
        monthlystatementButton.setBounds(left_x, height, 200, 100);
        closedaccountButton.setBounds(left_x, height + padding, 200, 100);
        dterButton.setBounds(left_x, height + padding*2, 200, 100);
        customerreportButton.setBounds(left_x, height + padding*3, 200, 100);

        addinterestButton.setBounds(right_x, height, 200, 100);
        createaccountButton.setBounds(right_x, height + padding, 200, 100);
        deleteaccountButton.setBounds(right_x, height + padding*2, 200, 100);
        deletetransactionButton.setBounds(right_x, height + padding*3, 200, 100);

	monthlystatementButton.addActionListener((ActionListener) new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e){
			mainFrame.getContentPane().removeAll();
			mainFrame.repaint();
			app.GenerateMonthlyStatement();
      // System.out.println(returnValue);
      BankTellerScreen bts = new BankTellerScreen(mainFrame, app);
		}
	});

        closedaccountButton.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.getContentPane().removeAll();
    		mainFrame.repaint();
		AccountManagement am = new AccountManagement(app, 2, mainFrame);
            }
        });

        dterButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
	          app.DTER();
	    // System.out.println(returnValue);
	    BankTellerScreen bts = new BankTellerScreen(mainFrame, app);
          }
        });

        customerreportButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){

						String accId = "";

            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
	    			app.GenerateCustomerReport(accId);
            // System.out.println(returnValue);
            BankTellerScreen bts = new BankTellerScreen(mainFrame, app);
          }
        });

        addinterestButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
	          app.AddInterest();
            // System.out.println(returnValue);
            BankTellerScreen bts = new BankTellerScreen(mainFrame, app);
          }
        });

        createaccountButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
	          AccountManagement am = new AccountManagement(app, 1, mainFrame);
          }
        });

        deleteaccountButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
	          AccountManagement am = new AccountManagement(app, 3, mainFrame);
          }
        });

        deletetransactionButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
          }
        });

        goBackButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
	          StartScreen ss = new StartScreen(app);
          }
        });

        //Add buttons to the GUI
        // frame.add(monthlystatementButton);
        // frame.add(closedaccountButton);
        // frame.add(dterButton);
        // frame.add(customerreportButton);
        // frame.add(addinterestButton);
        // frame.add(createaccountButton);
        // frame.add(deleteaccountButton);
        // frame.add(deletetransactionButton);
        panel.add(monthlystatementButton);
        panel.add(closedaccountButton);
        panel.add(dterButton);
        panel.add(customerreportButton);
        panel.add(addinterestButton);
        panel.add(createaccountButton);
        panel.add(deleteaccountButton);
        panel.add(deletetransactionButton);
	panel.add(goBackButton);

        mainFrame.add(panel);
        mainFrame.show();

        // frame.setLayout(null);
        // frame.setVisible(true);

    }

}
