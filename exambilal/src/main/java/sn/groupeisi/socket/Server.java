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
    private int cpt;
    private String lastAddedUsername;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        IMember iMember = new MembreImpl();
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                // Créer un nouveau gestionnaire de client uniquement si la connexion est établie
                if (socket.isConnected()) {
                    Membre membre = iMember.lastMembre();
                    lastAddedUsername = membre.getUsername();
                    System.out.println(membre.getIdM());
                    System.out.println(lastAddedUsername + " Vient de se connecter");
                    ClientHandler clientHandler = new ClientHandler(socket);
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                }
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
