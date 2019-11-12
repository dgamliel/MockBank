import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class startScreen {
    startScreen() {
        // Create Frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 720);

        // Define new buttons
        JButton tellerButton = new JButton("Teller");
        JButton clientButton = new JButton("Client");

        int height = 720 / 3;

        int left_x = 1024 / 3 - 165; // Makes it look nice
        int right_x = 1024 - (1024 / 3); // Makes it look nice lol

        // Create the bounds of each button... x,y,width,height
        tellerButton.setBounds(left_x, height, 200, 200);
        clientButton.setBounds(right_x, height, 200, 200);

				tellerButton.addActionListener((ActionListener) new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e){
						frame.getContentPane().removeAll();
						frame.repaint();
            BankTellerScreen BT = new BankTellerScreen();
            // cs.setVisible(true);
					}
				});

        //Set grid to get pin
        clientButton.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                // ClientLogin CL = new ClientLogin();
							ClientScreen CS = new ClientScreen();

            }
        });

        //Add buttons to the GUI
        frame.add(tellerButton);
        frame.add(clientButton);

        frame.setLayout(null);
        frame.setVisible(true);

    }

}

class gui{

    public static void main(String args[]){
      startScreen s = new startScreen();
    }
}
