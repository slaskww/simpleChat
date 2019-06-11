package src.main.java.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.*;
import java.util.Arrays;

public class ClientGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private DatagramSocket socket; //a socket is an analogy to the post office, so we use the socket to send our packets

    private String name;
    private String serverIPAddress;
    private int serverPort;
    private InetAddress serverIP;
    private Thread send;

    private JPanel clientPane;
    private JTextArea history;
    private JTextField txtMessage;
    private JButton btnSend;
    // private DefaultCaret caret;

    public ClientGUI(String name, String iPAddress, int port) {
        this.name = name;
        this.serverIPAddress = iPAddress;
        this.serverPort = port;

        boolean isConnected = openConnection(iPAddress);
        if (!isConnected) {

            console("Connection failed!");
        }

        createWindow();
        console("Attempting a connection to " + iPAddress + ":" + port + ", user: " + name);
        String connectionInfo = "/c/" + name + " connected from " + iPAddress + ":" +  port;
        send(connectionInfo.getBytes());
    }


    private boolean openConnection(String address) {

        try {
            socket = new DatagramSocket(); //a socket is an analogy to the post office, so we use the socket to send our packets
            serverIP = InetAddress.getByName(address);
        } catch (SocketException | UnknownHostException e) {
            return false;
        }

        return true;
    }

    private String receive() {

        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length); //received a packet of data

        try {
            socket.receive(packet); //this method freezes our application (one thread) until our socket gets some data from the network ( receive() uses an infinite while loop)
        } catch (java.io.IOException e) {

        }
        return Arrays.toString(packet.getData());
    }

    private void send(byte[] data) {

        send = new Thread("Send") {
            public void run() {
                DatagramPacket packet = new DatagramPacket(data, data.length, serverIP, serverPort);  // a packet is an analogy to a letter
                try {
                    socket.send(packet); //a socket is an analogy to the post office, so we use the socket to send our packets
                } catch (java.io.IOException e) {
                }
            }
        };
        send.start();
    }

    private void createWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  //  ustawioamy wygląd GUI (Look&Feel) zgodny z systemem operac. zytkownika
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(880, 550);
        setLocationRelativeTo(null); /// If the component is null , the window is placed in the center of the screen
        clientPane = new JPanel();
        clientPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        clientPane.setLayout(new BorderLayout(0, 0));
        clientPane.setBackground(new Color(71, 89, 135));
        setContentPane(clientPane);
        //  add(clientPane); //alternative way to add this child to the LFrame
        GridBagLayout gblContentPane = new GridBagLayout(); //tworzymy siatke o wymiarach 4 x 3
        gblContentPane.columnWidths = new int[]{28, 815, 30, 7}; //cztery kolumny o indeksach 0..3
        gblContentPane.rowHeights = new int[]{35, 475, 40}; //trzy wiersze o indeksach 0..2
        gblContentPane.columnWeights = new double[]{1.0, 1.0};
        gblContentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        clientPane.setLayout(gblContentPane);

        history = new JTextArea();
        history.setEditable(false);
        //caret = (DefaultCaret) history.getCaret();
        //caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollPane = new JScrollPane(history); //we put the history area inside the scroll pane
        GridBagConstraints scrollConstraints = new GridBagConstraints();
        scrollConstraints.fill = GridBagConstraints.BOTH;
        scrollConstraints.gridx = 0; //obiekt history zostaje wrzucony do kolumny 1 (index 0)
        scrollConstraints.gridy = 0; //obiekt history zostaje wrzucony do wiersza 1 (index 0)
        scrollConstraints.gridwidth = 3; //obiekt bedzie szeroki na 3 kolumny
        scrollConstraints.gridheight = 2; //obiekt bedzie wysoki na 2 wiersze
        scrollConstraints.insets = new Insets(15, 5, 0, 0);
        setFont(history);
        clientPane.add(scrollPane, scrollConstraints);

        txtMessage = new JTextField();
        GridBagConstraints gbcTxtMessage = new GridBagConstraints();
        gbcTxtMessage.insets = new Insets(0, 5, 0, 15);
        gbcTxtMessage.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtMessage.gridx = 0; //obiekt txtMessage zostaje wrzucony do kolumny 1 (index 0)
        gbcTxtMessage.gridy = 2; //obiekt txtMessage zostaje wrzucony do wiersza 3 (index 2)
        gbcTxtMessage.gridwidth = 2; //obiekt bedzie szeroki na dwie kolumny
        setFont(txtMessage);
        txtMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendToConsole(txtMessage.getText());
                }
            }
        });
        clientPane.add(txtMessage, gbcTxtMessage);
        txtMessage.setColumns(10);

        btnSend = new JButton("Send");
        GridBagConstraints gbcBtnSent = new GridBagConstraints();
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendToConsole(txtMessage.getText());
            }
        });
        gbcBtnSent.insets = new Insets(0, 0, 0, 0);
        gbcBtnSent.gridx = 2; //obiekt btnSend zostaje wrzucony do kolumny 3 (index 2)
        gbcBtnSent.gridy = 2; //obiekt btnSend zostaje wrzucony do wiersza 3 (index 2)
        clientPane.add(btnSend, gbcBtnSent);

        setVisible(true);
        txtMessage.requestFocusInWindow(); //kursor automatycznie zostaje umieszczony w polu txtMessage

    }

    private void setFont(JTextComponent component) {
        component.setFont(new Font("Consolas", Font.PLAIN, 16)); //style: 0=plain, 1=bold, 2= italic
    }

    private void sendToConsole(String msg) {

        if (msg.equals("")) {
            return;
        }
        msg = name + ":" + msg;
        console(msg);
        msg = "/m/" + msg;
        send(msg.getBytes()); //we send our message to the server
        txtMessage.setText("");
    }

    public void console(String message) {
        history.append(message + "\n");
        history.setCaretPosition(history.getDocument().getLength()); //ustawia karetę (kursor) na końcu bloku tekstu, przydatne, jesli zeskrollujemy tekst i dodamy nową linie tekstu.
        // Karetka automatycznie przejsdzie na koniec bloku tekstu w history
    }


}
