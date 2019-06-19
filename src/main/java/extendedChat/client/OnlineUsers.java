package src.main.java.extendedChat.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OnlineUsers extends JFrame {

    private JPanel contentPane;
    private JList list;

    public OnlineUsers(){

        setType(Type.UTILITY);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(200, 320);
        setTitle("Connected users");
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 8, 8, 8));
        setContentPane(contentPane);

        GridBagLayout gridBagLayoutContentPane = new GridBagLayout();
        gridBagLayoutContentPane.columnWidths = new int[]{0,0};
        gridBagLayoutContentPane.rowHeights = new int[]{0,0};
        gridBagLayoutContentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gridBagLayoutContentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        contentPane.setLayout(gridBagLayoutContentPane);
        contentPane.setBackground(new Color(71, 89, 135));

        list = new JList();
        GridBagConstraints gridBagConstraintsList = new GridBagConstraints();
        gridBagConstraintsList.fill = GridBagConstraints.BOTH;
        gridBagConstraintsList.gridx = 0;
        gridBagConstraintsList.gridy = 0;
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(list);
        contentPane.add(scroll, gridBagConstraintsList);
        list.setFont(new Font("Verdana", 0, 18));
    }

    public void update(String[] users){
        list.setListData(users);
    }

}
