package src.main.java.client;

import javax.swing.*;
import java.net.*;
import java.util.Arrays;

public class Client {
    private static final long serialVersionUID = 1L;

    private DatagramSocket socket; //a socket is an analogy to the post office, so we use the socket to send our packets

    private String name;
    private String serverIPAddress;
    private int serverPort;
    private InetAddress serverIP;
    private Thread send;


    public Client(String name, String iPAddress, int port) {
        this.name = name;
        this.serverIPAddress = iPAddress;
        this.serverPort = port;

    }

    public boolean openConnection(String address) {

        try {
            socket = new DatagramSocket(); //a socket is an analogy to the post office, so we use the socket to send our packets
            serverIP = InetAddress.getByName(address);
        } catch (SocketException | UnknownHostException e) {
            return false;
        }

        return true;
    }

    public String receive() {

        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length); //received a packet of data

        try {
            socket.receive(packet); //this method freezes our application (one thread) until our socket gets some data from the network ( receive() uses an infinite while loop)
        } catch (java.io.IOException e) {

        }
        return Arrays.toString(packet.getData());
    }

    public void send(byte[] data) {

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


    public String getClientName() {
        return name;
    }

    public String getServerIPAddress() {
        return serverIPAddress;
    }

    public int getServerPort() {
        return serverPort;
    }
}
