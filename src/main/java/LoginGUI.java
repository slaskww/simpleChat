package src.main.java;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginGUI extends JFrame {

    private static final long serialVeersionUID = 1L;

    private JPanel contentPane; //nasz panel logowania
    private JTextField txtName; //pole przyjmujące nazwe usera
    private JLabel lblName;
    private JTextField txtIPAddress;
    private JLabel lblIPAddress;
    private JLabel lblIPAddressHint;
    private JTextField txtPort;
    private JLabel lblPort;
    private JLabel lblPortHint;

    private JButton btnLogin;

    public LoginGUI() { //konstruktor ustawia wartosci domyslne
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400); //frame size
        setLocationRelativeTo(null); // If the component is null , the window is placed in the center of the screen
        contentPane = new JPanel(); //na panelu umiejscowiamy buttony i labele
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        contentPane.setLayout(new BorderLayout(0,0));
        setContentPane(contentPane);
        contentPane.setLayout(null); //ustawiamy absoluteLayout, wielkosc okna bedzie stała


        txtName = new JTextField();
        txtName.setBounds(70,50,160,33);
        contentPane.add(txtName);
     //   txtName.setColumns(10);
        lblName = new JLabel("Name:");
        lblName.setBounds(125, 32, 40, 18);
        contentPane.add(lblName);

        txtIPAddress = new JTextField();
        txtIPAddress.setBounds(70,126,160,33);
        contentPane.add(txtIPAddress);
        //   txtName.setColumns(10);
        lblIPAddress = new JLabel("IP Address:");
        lblIPAddress.setBounds(112, 108, 80, 18);
        contentPane.add(lblIPAddress);
        txtIPAddress.setColumns(10);
        lblIPAddressHint = new JLabel("(ex. 192.168.0.1)");
        lblIPAddressHint.setBounds(85, 160,120, 16 );
        lblIPAddressHint.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblIPAddressHint);

        txtPort = new JTextField();
        txtPort.setBounds(70,210,160,33);
        contentPane.add(txtPort);
        lblPort = new JLabel("Port:");
        lblPort.setBounds(129, 192, 80, 18);
        contentPane.add(lblPort);
        lblPortHint = new JLabel(("(ex. 1234)"));
        lblPortHint.setBounds(120, 240, 80, 18);
        contentPane.add(lblPortHint);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(105, 300, 80, 33);
        contentPane.add(btnLogin);

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
