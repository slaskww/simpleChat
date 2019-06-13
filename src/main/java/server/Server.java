package src.main.java.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {

    private List<ServerClient> clients = new ArrayList<>();

    private DatagramSocket socket; //a socket is an analogy to the post office, so we use the socket to sendToServer our packets
    private int port;
    private boolean isRunning = false;
    private Thread run, manage, receive, send;

    public Server(int port) {
        this.port = port;
        try {
            socket = new DatagramSocket(port);
        } catch (java.net.SocketException e) {
        }

        run = new Thread(this, "Server");
        run.start();
    }

    @Override
    public void run() { //this method is called in separately executing thread. It is activated in the constructor

        isRunning = true;
        System.out.println("Server started on port " + port);
        manageClients();
        receive();
    }

    private void manageClients() {
        manage = new Thread("Manage") {
            public void run() {

                while (isRunning) {
                    //managing
                }
            }
        };
        manage.start();
    }


    private void receive() {
        receive = new Thread("Receive") {
            public void run() {
                while (isRunning) {
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    try {
                        socket.receive(packet);
                    } catch (java.io.IOException e) {

                    }
                    process(packet);
                }
            }
        };
        receive.start();
    }

    private void sendToAll(String message) {
        for (int i = 0; i < clients.size(); i++) {
            ServerClient client = clients.get(i);
            send(message.getBytes(), client.clientIPAddress, client.clientPort);
        }
    }

    private void send(byte[] data, InetAddress address, int port) {
        send = new Thread("Send") {
            public void run() {
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port); // a packet is an analogy to a letter
                try {
                    socket.send(packet); //a socket is an analogy to the post office, so we use the socket like the post office to sendToServer our packets
                } catch (java.io.IOException e) {

                }
            }
        };
        send.start();
    }

    private void addPostfixAndSend(String msg, InetAddress address, int port){
        msg += "/e/";
        send(msg.getBytes(), address, port);

    }

    private void process(DatagramPacket packet) { //1. get the packet and process it depending on the prefix, 2. (optional) add a new client to the list and sendToServer him a confirmation 3. (opt.) sendToServer data from the packet to the clients from the list
        String string = new String(packet.getData());

        if (string.startsWith("/c/")) {
            // UUID id = UUID.randomUUID(); //Universal Unique ID generator = an alternative for our Id generator
            int id = UniqueIdentifier.getIdentifier();
            clients.add(new ServerClient(string.substring(3, string.length()), packet.getAddress(), packet.getPort(), id));
            String ID = "/c/" + id;
            addPostfixAndSend(ID, packet.getAddress(), packet.getPort());
            System.out.println(string.substring(3, string.length()));
        } else if (string.startsWith("/m/")) {
            sendToAll(string);
        } else if (string.startsWith("/d/")) {
            String id = string.split("/d/|/e/")[1];
            disconnect(Integer.parseInt(id), true);
        } else
            System.out.println(string);
    }

    private void disconnect(int id, boolean disconnectionStatus){
        ServerClient c = null;
        for (int i = 0; i < clients.size(); i++){

            if (clients.get(i).getClientID() == id){
                c = clients.get(i);
                clients.remove(i);
                break;
            }
        }
            String msg;

            if (disconnectionStatus){
                msg = "Client " + c.name + " (ID: " + c.getClientID() + ") @ " + c.clientIPAddress.toString() + ":" + c.clientPort + " disconnected.";
            } else{
                msg = "Client " + c.name + " (ID: " + c.getClientID() + ") @ " + c.clientIPAddress.toString() + ":" + c.clientPort + " timed out.";
            }
            System.out.println(msg);
    }
}
