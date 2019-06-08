package src.main.java;

import src.main.java.ChatServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

private Socket socket = null;
private Scanner inputSteram = null;
private String clientName;
private final int PORT;

    public Client() throws IOException {

    this.PORT = ChatServer.PORT;
    initialize();

    }

    private void initialize() throws IOException{

        System.out.println("What is the chat server's IP address?");
        Scanner scanner = new Scanner(System.in);
        String ipAddress = scanner.next();
        System.out.println("What is your socket name?");
        clientName = scanner.next();

        InetAddress host = null;
        try{
            host = InetAddress.getByName(ipAddress);
        }
        catch( java.net.UnknownHostException e){
            System.out.println("Host not found");
        }

        try{
            socket = new Socket(host, PORT);
            socket.setReuseAddress(true);
        }
        catch(IOException e){
            System.out.println("Not found");
        }

        System.out.println("You are now connected to: " + host.getHostAddress());

        PrintWriter outputStream = new PrintWriter(socket.getOutputStream());
        inputSteram = new Scanner(socket.getInputStream());

        Thread thread = new Thread(this);
        thread.start();

        while(scanner.hasNextLine()){
            String msg = scanner.nextLine();
            outputStream.println(clientName + " says: " + msg);
            outputStream.flush();
        }

    }



    public void run() {

        while (true) {
            if (inputSteram.hasNextLine()){}
            System.out.println(inputSteram.nextLine()); // to co wczesniej trafilo do obiektu-gniazda (przez printWriter) teraz skierowane jest strumieniem danych z gniazda

        }

    }

    public static void main(String[] args) throws Exception {
        new Client();
    }
}
