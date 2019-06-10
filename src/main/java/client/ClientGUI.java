package src.main.java.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ClientGUI extends JFrame {
    private static final long serialVersionUID = 1L;


    JPanel clientPane;
    JTextArea txtHistory;
    JTextField txtMessage;
    JButton btnSend;
    private String name;
    private String iPAddress;
    private int port;

    public ClientGUI(String name, String iPAddress, int port) {
        this.name = name;
        this.iPAddress = iPAddress;
        this.port = port;
        createWindow();
        console("Attempting a connection to " + iPAddress + ":" + port + ", user: " + name);
    }


     private void createWindow(){
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
         clientPane.setLayout(new BorderLayout(0,0));
         clientPane.setBackground(new Color(71,89,135));
         setContentPane(clientPane);
         //  add(clientPane); //alternative way to add this child to the LFrame
         GridBagLayout gblContentPane = new GridBagLayout(); //tworzymy siatke o wymiarach 4 x 3
         gblContentPane.columnWidths = new int[]{28, 815,30, 7}; //cztery kolumny o indeksach 0..3
         gblContentPane.rowHeights = new int[] {35, 475, 40}; //trzy wiersze o indeksach 0..2
         gblContentPane.columnWeights = new double[]{1.0, 1.0};
         gblContentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
         clientPane.setLayout(gblContentPane);

         txtHistory = new JTextArea();
         txtHistory.setEditable(false);
         GridBagConstraints gblTxtHistory = new GridBagConstraints();
         gblTxtHistory.fill = GridBagConstraints.BOTH;
         gblTxtHistory.gridx = 1; //obiekt txtHistory zostaje wrzucony do kolumny 2 (index 1)
         gblTxtHistory.gridy = 1; //obiekt txtHistory zostaje wrzucony do wiersza 2 (index 1)
         gblTxtHistory.gridwidth = 2;
         gblTxtHistory.insets = new Insets(15,5,0, 0);
         setFont(txtHistory);
         clientPane.add(txtHistory, gblTxtHistory);

         txtMessage = new JTextField();
         GridBagConstraints gbcTxtMessage = new GridBagConstraints();
         gbcTxtMessage.insets = new Insets(0,5,0,15);
         gbcTxtMessage.fill = GridBagConstraints.HORIZONTAL;
         gbcTxtMessage.gridx = 1; //obiekt txtMessage zostaje wrzucony do kolumny 2 (index 1)
         gbcTxtMessage.gridy = 2; //obiekt txtMessage zostaje wrzucony do wiersza 3 (index 2)
         setFont(txtMessage);
         txtMessage.addKeyListener(new KeyAdapter() {
             @Override
             public void keyPressed(KeyEvent e) {
                 if (e.getKeyCode() == KeyEvent.VK_ENTER){
                     sent(txtMessage.getText());
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
                sent(txtMessage.getText());
             }
         });
         gbcBtnSent.insets = new Insets(0,0,0,0);
         gbcBtnSent.gridx = 2; //obiekt btnSend zostaje wrzucony do kolumny 3 (index 2)
         gbcBtnSent.gridy = 2; //obiekt btnSend zostaje wrzucony do wiersza 3 (index 2)
         clientPane.add(btnSend, gbcBtnSent);

         setVisible(true);
         txtMessage.requestFocusInWindow(); //kursor automatycznie zostaje umieszczony w polu txtMessage

     }

    private void setFont(JTextComponent component)
    {
        component.setFont(new Font("Consolas", Font.PLAIN, 16 )); //style: 0=plain, 1=bold, 2= italic
    }

     private void sent(String msg){

        if (msg.equals(""))
        {return;}
         msg = name + ": " + msg;
         console(msg);
         txtMessage.setText("");
     }

     public void console(String message){
        txtHistory.append(message + "\n");
     }

}
