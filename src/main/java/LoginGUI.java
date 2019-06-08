package src.main.java;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginGUI extends JFrame {

    private static final long serialVeersionUID = 1L;

    private JPanel contentPane; //nasz panel logowania
    private JTextField txtName; //pole przyjmujące nazwe usera
    JLabel lblName;

    public LoginGUI() { //konstruktor ustawia wartosci domyslne
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 380); //frame size
        setLocationRelativeTo(null); // If the component is null , the window is placed in the center of the screen
        contentPane = new JPanel(); //na panelu umiejscowiamy buttony i labele
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        contentPane.setLayout(new BorderLayout(0,0));
        setContentPane(contentPane);
        contentPane.setLayout(null); //ustawiamy absoluteLayout, wielkosc okna bedzie stała


        txtName = new JTextField();
        txtName.setBounds(67,50,165,28);
        contentPane.add(txtName);
     //   txtName.setColumns(10);

        lblName = new JLabel("Name:");
        lblName.setBounds(127, 34, 45, 16);
        contentPane.add(lblName);
    }

    public static void main(String[] args) {


        EventQueue.invokeLater(() -> {

            try {
                LoginGUI frame = new LoginGUI();
                frame.setVisible(true);
            } catch(Exception e){
                e.printStackTrace();
            }

        });
    }
}
