package src.main.java.extendedChat.login;

import src.main.java.extendedChat.client.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginGUI extends JFrame {

    private static final long serialVeersionUID = 1L;

    //  LoginButtonHandler handle = new LoginButtonHandler(); //obiekt ten rozszerza interfejs ActionListener, obsługuje zdarzenie klikniecia w przycisk btnLogin

    private JPanel contentPane; //nasz panel logowania
    private JTextField txtName; //pole przyjmujące nazwe usera
    private JLabel lblName;
    private JTextField txtIPAddress;
    private JLabel lblIPAddress;
    private JLabel lblIPAddressHint;
    private JTextField txtPort;
    private JLabel lblPort;
    private JLabel lblPortHint;
    private final Color FOREGROUND_COLOR = new Color(255, 255, 255);

    private JButton btnLogin;

    public LoginGUI() { //konstruktor ustawia wartosci domyslne

        createWindow();
    }

    private void createWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  //  ustawioamy wygląd GUI (Look&Feel) zgodny z systemem operac. zytkownika
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        setTitle("Login"); //frame name
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //frame [x] close operation
        setSize(300, 400); //frame size
        setLocationRelativeTo(null); // If the component is null , the window is placed in the center of the screen

        contentPane = new JPanel(); //na panelu umiejscowiamy buttony i labele
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.setBackground(new Color(71, 89, 135));

        setContentPane(contentPane);
        //  add(clientPane); //alternative way to add this child to the JFrame

        contentPane.setLayout(null); //ustawiamy absoluteLayout - for any window, that does not need to be resizeable

        txtName = new JTextField("");
        txtName.setBounds(65, 50, 160, 33);
        setFont(txtName);
        contentPane.add(txtName);
        lblName = new JLabel("Client name:");
        lblName.setBounds(95, 32, 100, 18);
        setFont(lblName, FOREGROUND_COLOR);
        contentPane.add(lblName);
        lblName.setHorizontalAlignment(SwingConstants.CENTER);

        txtIPAddress = new JTextField("localhost");
        txtIPAddress.setBounds(65, 126, 160, 33);
        setFont(txtIPAddress);
        contentPane.add(txtIPAddress);
        lblIPAddress = new JLabel("Server IP Address:");
        lblIPAddress.setBounds(95, 108, 100, 18);
        lblIPAddress.setHorizontalAlignment(SwingConstants.CENTER);
        setFont(lblIPAddress, FOREGROUND_COLOR);
        contentPane.add(lblIPAddress);
        txtIPAddress.setColumns(10);
        lblIPAddressHint = new JLabel("(ex. 192.168.0.1)");
        lblIPAddressHint.setBounds(95, 160, 100, 16);
        lblIPAddressHint.setHorizontalAlignment(SwingConstants.CENTER);
        setFont(lblIPAddressHint, FOREGROUND_COLOR);
        contentPane.add(lblIPAddressHint);

        txtPort = new JTextField("8012");
        txtPort.setBounds(65, 210, 160, 33);
        setFont(txtPort);
        contentPane.add(txtPort);
        lblPort = new JLabel("Server port:");
        lblPort.setBounds(95, 192, 100, 18);
        lblPort.setHorizontalAlignment(SwingConstants.CENTER);
        setFont(lblPort, FOREGROUND_COLOR);
        contentPane.add(lblPort);
        lblPortHint = new JLabel(("(ex. 1234)"));
        lblPortHint.setBounds(95, 240, 100, 18);
        lblPortHint.setHorizontalAlignment(SwingConstants.CENTER);
        setFont(lblPortHint, FOREGROUND_COLOR);
        contentPane.add(lblPortHint);

        btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> {
            String name = txtName.getText();
            String iPAddress = txtIPAddress.getText();
            int port = Integer.parseInt(txtPort.getText());
            login(name, iPAddress, port);
        });

        btnLogin.setBounds(105, 300, 80, 33);
        contentPane.add(btnLogin);

    }

    private void setFont(JComponent component) {
        component.setFont(new Font("Consolas", Font.PLAIN, 16)); //style: 0=plain, 1=bold, 2= italic
    }

    private void setFont(JComponent component, Color color) {
        component.setForeground(color);
    }

    private void login(String name, String address, int port) {
        dispose(); //zamyka (niszczy) JFrame
        new ClientGUI(name, address, port);
    }

    public static void main(String[] args) {


        EventQueue.invokeLater(() -> {

            try {
                LoginGUI frame = new LoginGUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
}
