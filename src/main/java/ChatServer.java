import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatServer {

    public static final int PORT = 1234;

    private ServerSocket serverSocket;
    private List<Socket> clientsList;


    public ChatServer() throws java.io.IOException {
        this.serverSocket = new ServerSocket(PORT);
        serverSocket.setReuseAddress(true);
        this.clientsList = new ArrayList<Socket>();
    }


    public void start() throws java.io.IOException {
        printNetworkAddress();
        System.out.println("Accepting clients...");

        while (true) {

            Socket client = serverSocket.accept(); // Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made.
            clientsList.add(client);

            System.out.println("New client accepted..." + client.getRemoteSocketAddress());
            System.out.println("Total users: " + clientsList.size());

            ChatClientHandler clientHandler = new ChatClientHandler(client, this);
            Thread thread = new Thread(clientHandler);
            thread.start();

        }

    }

    public void printNetworkAddress() throws UnknownHostException {

        System.out.println("Network configuration: ");
        System.out.println("Host address: " + InetAddress.getLocalHost().getHostAddress());
        System.out.println("Host name: " + InetAddress.getLocalHost().getHostName());
    }

    public void sendMessageToAllClients(String msg) throws java.io.IOException {
        for (Iterator<Socket> socketIterator = clientsList.iterator(); socketIterator.hasNext(); ) {

            Socket client = socketIterator.next();
            if (!client.isClosed()) {

                PrintWriter outputStream = new PrintWriter(client.getOutputStream());
                outputStream.println(msg);
                outputStream.flush();
            }

        }

    }

    public static void main(String[] args) throws  java.io.IOException{
        ChatServer server = new ChatServer();
        server.start();
    }

}
