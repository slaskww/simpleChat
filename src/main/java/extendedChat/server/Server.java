package src.main.java.extendedChat.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server implements Runnable {

    private List<ServerClient> clients = new ArrayList<>();
    private List<Integer> clientsResponse = new ArrayList<>();
    private final int MAX_ATTEMPTS = 5; //max number of attempts to get response from a client

    private DatagramSocket socket; //a socket is an analogy to the post office, so we use the socket to sendToServer our packets
    private int port;
    private boolean isRunning = false;
    private Thread run, manage, receive, send;
    private boolean rawMsg= false; // this flag allows the server to see the actual packets of everything

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
        manageClients(); //this method is in its own thread (reason? it has an internal while infinite loop, which could freeze the entire chat)
        receive(); //this method is in its own thread (reason? it has an internal while infinite loop, which could freeze the entire chat

        Scanner serverInput = new Scanner(System.in);
        String text;
        while(isRunning){
            text = serverInput.nextLine();

        if (!text.startsWith("/")){ //if text from server input is not a special command, send that text as an ordinary message to all clients
            text = "/m/Server: " + text + "/e/";
            sendToAll(text);
            continue; //it breaks the current iteration in the loop and carry on with the next iteration in the loop
        }

        if (text.equals("/raw")){
            rawMsg = !rawMsg;
            continue; //it breaks the current iteration in the loop and carry on with the next iteration in the loop
        }

        if (text.equals("/clients")){
            System.out.println("Clients:");
            System.out.println("========");
            for (int i = 0; i < clients.size(); i++){
                ServerClient c = clients.get(i);
                System.out.format("%s(%d)\t%s:%d\n", c.name, c.getClientID(), c.clientIPAddress.toString(), c.clientPort);
            }
            System.out.println("========");


            }
        }
    }

    private void manageClients() {
        manage = new Thread("Manage") {
            public void run() {

                while (isRunning) {
                    String msg = "/i/server";
                    sendToAll(msg);
                    try{
                        Thread.sleep(1500);
                    } catch (java.lang.InterruptedException e){

                    }

                    for (int i = 0; i < clients.size(); i++){
                        ServerClient c = clients.get(i);
                        if (!clientsResponse.contains(c.getClientID())){
                            if (c.attempt > MAX_ATTEMPTS){
                                disconnect(c.getClientID(), DisconnectionStatus.ABORT);
                            } else {
                                c.attempt++;
                            }
                        } else {
                            clientsResponse.remove(c.getClientID());
                            c.attempt = 0;
                        }
                    }

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


        if (message.startsWith("/m/")){
            String text = message.substring(3);
            text = text.split("/e/")[0];
            System.out.println(text);
        }

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

        if (rawMsg) { // if rawMsg is true, print all packets (with prefixes)
            System.out.println(string);
        }

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
            disconnect(Integer.parseInt(id), DisconnectionStatus.QUIT);
        } else if (string.startsWith("/i/")) {
            String id = string.split("/i/|/e/")[1];
            clientsResponse.add(Integer.parseInt(id));
        } else
            System.out.println(string);
    }

    private void disconnect(int clientId, DisconnectionStatus disconnectionStatus){
        ServerClient c = null;
        for (int i = 0; i < clients.size(); i++){

            if (clients.get(i).getClientID() == clientId){
                c = clients.get(i);
                clients.remove(i);
                break;
            }
        }
            String msg;

            if (disconnectionStatus == DisconnectionStatus.QUIT){
                msg = "Client " + c.name + " (ID: " + c.getClientID() + ") @ " + c.clientIPAddress.toString() + ":" + c.clientPort + " disconnected.";
            } else{
                msg = "Client " + c.name + " (ID: " + c.getClientID() + ") @ " + c.clientIPAddress.toString() + ":" + c.clientPort + " timed out.";
            }
            System.out.println(msg);
    }
}
