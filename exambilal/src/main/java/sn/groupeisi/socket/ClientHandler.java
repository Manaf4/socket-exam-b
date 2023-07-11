package sn.groupeisi.socket;

import sn.groupeisi.entities.*;

import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientHandler implements Runnable {
    public static List<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    private IMember memberImpl;
    private CommentaireImpl commentaireImpl;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            this.memberImpl = new MembreImpl(); // Instantiate the MembreImpl class
            this.commentaireImpl = new CommentaireImpl(); // Instantiate the CommentaireImpl class
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " est entré dans le chat");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }



    @Override
    public void run() {
        String messageFromClient;
        boolean quit = false;
        try {
            while (socket.isConnected() && !quit) {
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient == null) {
                    break;
                }
                if (messageFromClient.equalsIgnoreCase("/quit")) {
                    System.out.println("SERVER: " + clientUsername + " a quitté le chat");
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
                // Create a Commentaire object
                Commentaire commentaire = new Commentaire();
                commentaire.setMessage(messageFromClient.substring(messageFromClient.indexOf(":") + 1).trim());

                commentaire.setDateC(new Timestamp(new Date().getTime()));



                // Get the Membre object
                Membre membre = memberImpl.getMembreByUsername(clientUsername);
                if (membre != null) {
                    commentaire.setMembre(membre);
                }
                //System.out.println(commentaire.toString());
                // Save the Commentaire object in the database
                int result = commentaireImpl.addCommentaire(commentaire);
                if (result > 0) {
                    broadcastMessage(messageFromClient);
                } else {
                    System.out.println("Failed to save the commentaire");
                }
                //broadcastMessage(messageFromClient);
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }



    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " est sorti du chat");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
