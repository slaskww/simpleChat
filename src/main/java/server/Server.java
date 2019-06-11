package src.main.java.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server implements Runnable {

    private List<ServerClient> clients = new ArrayList<>();

    private DatagramSocket socket;
    private int port;
    private boolean isRunning = false;
    private Thread run, manage, receive, send;

    public Server(int port) {
        this.port = port;
        try {
            socket = new DatagramSocket(port);
        } catch (java.net.SocketException e){
        }

        run = new Thread(this, "Server");
        run.start();
    }

    @Override
    public void run() {
        isRunning = true;
        System.out.println("Server started on port " + port);
        manageClients();
        receive();
    }

    private void manageClients(){
        manage = new Thread("Manage"){
            public void run(){

                while (isRunning){
                    //managing
                }
            }
        };
        manage.start();
    }


    private void receive(){
        receive = new Thread("Receive"){
            public void run(){
                while (isRunning){
                byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                   try{ socket.receive(packet);
                   } catch ( java.io.IOException e){

                   }

                   String string = new String(packet.getData());
                }
            }
        };
        receive.start();
    }
}
