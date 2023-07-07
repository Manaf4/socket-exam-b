package sn.groupeisi.socket;

import sn.groupeisi.dao.IMembre;
import sn.groupeisi.entities.IMember;
import sn.groupeisi.entities.Membre;
import sn.groupeisi.entities.MembreImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private int connectionCount; // Compteur de connexions
    private String lastAddedUsername;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.connectionCount = 0; // Initialise le compteur à 0
    }

    public void startServer() {
        IMember iMember = new MembreImpl();
        try {
            while (!serverSocket.isClosed() && connectionCount < 4) {
                Socket socket = serverSocket.accept();
                if (socket.isConnected()) {
                    connectionCount++; // Incrémente le compteur à chaque connexion acceptée

                    Membre membre = iMember.lastMembre();
                    lastAddedUsername = membre.getUsername();
                    System.out.println(lastAddedUsername + " vient de se connecter");

                    ClientHandler clientHandler = new ClientHandler(socket);
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                }
            }
            if (connectionCount >= 4) {
                System.out.println("serveur plein...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeServer() {
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
