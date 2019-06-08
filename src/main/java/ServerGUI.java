package src.main.java;

import javax.swing.*;

public class ServerGUI{
    private JPanel panel1;
    private JButton msg_send;
    private JTextArea msg_area;
    private JTextArea msg_text;



    public static void main(String[] args) {

        JFrame frame = new JFrame("Server");
        frame.setContentPane(new ServerGUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}


