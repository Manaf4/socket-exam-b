package sn.groupeisi.socket;

import sn.groupeisi.dao.DB;
import sn.groupeisi.entities.IMember;
import sn.groupeisi.entities.Membre;
import sn.groupeisi.entities.MembreImpl;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class Client {

    private Socket socket;
//    public static List<Membre> instanceClients = new ArrayList<>();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
//    private static int cpt = 0;
    public Client(Socket socket, String username){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;

//            cpt++;
            //instanceClients.add(cpt);
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(){
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);

            while(socket.isConnected()){
                String messageToSend = scanner.nextLine();

                if (messageToSend.equalsIgnoreCase("/quit")) {
                    bufferedWriter.write(messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    System.out.println("Bye vous avez quitte le chat");
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }

                bufferedWriter.write(username + " : " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

            }
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while (socket.isConnected()){
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        if (msgFromGroupChat == null || msgFromGroupChat.equals("/quit")) {
                            closeEverything(socket, bufferedReader, bufferedWriter);
                            break;
                        }

                        System.out.println(msgFromGroupChat);
                    }catch (IOException e){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws IOException {
        try{
            IMember iMember = new MembreImpl();
            Membre membre = new Membre();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Entrer votre nom d'utilisateur pour le chat");
            String username = scanner.nextLine();
            membre.setUsername(username);

            int ok = iMember.addMember(membre);
            Socket socket = new Socket("localhost", 1234);
            Client client = new Client(socket, username);
            System.out.println("Vous venez de vous connecter au serveur d'isi");
            client.listenForMessage();
            client.sendMessage();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}

