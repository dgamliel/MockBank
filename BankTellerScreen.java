import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class BankTellerScreen {
    BankTellerScreen() {
        // Create Frame
        JFrame frame = new JFrame("Welcome to the Bank Teller Portal!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 720);

        // Define new buttons
        JButton monthlystatementButton = new JButton("Monthly Statement");
        JButton closedaccountButton = new JButton("Closed Account");
        JButton dterButton = new JButton("DTER");
        JButton customerreportButton = new JButton("Customer Report");
        JButton addinterestButton = new JButton("Add Interest");
        JButton createaccountButton = new JButton("Create Account");
        JButton deleteaccountButton = new JButton("Delete Account");
        JButton deletetransactionButton = new JButton("Delete Transaction");


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
						frame.getContentPane().removeAll();
						frame.repaint();
					}
				});

        closedaccountButton.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
    						frame.repaint();
            }
        });

        dterButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            frame.getContentPane().removeAll();
            frame.repaint();
          }
        });

        customerreportButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            frame.getContentPane().removeAll();
            frame.repaint();
          }
        });

        addinterestButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            frame.getContentPane().removeAll();
            frame.repaint();
          }
        });

        createaccountButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            frame.getContentPane().removeAll();
            frame.repaint();
          }
        });

        deleteaccountButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            frame.getContentPane().removeAll();
            frame.repaint();
          }
        });

        deletetransactionButton.addActionListener((ActionListener) new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            frame.getContentPane().removeAll();
            frame.repaint();
          }
        });

        //Add buttons to the GUI
        frame.add(monthlystatementButton);
        frame.add(closedaccountButton);
        frame.add(dterButton);
        frame.add(customerreportButton);
        frame.add(addinterestButton);
        frame.add(createaccountButton);
        frame.add(deleteaccountButton);
        frame.add(deletetransactionButton);


        frame.setLayout(null);
        frame.setVisible(true);

    }

}

class BankTellergui{

    public static void main(String args[]){
      BankTellerScreen BTS = new BankTellerScreen();
    }
}
