package src.main.java.server;

import java.net.InetAddress;

public class ServerClient {

    public String name;
    public InetAddress clientIPAddress;
    public int clientPort;
    public final int clientID;
    public  int attempt = 0; //is used to check if the client is still connected (sometimes his internet connection may drop out for a while
                             //in that case the server will try to sent the package once again and will increment attempt field. When
                             //attempt value is greater than maximum value, the server will drop the client - remove it from the list of clients)

    public ServerClient(String name, InetAddress clientIPAddress, int clientPort, int ID, int attempt) {
        this.name = name;
        this.clientIPAddress = clientIPAddress;
        this.clientPort = clientPort;
        this.clientID = ID;
        this.attempt = attempt;
    }

    public int getClientID() {
        return clientID;
    }
}
