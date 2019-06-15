package src.main.java.extendedChat.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientGUI extends JFrame implements Runnable{
    private static final long serialVersionUID = 1L;


    private JPanel clientPane;
    private JTextArea history;
    private JTextField txtMessage;
    private JButton btnSend;
    // private DefaultCaret caret;

    private Client client;
    private Thread listen, run;
    private boolean isRunning = false;



    public ClientGUI(String name, String iPAddress, int port) {

        client = new Client(name, iPAddress, port);

        boolean isConnected = client.openConnection(iPAddress);
        if (!isConnected) {

            console("Connection failed!");
        }

        createWindow();
        console("Attempting a connection to " + iPAddress + ":" + port + ", user: " + name);
        String connectionInfo = "/c/" + name + "/e/"; //message with the prefix '/c/'  is interpreted as a connecting message
        send(connectionInfo, false); //send the packet to the server
        run = new Thread(this, "Running");
        run.start();
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
                    send(txtMessage.getText(), true);
                }
            }
        });

        clientPane.add(txtMessage, gbcTxtMessage);
        txtMessage.setColumns(10);

        btnSend = new JButton("Send");
        GridBagConstraints gbcBtnSent = new GridBagConstraints();
        btnSend.addActionListener(e -> send(txtMessage.getText(), true));
        gbcBtnSent.insets = new Insets(0, 0, 0, 0);
        gbcBtnSent.gridx = 2; //obiekt btnSend zostaje wrzucony do kolumny 3 (index 2)
        gbcBtnSent.gridy = 2; //obiekt btnSend zostaje wrzucony do wiersza 3 (index 2)
        clientPane.add(btnSend, gbcBtnSent);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                String disconnect = "/d/" + client.getID() + "/e/";
                send(disconnect, false);
                isRunning = false;
                client.close();
            }
        });

        setVisible(true);
        txtMessage.requestFocusInWindow(); //kursor automatycznie zostaje umieszczony w polu txtMessage

    }

    private void setFont(JTextComponent component) {
        component.setFont(new Font("Consolas", Font.PLAIN, 16)); //style: 0=plain, 1=bold, 2= italic
    }

    private void send(String msg, boolean isMessageOrdinary) {

        if (msg.equals("")) {
            return;
        }
        if (isMessageOrdinary){
            msg = "/m/" +  client.getClientName() + ":" + msg; //message with the '/m/' prefix is interpreted as a regular message
            txtMessage.setText("");
        }
        client.sendToServer(msg.getBytes()); //we send our message to the server
    }

    public void console(String message) {
        history.append(message + "\n");
        history.setCaretPosition(history.getDocument().getLength()); //ustawia karetę (kursor) na końcu bloku tekstu, przydatne, jesli zeskrollujemy tekst i dodamy nową linie tekstu.
        // Karetka automatycznie przejsdzie na koniec bloku tekstu w history
    }

    public void listen(){

        listen = new Thread("Listen"){
            public void run(){
                while(isRunning){
                 String msg = client.receive();
                 if (msg.startsWith("/c/")){
                     Integer ID = Integer.parseInt(msg.split("/c/|/e/")[1]);
                   //  Integer ID = Integer.parseInt(msg.substring(3, 7));
                     client.setID(ID);
                     console("Successfully connected to server. ID: " + client.getID());
                    } else if (msg.startsWith("/m/")){
                        String text = msg.substring(3);
                        text = text.split("/e/")[0];

                        console(text);
                    } else if (msg.startsWith("/i/")){
                     String text = "/i/" +  client.getID() + "/e/";
                     send(text, false);
                 }

                }
            }
        };
        listen.start();

    }

    @Override
    public void run() { //this method is called in separately executing thread. It is activated in the constructor

        isRunning = true;
        listen();
    }
}
