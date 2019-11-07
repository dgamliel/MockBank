import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class numPad{
    GridLayout grid;

    numPad() {
        grid = new GridLayout(3, 4, 10, 10);

        for (int i = 0; i < 10; i++) {
            JButton button = new JButton(Integer.toString(i));
        }
    }

    public GridLayout grid(){
        return this.grid;
    }
}

class startScreen {
    startScreen() {
        // Create Frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 720);

        // Define new buttons
        JButton tellerButton = new JButton("Teller");
        JButton userButton = new JButton("Client");

        int height = 720 / 3;

        int left_x = 1024 / 3 - 165; // Makes it look nice
        int right_x = 1024 - (1024 / 3); // Makes it look nice lol

        // Create the bounds of each button... x,y,width,height
        tellerButton.setBounds(left_x, height, 200, 200);
        userButton.setBounds(right_x, height, 200, 200);

        //Set grid to get pin
        userButton.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();

                JPanel pinPanel = new JPanel();
                JFormattedTextField pinTextField= new JFormattedTextField();

                pinPanel.add(pinTextField);

                frame.add(pinPanel);

                frame.revalidate();

            }
        });



        //Add buttons to the GUI
        frame.add(tellerButton);
        frame.add(userButton);

        frame.setLayout(null);
        frame.setVisible(true);

    }

}

class gui{

    public static void main(String args[]){
        startScreen s = new startScreen();
    }
}