package src.main.java.extendedChat.client;

import java.net.*;

public class Client {
    private static final long serialVersionUID = 1L;

    private DatagramSocket socket; //a socket is an analogy to the post office, so we use the socket to sendToServer our packets
    private int ID = -1;

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
            socket = new DatagramSocket(); //a socket is an analogy to the post office, so we use the socket to sendToServer our packets
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
        String msg = new String(packet.getData());
        return msg;
    }

    public void sendToServer(byte[] data) { //sends a packet to the server

        send = new Thread("Send") {
            public void run() {
                DatagramPacket packet = new DatagramPacket(data, data.length, serverIP, serverPort);  // a packet is an analogy to a letter
                try {
                    socket.send(packet); //a socket is an analogy to the post office, so we use the socket to sendToServer our packets
                } catch (java.io.IOException e) {
                }
            }
        };
        send.start();
    }

    public void close(){

        new Thread("Close"){
            public void run(){
                synchronized (socket){ //specyfikator synchronized daje nam gwarancję, że metoda w ten sposób oznaczona będzie mogła być wywołana przez jeden wątek w danym czasie
                    socket.close();
                }
            }
        }.start();

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

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
