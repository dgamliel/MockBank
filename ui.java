import javax.swing.*;
import java.awt.*;

class gui{

    public static void main(String args[]){

        //Create Frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024,720);


        //Define new buttons 
        JButton tellerButton = new JButton("Teller");
        JButton userButton   = new JButton("Client");

        int height = 720/3;

        int left_x  = 1024/3 - 170;    //Makes it look nice
        int right_x = 1024 - (1024/3); //Makes it look nice lol

        //Create the bounds of each button... x,y,width,height
        tellerButton.setBounds(left_x, height, 200, 200);
        userButton.setBounds(right_x, height, 200, 200);

        //Add buttons to the GUI
        frame.add(tellerButton);
        frame.add(userButton);

        frame.setLayout(null);
        frame.setVisible(true);

    }
}