import java.net.Socket;
import java.util.Scanner;

public class ChatClientHandler implements Runnable{

private Socket client;
private ChatServer server;
private Scanner inputStream;


    public ChatClientHandler(Socket client, ChatServer serverSocket) {
        this.client = client;
        this.server = serverSocket;
    }

    public void run() {

        try {
            inputStream = new Scanner(client.getInputStream());

            while(true){

                if (!inputStream.hasNext()){
                    return;
                }
            String msg = inputStream.nextLine();
            System.out.println(client.getRemoteSocketAddress() + " said: " + msg);
            server.sendMessageToAllClients(msg);
            }
        }
        catch ( java.io.IOException e){

        }
    }
}
