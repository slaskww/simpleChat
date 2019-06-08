package src.main.java.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ClientGUI extends JFrame {
    private static final long serialVersionUID = 1L;


    JPanel clientPane;
    private String name;
    private String iPAddress;
    private int port;

    public ClientGUI(String name, String iPAddress, int port) {
        this.name = name;
        this.iPAddress = iPAddress;
        this.port = port;
        createWindow();
    }


     private void createWindow(){
         setTitle("Chat Client");
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setSize(880, 550);
         setLocationRelativeTo(null); /// If the component is null , the window is placed in the center of the screen
         clientPane = new JPanel();
         clientPane.setBorder(new EmptyBorder(5, 5, 5, 5));
         clientPane.setLayout(new BorderLayout(0,0));
         setContentPane(clientPane);
       //  add(clientPane); //alternative way to add this child to the JFrame



         setVisible(true);

     }

}
